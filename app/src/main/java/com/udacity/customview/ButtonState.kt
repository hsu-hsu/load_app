package com.udacity.customview

import com.udacity.R


sealed class ButtonState(var buttonText: Int) {

    object Clicked : ButtonState(R.string.button_text_click)
    object Loading : ButtonState(R.string.button_text_loading)
    object Completed : ButtonState(R.string.button_text_downloaded)
}