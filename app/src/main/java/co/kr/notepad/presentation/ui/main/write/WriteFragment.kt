package co.kr.notepad.presentation.ui.main.write

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
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

    private val writeViewModel by viewModels<WriteViewModel>()
    private val menuProvider = (object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_write, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_save -> {
                    writeViewModel.insertOrUpdate(
                        memoId = memoId,
                        text = binding.editTextField.text.toString()
                    )
                    true
                }
                else -> false
            }
        }
    })
    private var memoId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memoId = arguments?.getLong(MEMO_ID) ?: 0L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loadData()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeMenuProvider()
    }

    private fun initView() {
        initToolbar()
    }

    private fun loadData() {
        writeViewModel.getMemo(memoId)
    }

    private fun observeData() {
        with(writeViewModel) {
            isSaved.observe(viewLifecycleOwner) {
                if (it) {
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Not saved", Toast.LENGTH_SHORT).show()
                }
            }
            memo.observe(viewLifecycleOwner) {
                binding.editTextField.setText(it.text)
            }
        }
    }

    private fun initToolbar() {
        (requireActivity() as MenuHost).addMenuProvider(menuProvider)
    }

    private fun removeMenuProvider() {
        (requireActivity() as MenuHost).removeMenuProvider(menuProvider)
    }

    companion object {
        private const val MEMO_ID = "MEMO_ID"
        fun newInstance(memoId: Long = 0L) = WriteFragment().apply {
            arguments = Bundle().apply { putLong(MEMO_ID, memoId) }
        }
    }
}