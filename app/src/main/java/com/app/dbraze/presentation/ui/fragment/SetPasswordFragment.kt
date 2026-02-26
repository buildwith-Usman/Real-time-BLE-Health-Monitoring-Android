package com.app.dbraze.presentation.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.dbraze.base.BaseFragment
import com.app.dbraze.databinding.LoginFragmentBinding
import com.app.dbraze.databinding.SetPasswordFragmentBinding
import com.app.dbraze.presentation.viewmodel.LoginViewModel
import com.app.dbraze.presentation.viewmodel.SetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetPasswordFragment : BaseFragment<SetPasswordFragmentBinding, SetPasswordViewModel>() {

    override val viewModel: SetPasswordViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SetPasswordFragmentBinding {
        // Inflate the ViewBinding
        return SetPasswordFragmentBinding.inflate(inflater, container, false)
    }

    override fun setupViews(view: View) {
        handleClicks()
    }

    private fun handleClicks() {

        binding.backPassword.setOnClickListener {
            findNavController().navigateUp()
        }

    }
}