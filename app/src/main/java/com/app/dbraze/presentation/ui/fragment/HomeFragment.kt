package com.app.dbraze.presentation.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.dbraze.R
import com.app.dbraze.base.BaseFragment
import com.app.dbraze.databinding.HomeFragmentBinding
import com.app.dbraze.databinding.WelcomeFragmentBinding
import com.app.dbraze.presentation.viewmodel.HomeViewModel
import com.app.dbraze.presentation.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater, container, false)
    }

    override fun setupViews(view: View) {
        handleClicks()
    }

    private fun handleClicks() {
        binding.bluetoothIcon.setOnClickListener {
            findNavController().navigate(R.id.homeFragment_to_blueToothFragment)
        }
    }


}
