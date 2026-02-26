package com.app.dbraze.presentation.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.dbraze.R
import com.app.dbraze.base.BaseFragment
import com.app.dbraze.databinding.WelcomeFragmentBinding
import com.app.dbraze.presentation.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<WelcomeFragmentBinding, WelcomeViewModel>() {

    override val viewModel: WelcomeViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): WelcomeFragmentBinding {
        // Inflate the ViewBinding
        return WelcomeFragmentBinding.inflate(inflater, container, false)
    }

    override fun setupViews(view: View) {
        handleClicks()
    }

    private fun handleClicks() {

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
        }
    }


}
