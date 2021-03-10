package com.example.nurtour.ui.unauthorized.registry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.nurtour.R
import com.example.nurtour.common.constants.ARGConstants.ARG_NAME_AUTH
import com.example.nurtour.ui.authorized.AuthorizedActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_auth_registry.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistryFragment : Fragment(R.layout.fragment_auth_registry) {

    private val viewModel by viewModel<RegistryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnclickListeners()
        observeViewModel()
    }

    private fun setOnclickListeners() {
        loginTextView.setOnClickListener {
            findNavController().popBackStack()
        }
        registerButton.setOnClickListener {
            fieldValidation()
        }
    }

    private fun observeViewModel() = with(viewModel) {
        registred.observe(viewLifecycleOwner, Observer {
            val intent = Intent(activity, AuthorizedActivity::class.java)
            intent.putExtra(ARG_NAME_AUTH, it)
            startActivity(intent)
            activity?.finish()
        })
        error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        loading.observe(viewLifecycleOwner, Observer {
            mainContent.isClickable = !it
            loginTextView.isClickable = !it
            progressLayout.isVisible = it
        })
    }


    private fun fieldValidation() {
        var isChecked = false

        when{
            nameEditText.text.toString().isNullOrEmpty() -> {
                isChecked = false
                nameEditText.error = "Поле не должно быть пустым"
            }
            emailEditText.text.toString().isNullOrEmpty() -> {
                isChecked = false
                emailEditText.error = "Поле не должно быть пустым"
            }
            passwordEditText.text.toString().isNullOrEmpty() -> {
                isChecked = false
                passwordEditText.error = "Поле не должно быть пустым"
            }
            confirmPassEditText.text.isNullOrEmpty()-> {
                isChecked = false
                confirmPassEditText.error = "Поле не должно быть пустым"
            }
            passwordEditText.text.toString() != confirmPassEditText.text.toString() -> {
                isChecked = false
                Toast.makeText(context, "Пароли не совпадают!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                isChecked = true
            }
        }

        if(isChecked)
            viewModel.makeRegistration(
                emailEditText.text.toString(),
                passwordEditText.text.toString(),
                nameEditText.text.toString()
            )
    }
}