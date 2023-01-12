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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.kr.notepad.R
import co.kr.notepad.databinding.FragmentListBinding
import co.kr.notepad.presentation.adapter.MemoAdapter
import co.kr.notepad.presentation.ui.base.BaseFragment
import co.kr.notepad.presentation.ui.write.WriteFragment
import co.kr.notepad.presentation.viewmodel.ListViewModel
import co.kr.notepad.presentation.viewmodel.UiState
import co.kr.notepad.util.showErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

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
    private val onBackPressedCallback: OnBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
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
    }
    private var isMenuProviderAdded = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.app_name)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.memos.collect { uiState ->
                        when (uiState) {
                            is UiState.Init -> {
                                binding.progressBar.hide()
                                memoAdapter.updateList(emptyList())
                            }
                            is UiState.Loading -> {
                                binding.progressBar.show()
                            }
                            is UiState.Success -> {
                                binding.progressBar.hide()
                                memoAdapter.updateList(uiState.data)
                            }
                            is UiState.Failure -> {
                                binding.progressBar.hide()
                                requireContext().showErrorMessage()
                            }
                        }
                    }
                }

                launch {
                    viewModel.selectedMemos.collect { uiState ->
                        when (uiState) {
                            is UiState.Init -> {
                                binding.progressBar.hide()
                                memoAdapter.updateSelectedItems(emptyList())
                            }
                            is UiState.Loading -> {
                                binding.progressBar.show()
                            }
                            is UiState.Success -> {
                                Timber.tag("selected memo").i("collected")
                                binding.progressBar.hide()
                                uiState.data.run {
                                    memoAdapter.updateSelectedItems(this)
                                    binding.fabAdd.isEnabled = this.isEmpty()
                                    if (this.isEmpty()) {
                                        removeMenuProvider()
                                    } else {
                                        addMenuProvider()
                                    }
                                }
                            }
                            is UiState.Failure -> {
                                binding.progressBar.hide()
                                requireContext().showErrorMessage()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addMenuProvider() {
        if (!isMenuProviderAdded) {
            isMenuProviderAdded = true
            (requireActivity() as MenuHost).addMenuProvider(menuProvider)
        }
    }

    private fun removeMenuProvider() {
        if (isMenuProviderAdded) {
            isMenuProviderAdded = false
            (requireActivity() as MenuHost).removeMenuProvider(menuProvider)
        }
    }

    companion object {
        fun newInstance(): ListFragment = ListFragment()
    }
}