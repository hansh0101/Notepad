package co.kr.notepad.presentation.ui.write

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private val memoId: Long by lazy { arguments?.getLong(MEMO_ID) ?: 0L }
    private val memoTitle: String get() = binding.editTextTitle.text.toString()
    private val memoText: String get() = binding.editTextField.text.toString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loadData()
        observeData()
    }

    override fun onStop() {
        super.onStop()
        writeViewModel.insertOrUpdate(memoId = memoId, title = memoTitle, text = memoText)
    }

    private fun initView() {
        with((activity as? AppCompatActivity)?.supportActionBar) {
            this?.title = "Write memo"
        }
    }

    private fun loadData() {
        writeViewModel.getMemo(memoId)
    }

    private fun observeData() {
        with(writeViewModel) {
            isErrorOccurred.observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), "Not saved", Toast.LENGTH_SHORT).show()
                }
            }
            memo.observe(viewLifecycleOwner) {
                binding.editTextTitle.setText(it.title)
                binding.editTextField.setText(it.text)
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