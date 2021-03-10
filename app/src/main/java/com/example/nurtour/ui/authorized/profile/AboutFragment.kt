package com.example.nurtour.ui.authorized.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.nurtour.R
import com.example.nurtour.ui.unauthorized.AuthActivity
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_about.progressLayout
import kotlinx.android.synthetic.main.fragment_place_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : Fragment(R.layout.fragment_about){

    private val viewModel by viewModel<AboutViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnclickListener()
        observeViewModel()
        viewModel.onViewCreated()
    }

    private fun setOnclickListener(){
        logoutButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Вы точно хотите выйти?")
                .setPositiveButton("Да", DialogInterface.OnClickListener { dialog, which ->
                    viewModel.logout()
                    dialog.dismiss()
                    val intent = Intent(activity, AuthActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                })
                .setNegativeButton("Нет", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                }).show()

        }
    }

    private fun observeViewModel() = with(viewModel){
        name.observe(viewLifecycleOwner, Observer {name ->
            titleView.text = "Привет, ${name}!"
        })
        error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        loading.observe(viewLifecycleOwner, Observer {
            progressLayout.isVisible = it
        })
    }
}