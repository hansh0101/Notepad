package co.kr.notepad.presentation.ui.main

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import co.kr.notepad.R
import co.kr.notepad.databinding.ActivityMainBinding
import co.kr.notepad.presentation.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        supportFragmentManager.commit {
            replace<ListFragment>(R.id.fcv_main)
        }
    }
}