package co.kr.notepad.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import co.kr.notepad.R
import co.kr.notepad.databinding.FragmentListBinding
import co.kr.notepad.presentation.ui.adapter.MemoAdapter
import co.kr.notepad.presentation.ui.base.BaseFragment

class ListFragment : BaseFragment<FragmentListBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_list
    private var _memoAdapter: MemoAdapter? = null
    private val memoAdapter get() = _memoAdapter ?: error("adapter not initialized")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initOnClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _memoAdapter = null
    }

    private fun initView() {
        _memoAdapter = MemoAdapter()
        binding.rvMemo.adapter = memoAdapter
    }

    private fun initOnClickListener() {
        with(binding) {
            fabAdd.setOnClickListener {
                parentFragmentManager.commit {
                    replace<WriteFragment>(R.id.fcv_main)
                    addToBackStack(null)
                }
            }
        }
    }

    companion object {
        fun newInstance() = ListFragment()
    }
}