package com.github.panpf.noad.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.github.panpf.noad.R
import com.github.panpf.noad.base.BaseBindingFragment
import com.github.panpf.noad.databinding.FragmentSkipAdBinding
import com.github.panpf.noad.vm.SkipAdViewModel
import com.github.panpf.tools4a.toast.ktx.showLongToast

class SkipAdFragment : BaseBindingFragment<FragmentSkipAdBinding>() {

    private val viewModel by viewModels<SkipAdViewModel>()

    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) = FragmentSkipAdBinding.inflate(inflater, parent, false)

    override fun onInitViews(binding: FragmentSkipAdBinding, savedInstanceState: Bundle?) {
        binding.skipAdFragmentHeaderView.setOnClickListener {
            if (viewModel.powerStateData.value == false) {
                AlertDialog.Builder(requireActivity()).apply {
                    setTitle(R.string.dialog_title_prompt)
                    setMessage(R.string.dialog_enableSkipAdService_message)
                    setNegativeButton(R.string.dialog_button_cancel, null)
                    setPositiveButton(R.string.dialog_enableSkipAdService_button_confirm) { _, _ ->
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    }
                }.show()
            } else {
                showLongToast(R.string.toast_service_enabled)
            }
        }
    }

    override fun onInitData(binding: FragmentSkipAdBinding, savedInstanceState: Bundle?) {
        viewModel.powerStateData.observe(viewLifecycleOwner) {
            val bgColorId = if (it == true) R.color.power_on else R.color.power_off
            val bgColor = ResourcesCompat.getColor(requireContext().resources, bgColorId, null)
            binding.skipAdFragmentHeaderView.setBackgroundColor(bgColor)
            val stateId = if (it == true) R.string.power_on else R.string.power_off
            binding.skipAdFragmentStateText.text = getText(stateId)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPowerState()
    }
}