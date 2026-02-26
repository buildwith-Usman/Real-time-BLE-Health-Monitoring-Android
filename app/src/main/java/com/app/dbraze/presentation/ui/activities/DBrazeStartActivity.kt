package com.app.dbraze.presentation.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.app.dbraze.R
import com.app.dbraze.base.BaseActivity
import com.app.dbraze.databinding.ActivityDbrazeStartBinding
import com.app.dbraze.presentation.viewmodel.DBrazeMainViewModel
import com.app.dbraze.presentation.viewmodel.DBrazeSplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DBrazeStartActivity : BaseActivity<DBrazeMainViewModel,ActivityDbrazeStartBinding>() {

    override val viewModel: DBrazeMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_dbraze_start
    }

    override fun getViewModelClass(): Class<DBrazeMainViewModel> {
        return DBrazeMainViewModel::class.java
    }

    override fun inflateBinding(): ActivityDbrazeStartBinding {
        return ActivityDbrazeStartBinding.inflate(layoutInflater)
    }


}