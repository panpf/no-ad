package com.github.panpf.noad.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseBindingFragment<VIEW_BINDING : ViewBinding> : Fragment() {

    protected var binding: VIEW_BINDING? = null

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = createViewBinding(inflater, container)
        this.binding = binding
        return binding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = this.binding!!
        onInitViews(binding, savedInstanceState)
        onInitData(binding, savedInstanceState)
    }

    override fun onDestroyView() {
        this.binding = null
        super.onDestroyView()
    }

    protected abstract fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): VIEW_BINDING

    protected abstract fun onInitViews(binding: VIEW_BINDING, savedInstanceState: Bundle?)
    protected abstract fun onInitData(binding: VIEW_BINDING, savedInstanceState: Bundle?)
}