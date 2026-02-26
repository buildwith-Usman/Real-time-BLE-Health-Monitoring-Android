package com.app.dbraze.presentation.ui.fragment


import android.app.DatePickerDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.dbraze.R
import com.app.dbraze.base.BaseFragment
import com.app.dbraze.databinding.SignUpFragmentBinding
import com.app.dbraze.presentation.model.UserModel
import com.app.dbraze.presentation.model.toMap
import com.app.dbraze.presentation.ui.activities.DBrazeDashboardActivity
import com.app.dbraze.presentation.viewmodel.SignUpViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class SignUpFragment : BaseFragment<SignUpFragmentBinding, SignUpViewModel>() {

    override val viewModel: SignUpViewModel by viewModels()

    private lateinit var selectedGender: String

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): SignUpFragmentBinding {
        return SignUpFragmentBinding.inflate(inflater, container, false)
    }

    override fun setupViews(view: View) {
        setUpGenderDropDown()
        setUpVars()
        handleClicks()
    }

    private fun setUpVars() {
     //
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewModel.validationErrors.observe(viewLifecycleOwner) { errors ->
            binding.emailInputLayout.error = errors["email"]
            binding.setPasswordView.passwordInputLayout.error = errors["password"]
            binding.setPasswordView.confirmPasswordInputLayout.error = errors["confirmPassword"]
        }

        viewModel.navigateToPasswordView.observe(viewLifecycleOwner) {
            if (it) {
                showSetPasswordView()
            }
        }

        viewModel.isValidPasswordForm.observe(viewLifecycleOwner) {
            if (it) {
                val userModel = createSignUpUserModel()
                val signUpUserData = userModel.toMap()
                viewModel.createUser(signUpUserData)
            }
        }

        viewModel.signUpResult.observe(viewLifecycleOwner){
            if(it.isSuccess){
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

    private fun showSetPasswordView() {
        binding.setPasswordView.main.visibility = View.VISIBLE
        binding.signUpPersonalInformation.visibility = View.GONE
    }

    private fun hideSetPasswordView() {
        binding.setPasswordView.main.visibility = View.GONE
        binding.signUpPersonalInformation.visibility = View.VISIBLE
    }

    private fun setUpGenderDropDown() {
        val items = resources.getStringArray(R.array.gender_items)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_gender_item, items)

        binding.autoCompleteTextView.setAdapter(adapter)
        binding.autoCompleteTextView.setOnClickListener {
            binding.autoCompleteTextView.showDropDown()
        }
        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            selectedGender = parent.getItemAtPosition(position).toString()
        }
    }

    private fun handleClicks() {

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_setPasswordFragment)
        }

        binding.backSignUp.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.dateOfBirthInputLayout.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnNext.setOnClickListener {
            val email = binding.etEmail.text.toString()
            viewModel.validatePersonalInformationForm(email)
        }

        binding.setPasswordView.backPassword.setOnClickListener {
            hideSetPasswordView()
        }

        binding.setPasswordView.btnSignUpPassword.setOnClickListener {
            val password = binding.setPasswordView.etPassword.text.toString()
            val confirmPassword = binding.setPasswordView.etConfirmPassword.text.toString()
            viewModel.validatePasswordForm(password, confirmPassword)
        }
    }

    private fun createSignUpUserModel(): UserModel {

        val fullName = binding.etFullName.text.toString().takeIf { it.isNotEmpty() } ?: ""
        val email = binding.etEmail.text.toString()
        val height = binding.etHeight.text.toString().toFloatOrNull() ?: 0.0f
        val weight = binding.etWeight.text.toString().toFloatOrNull() ?: 0.0f
        val dateOfBirth =
            binding.dateOfBirthInputLayout.text.toString().takeIf { it.isNotEmpty() } ?: ""
        val password = binding.setPasswordView.etPassword.text.toString()
        val gender = if (::selectedGender.isInitialized) selectedGender else ""

        val userModel = UserModel(
            fullName = fullName,
            email = email,
            gender = gender,
            height = height,
            weight = weight,
            dateOfBirth = dateOfBirth,
            password = password
        )
        return userModel
    }

    private fun showDatePickerDialog() {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = context?.let {
            DatePickerDialog(
                it, { _: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val selectedDate =
                        selectedDay.toString() + "/" + (selectedMonth + 1) + "/" + selectedYear
                    binding.dateOfBirthInputLayout.text = selectedDate
                }, year, month, day
            )
        }

        datePickerDialog?.show()
    }

    companion object {
        private const val TAG = "DBrazeLogin"
    }

}