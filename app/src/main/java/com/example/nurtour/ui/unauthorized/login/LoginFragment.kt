package com.example.nurtour.ui.unauthorized.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.nurtour.R
import com.example.nurtour.ui.authorized.AuthorizedActivity
import kotlinx.android.synthetic.main.fragment_auth_login.*
import kotlinx.android.synthetic.main.fragment_auth_login.emailEditText
import kotlinx.android.synthetic.main.fragment_auth_login.passwordEditText
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_auth_login){

    private val viewModel by viewModel<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnclickListeners()
        observeViewModel()
    }

    private fun setOnclickListeners(){
        registryTextView.setOnClickListener {
            findNavController().navigate(R.id.action_to_registry_fragment)
        }
        loginButton.setOnClickListener {
            fieldValidation()
        }
    }

    private fun observeViewModel()=with(viewModel){
        authorized.observe(viewLifecycleOwner, Observer {
            val intent = Intent(activity, AuthorizedActivity::class.java)
            startActivity(intent)
            activity?.finish()
        })
        error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        loading.observe(viewLifecycleOwner, Observer {
            mainContent.isClickable = !it
            registryTextView.isClickable = !it
            progressLayout.isVisible = it
        })

    }

    private fun fieldValidation() {
        var isChecked = false

        when{
            emailEditText.text.toString().isNullOrEmpty() -> {
                isChecked = false
                emailEditText.error = "Поле не должно быть пустым"
            }
            passwordEditText.text.toString().isNullOrEmpty() -> {
                isChecked = false
                passwordEditText.error = "Поле не должно быть пустым"
            }
            else -> {
                isChecked = true
            }
        }

        if(isChecked)
            viewModel.makeAuthorization(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
    }

}