package com.app.dbraze.presentation.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.app.dbraze.R
import com.app.dbraze.base.BaseActivity
import com.app.dbraze.databinding.ActivityDbrazeSplashBinding
import com.app.dbraze.presentation.viewmodel.DBrazeMainViewModel
import com.app.dbraze.presentation.viewmodel.DBrazeSplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class DBrazeSplashActivity: BaseActivity<DBrazeSplashViewModel, ActivityDbrazeSplashBinding>() {


    override val viewModel: DBrazeSplashViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun observeViewModel() {
        super.observeViewModel()
        viewModel.navigateToMainActivity.observe(this) {
            it?.let {
                if (it) {
                    navigateToMainActivity()
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_dbraze_splash
    }

    override fun getViewModelClass(): Class<DBrazeSplashViewModel> {
        return DBrazeSplashViewModel::class.java
    }

    override fun inflateBinding(): ActivityDbrazeSplashBinding {
        return ActivityDbrazeSplashBinding.inflate(layoutInflater)
    }

    private fun navigateToMainActivity() {
//        startActivity(Intent(this, DBrazeStartActivity::class.java))
        startActivity(Intent(this, DBrazeDashboardActivity::class.java))
        finish()
    }

}