package com.app.dbraze.presentation.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.dbraze.R
import com.app.dbraze.base.BaseFragment
import com.app.dbraze.databinding.LoginFragmentBinding
import com.app.dbraze.presentation.model.UserModel
import com.app.dbraze.presentation.model.toMap
import com.app.dbraze.presentation.ui.activities.DBrazeDashboardActivity
import com.app.dbraze.presentation.viewmodel.LoginViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): LoginFragmentBinding {
        return LoginFragmentBinding.inflate(inflater, container, false)
    }

    override fun setupViews(view: View) {
        handleClicks()
    }


    override fun observeViewModel() {
        super.observeViewModel()
        viewModel.validationErrors.observe(viewLifecycleOwner) { errors ->
            binding.emailInputLayout.error = errors["email"]
            binding.passwordInputLayout.error = errors["password"]
        }

        viewModel.isValidLoginForm.observe(viewLifecycleOwner) {
            if (it) {
                val userModel = createLoginUserModel()
                val loginUserData = userModel.toMap()
                viewModel.loginUser(loginUserData)
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner){
            if (it.isSuccess){
                val intent = Intent(requireContext(), DBrazeDashboardActivity::class.java)
                startActivity(intent)
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

    private fun handleClicks() {
        binding.signUpTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.validateForm(email, password)
        }
    }

    private fun createLoginUserModel(): UserModel {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val userModel = UserModel(
            email = email, password = password
        )
        return userModel
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    companion object {
        private const val TAG = "DBrazeLogin"
    }
}