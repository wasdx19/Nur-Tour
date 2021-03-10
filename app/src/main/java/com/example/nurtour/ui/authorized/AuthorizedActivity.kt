package com.example.nurtour.ui.authorized

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nurtour.R
import com.example.nurtour.common.constants.ARGConstants.ARG_NAME_AUTH
import com.yandex.mapkit.MapKitFactory
import kotlinx.android.synthetic.main.activity_authorized.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthorizedActivity: AppCompatActivity() {

    private val viewModel by viewModel<AuthorizedViewModel>()

    private val userName: String?
        get() = intent.extras?.getString(ARG_NAME_AUTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorized)

        val navController=findNavController(R.id.authorizedNav)
        bottomNavigationView.setupWithNavController(navController)

        viewModel.saveUser(userName)
    }
}