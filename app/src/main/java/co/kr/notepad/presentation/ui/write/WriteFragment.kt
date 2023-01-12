package co.kr.notepad.presentation.ui.write

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.kr.notepad.R
import co.kr.notepad.databinding.FragmentWriteBinding
import co.kr.notepad.presentation.ui.base.BaseFragment
import co.kr.notepad.presentation.viewmodel.UiState
import co.kr.notepad.presentation.viewmodel.WriteViewModel
import co.kr.notepad.util.showErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WriteFragment : BaseFragment<FragmentWriteBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_write
    override val TAG: String
        get() = this::class.java.simpleName

    private val viewModel by viewModels<WriteViewModel>()
    private val menuProvider = (object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_write, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_image -> {
                    when {
                        checkSelfPermissionGranted() -> {
                            navigateGallery()
                        }
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                            showInContextUI()
                        }
                        else -> {
                            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                    true
                }
                else -> false
            }
        }
    })
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            when (permissionGranted) {
                true -> navigateGallery()
                false -> Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.gallery_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    private val galleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                viewModel.imageUri.value = result.data?.data
            }
        }
    private val memoId: Long by lazy { arguments?.getLong(MEMO_ID) ?: 0L }
    private val memoTitle: String get() = binding.editTextTitle.text.toString()
    private val memoText: String get() = binding.editTextField.text.toString()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.insertOrUpdate(memoId = memoId, title = memoTitle, text = memoText)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initOnClickListener()
        fetchData()
        observeData()
    }

    private fun initView() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.write_fragment_title)
        (requireActivity() as MenuHost).addMenuProvider(
            menuProvider,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun initOnClickListener() {
        binding.imageClear.setOnClickListener {
            viewModel.imageUri.value = null
        }
    }

    private fun fetchData() {
        viewModel.getMemo(memoId)
    }

    private fun observeData() {
        observeMemo()
        observeImageUri()
        observeIsSaved()
    }

    private fun checkSelfPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun navigateGallery() {
        galleryLauncher.launch(Intent(Intent.ACTION_PICK).apply {
            type = MediaStore.Images.Media.CONTENT_TYPE
        })
    }

    private fun showInContextUI() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.need_permission))
            .setMessage(resources.getString(R.string.need_external_storage_permission))
            .setPositiveButton(resources.getString(R.string.agree)) { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton(resources.getString(R.string.refuse)) { _, _ ->
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.no_external_storage_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .create()
            .show()
    }

    private fun observeMemo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.memo.collect { uiState ->
                        when (uiState) {
                            is UiState.Init -> {
                                binding.progressBar.hide()
                            }
                            is UiState.Loading -> {
                                binding.progressBar.show()
                            }
                            is UiState.Success -> {
                                binding.progressBar.hide()
                                uiState.data.run {
                                    binding.editTextTitle.setText(this.title)
                                    binding.editTextField.setText(this.text)
                                }
                            }
                            is UiState.Failure -> {
                                binding.progressBar.hide()
                                requireContext().showErrorMessage()
                                parentFragmentManager.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeImageUri() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.imageUri.collect { imageUri ->
                        binding.image.setImageURI(imageUri)
                        binding.imageClear.isVisible = imageUri != null
                    }
                }
            }
        }
    }

    private fun observeIsSaved() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isSaved.collect { uiState ->
                        when (uiState) {
                            is UiState.Init -> {}
                            is UiState.Loading -> {
                                binding.progressBar.show()
                            }
                            is UiState.Success -> {
                                binding.progressBar.hide()
                                parentFragmentManager.popBackStack()
                            }
                            is UiState.Failure -> {
                                binding.progressBar.hide()
                                requireContext().showErrorMessage()
                                parentFragmentManager.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val MEMO_ID = "MEMO_ID"
        fun newInstance(memoId: Long = 0L) = WriteFragment().apply {
            arguments = Bundle().apply { putLong(MEMO_ID, memoId) }
        }
    }
}