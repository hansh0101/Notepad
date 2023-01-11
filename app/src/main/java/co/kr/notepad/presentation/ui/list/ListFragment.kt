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

    private val viewModel by viewModels<ListViewModel>()
    private val memoAdapter: MemoAdapter by lazy {
        MemoAdapter(
            onItemClick = {
                val isSelectMode = (memoAdapter.getSelectedItemCount() != 0)
                if (memoAdapter.getSelectedItemCount() == 0) {
                    parentFragmentManager.commit {
                        replace(R.id.fcv_main, WriteFragment.newInstance(it.id))
                        addToBackStack(null)
                    }
                } else {
                    viewModel.updateSelectedMemos(it)
                }
                isSelectMode
            },
            onItemSelect = {
                viewModel.updateSelectedMemos(it)
            })
    }
    private val menuProvider = (object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_list, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_delete -> {
                    viewModel.delete()
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
                        viewModel.clearSelectedMemos()
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
        requireActivity().onBackPressedDispatcher
            .addCallback(onBackPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initOnClickListener()
        fetchData()
        observeData()
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    private fun initView() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Notepad"
        binding.rvMemo.adapter = memoAdapter
    }

    private fun initOnClickListener() {
        binding.run {
            fabAdd.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.fcv_main, WriteFragment.newInstance())
                    addToBackStack(null)
                }
            }
        }
    }

    private fun fetchData() {
        viewModel.getAll()
    }

    private fun observeData() {
        viewModel.run {
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

    private fun addMenuProvider() {
        (requireActivity() as MenuHost).addMenuProvider(menuProvider)
    }

    private fun removeMenuProvider() {
        (requireActivity() as MenuHost).removeMenuProvider(menuProvider)
    }
}