package co.kr.notepad.presentation.ui.list

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import co.kr.notepad.R
import co.kr.notepad.databinding.FragmentListBinding
import co.kr.notepad.presentation.adapter.MemoAdapter
import co.kr.notepad.presentation.ui.base.BaseFragment
import co.kr.notepad.presentation.ui.write.WriteFragment
import co.kr.notepad.presentation.viewmodel.ListViewModel
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
    private val menuProvider = (object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_list, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_delete -> {
                    listViewModel.delete()
                    true
                }
                else -> false
            }
        }
    })
    private var isMenuProviderAdded = false
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (isMenuProviderAdded) {
                    true -> {
                        listViewModel.clearSelectedMemos()
                    }
                    false -> {
                        if (parentFragmentManager.backStackEntryCount != 0) {
                            parentFragmentManager.popBackStack()
                        } else {
                            if (!requireActivity().isFinishing) {
                                requireActivity().finish()
                            }
                        }
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

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

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    private fun initView() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Notepad"
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
        with(listViewModel) {
            memos.observe(viewLifecycleOwner) {
                memoAdapter.updateList(it)
            }
            selectedMemos.observe(viewLifecycleOwner) {
                memoAdapter.updateSelectedItems(it)
                if (it.isNotEmpty()) {
                    binding.fabAdd.isEnabled = false
                    if (!isMenuProviderAdded) {
                        isMenuProviderAdded = true
                        addMenuProvider()
                    }
                } else {
                    binding.fabAdd.isEnabled = true
                    if (isMenuProviderAdded) {
                        isMenuProviderAdded = false
                        removeMenuProvider()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        _memoAdapter = MemoAdapter(
            onItemClick = {
                val isSelectMode = (memoAdapter.getSelectedItemCount() != 0)
                if (memoAdapter.getSelectedItemCount() == 0) {
                    parentFragmentManager.commit {
                        replace(R.id.fcv_main, WriteFragment.newInstance(it.id))
                        addToBackStack(null)
                    }
                } else {
                    listViewModel.updateSelectedMemos(it)
                }
                isSelectMode
            },
            onItemSelect = {
                listViewModel.updateSelectedMemos(it)
            })
        binding.rvMemo.adapter = memoAdapter
    }

    private fun addMenuProvider() {
        (requireActivity() as MenuHost).addMenuProvider(menuProvider)
    }

    private fun removeMenuProvider() {
        (requireActivity() as MenuHost).removeMenuProvider(menuProvider)
    }
}