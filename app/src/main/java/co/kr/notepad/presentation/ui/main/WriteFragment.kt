package co.kr.notepad.presentation.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import co.kr.notepad.R
import co.kr.notepad.databinding.FragmentWriteBinding
import co.kr.notepad.presentation.ui.base.BaseFragment

class WriteFragment : BaseFragment<FragmentWriteBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_write
    private val menuProvider = (object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_write, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.menu_save -> {
                    parentFragmentManager.popBackStack()
                    true
                }
                else -> false
            }
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeMenuProvider()
    }

    private fun initView() {
        initToolbar()
    }

    private fun initToolbar() {
        (requireActivity() as MenuHost).addMenuProvider(menuProvider)
    }

    private fun removeMenuProvider() {
        (requireActivity() as MenuHost).removeMenuProvider(menuProvider)
    }

    companion object {
        fun newInstance() = WriteFragment
    }
}