package com.dice.themovie.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory {
    inline fun <viewModel: ViewModel> viewModelFactory(crossinline f: () -> viewModel) =
        object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = f() as T
        }
}