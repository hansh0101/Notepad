package co.kr.notepad.presentation.ui

import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import co.kr.notepad.R
import co.kr.notepad.databinding.ActivityMainBinding
import co.kr.notepad.presentation.ui.base.BaseActivity
import co.kr.notepad.presentation.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initView()
        }
    }

    private fun initView() {
        when (supportFragmentManager.backStackEntryCount) {
            0 -> {
                supportFragmentManager.commit {
                    add<ListFragment>(R.id.fcv_main)
                }
            }
            else -> {

            }
        }
    }
}