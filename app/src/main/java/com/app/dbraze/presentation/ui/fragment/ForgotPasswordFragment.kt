package com.app.dbraze.presentation.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.dbraze.base.BaseFragment
import com.app.dbraze.databinding.ForgotPasswordFragmentBinding
import com.app.dbraze.presentation.viewmodel.ForgotPasswordViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<ForgotPasswordFragmentBinding, ForgotPasswordViewModel>() {

    override val viewModel: ForgotPasswordViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ForgotPasswordFragmentBinding {
        return ForgotPasswordFragmentBinding.inflate(inflater, container, false)
    }

    override fun setupViews(view: View) {
        handleClicks()
    }

    private fun handleClicks() {

        binding.backForgotPassword.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSend.setOnClickListener {
            viewModel.validateEmail(binding.etEmail.text.toString())
        }

    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewModel.validationErrors.observe(viewLifecycleOwner) { errors ->
            binding.emailInputLayout.error = errors["email"]
        }

        viewModel.isValidEmail.observe(viewLifecycleOwner) {
            if (it) {
                val email = binding.etEmail.text.toString()
                viewModel.resetPassword(email)
            }
        }

        viewModel.resetPasswordEmail.observe(viewLifecycleOwner){
            if(it.isSuccess){
                Toast.makeText(requireContext(),"Email Sent Successfully",Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }
}