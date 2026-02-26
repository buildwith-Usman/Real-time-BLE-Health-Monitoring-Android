package com.app.dbraze.presentation.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.app.dbraze.R
import com.app.dbraze.base.BaseActivity
import com.app.dbraze.databinding.ActivityDbrazeDashboardBinding
import com.app.dbraze.databinding.ActivityDbrazeStartBinding
import com.app.dbraze.presentation.viewmodel.DBrazeDashBoardViewModel
import com.app.dbraze.presentation.viewmodel.DBrazeMainViewModel
import com.app.dbraze.presentation.viewmodel.DBrazeSplashViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DBrazeDashboardActivity : BaseActivity<DBrazeDashBoardViewModel,ActivityDbrazeDashboardBinding>() {

    override val viewModel: DBrazeDashBoardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.dashboard_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavView = findViewById<BottomNavigationView>(R.id.d_braze_bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.statsFragment,R.id.infoFragment,R.id.profileFragment -> {
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.dBrazeBottomNavigation.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomAppBar.visibility = View.GONE
                    binding.dBrazeBottomNavigation.visibility = View.GONE
                    binding.floatingActionButton.visibility = View.GONE
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_dbraze_dashboard
    }

    override fun getViewModelClass(): Class<DBrazeDashBoardViewModel> {
        return DBrazeDashBoardViewModel::class.java
    }

    override fun inflateBinding(): ActivityDbrazeDashboardBinding {
        return ActivityDbrazeDashboardBinding.inflate(layoutInflater)
    }


}