package co.kr.notepad.util

import android.content.Context
import android.widget.Toast
import co.kr.notepad.R

fun Context.showErrorMessage() =
    Toast.makeText(
        this,
        resources.getString(R.string.something_went_wrong),
        Toast.LENGTH_SHORT
    ).show()