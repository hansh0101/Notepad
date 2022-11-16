package co.kr.notepad.presentation.ui.main.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import co.kr.notepad.R
import co.kr.notepad.databinding.FragmentListBinding
import co.kr.notepad.presentation.ui.adapter.MemoAdapter
import co.kr.notepad.presentation.ui.base.BaseFragment
import co.kr.notepad.presentation.ui.main.write.WriteFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_list
    override val TAG: String
        get() = this::class.java.simpleName
    private val listViewModel by viewModels<ListViewModel>()
    private var _memoAdapter: MemoAdapter? = null
    private val memoAdapter get() = _memoAdapter ?: error("adapter not initialized")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initOnClickListener()
        loadData()
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _memoAdapter = null
    }

    private fun initView() {
        initRecyclerView()
    }

    private fun initOnClickListener() {
        with(binding) {
            fabAdd.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.fcv_main, WriteFragment.newInstance())
                    addToBackStack(null)
                }
            }
        }
    }

    private fun loadData() {
        listViewModel.getAll()
    }

    private fun observeData() {
        listViewModel.memos.observe(viewLifecycleOwner) {
            memoAdapter.updateList(it)
        }
    }

    private fun initRecyclerView() {
        _memoAdapter = MemoAdapter({
            parentFragmentManager.commit {
                replace(R.id.fcv_main, WriteFragment.newInstance(it))
                addToBackStack(null)
            }
        }, {
            AlertDialog.Builder(requireContext())
                .setMessage("Are you sure you want to erase the memo?")
                .setTitle("Erase the memo")
                .setPositiveButton("YES") { _, _ -> listViewModel.delete(it) }
                .setNegativeButton("NO") { _, _ -> }
                .show()
        })
        binding.rvMemo.adapter = memoAdapter
    }

    companion object {
        fun newInstance() = ListFragment()
    }
}