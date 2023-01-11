package co.kr.notepad.presentation.ui.write

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import co.kr.notepad.R
import co.kr.notepad.databinding.FragmentWriteBinding
import co.kr.notepad.presentation.ui.base.BaseFragment
import co.kr.notepad.presentation.viewmodel.WriteViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when (it) {
                true -> navigateGallery()
                false -> Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.gallery_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    private val galleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                viewModel.imageUri.value = it.data?.data
            }
        }
    private val memoId: Long by lazy { arguments?.getLong(MEMO_ID) ?: 0L }
    private val memoTitle: String get() = binding.editTextTitle.text.toString()
    private val memoText: String get() = binding.editTextField.text.toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initOnClickListener()
        loadData()
        observeData()
    }

    override fun onStop() {
        super.onStop()
        viewModel.insertOrUpdate(memoId = memoId, title = memoTitle, text = memoText)
    }

    private fun initView() {
        with((activity as? AppCompatActivity)?.supportActionBar) {
            this?.title = "Write memo"
        }
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

    private fun loadData() {
        viewModel.getMemo(memoId)
    }

    private fun observeData() {
        with(viewModel) {
            isErrorOccurred.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), "Not saved", Toast.LENGTH_SHORT).show()
                }
            }
            memo.observe(viewLifecycleOwner) {
                binding.editTextTitle.setText(it.title)
                binding.editTextField.setText(it.text)
            }
            imageUri.observe(viewLifecycleOwner) {
                binding.image.setImageURI(it)
                binding.imageClear.isVisible = it != null
            }
        }
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
            .setTitle("권한 동의 필요")
            .setMessage("프로필 사진 수정을 위해 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton("거부") { _, _ ->
                Toast.makeText(requireContext(), "갤러리 접근 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

    companion object {
        private const val MEMO_ID = "MEMO_ID"
        fun newInstance(memoId: Long = 0L) = WriteFragment().apply {
            arguments = Bundle().apply { putLong(MEMO_ID, memoId) }
        }
    }
}