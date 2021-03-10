package com.example.nurtour.ui.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.nurtour.R
import com.example.nurtour.ui.authorized.AuthorizedActivity
import com.example.nurtour.ui.unauthorized.AuthActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LauncherActivity : AppCompatActivity(R.layout.activity_launcher){

    private val viewModel by viewModel<LauncherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()

        Handler().postDelayed({
            viewModel.onCreate()
        },300)
    }

    private fun observeViewModel() = with(viewModel){
        userExist.observe(this@LauncherActivity, Observer {
            val intent = Intent(this@LauncherActivity, AuthorizedActivity::class.java)
            startActivity(intent)
            this@LauncherActivity.finish()
        })
        userNotExist.observe(this@LauncherActivity, Observer {
            val intent = Intent(this@LauncherActivity, AuthActivity::class.java)
            startActivity(intent)
            this@LauncherActivity.finish()
        })
    }
}