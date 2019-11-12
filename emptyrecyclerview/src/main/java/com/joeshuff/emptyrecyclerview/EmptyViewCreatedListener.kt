package com.joeshuff.emptyrecyclerview

import android.view.View

interface EmptyViewCreatedListener {
    fun onCreated(view: View) //Called when the view is created and added to the layout.
    fun onShown(view: View?) //Called when the EmptyIndicator is shown on screen so you can update it with anything you like, if you like.
}