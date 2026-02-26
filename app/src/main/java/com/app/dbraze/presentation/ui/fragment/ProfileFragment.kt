package com.app.dbraze.presentation.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.dbraze.R
import com.app.dbraze.base.BaseFragment
import com.app.dbraze.databinding.ProfileFragmentBinding
import com.app.dbraze.databinding.WelcomeFragmentBinding
import com.app.dbraze.presentation.viewmodel.ProfileViewModel
import com.app.dbraze.presentation.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): ProfileFragmentBinding {
        return ProfileFragmentBinding.inflate(inflater, container, false)
    }

    override fun setupViews(view: View) {
        handleClicks()
    }

    private fun handleClicks() {

    }


}
