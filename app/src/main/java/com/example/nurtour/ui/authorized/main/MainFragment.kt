package com.example.nurtour.ui.authorized.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.example.nurtour.R
import com.example.nurtour.common.constants.ARGConstants.ARG_PLACE_DETAIL
import com.example.nurtour.ui.authorized.main.viewBinders.PlaceViewBinder
import com.example.nurtour.ui.authorized.main.viewBinders.PlacesTypeViewBinder
import com.example.nurtour.ui.authorized.placeDetail.PlaceDetailActivity
import com.example.nurtour.utils.decorator.HorizontalItemDecorator
import com.example.nurtour.utils.decorator.VerticalItemDecorator
import kotlinx.android.synthetic.main.fragment_auth_login.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.progressLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by viewModel<MainViewModel>()

    private val placeTypeAdapter = MultiTypeAdapter().apply {
        register(PlacesTypeViewBinder {
            viewModel.onTypeClicked(it)
        })
    }

    private val placeAdapter = MultiTypeAdapter().apply {
        register(PlaceViewBinder {
            val intent = Intent(activity, PlaceDetailActivity::class.java)
            intent.putExtra(ARG_PLACE_DETAIL, it)
            startActivity(intent)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupView()
        viewModel.onViewCreated()
    }

    private fun setupView() {
        placesTypeRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = placeTypeAdapter
            if (itemDecorationCount == 0)
                addItemDecoration(HorizontalItemDecorator())
        }

        placesRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = placeAdapter
            if (itemDecorationCount == 0)
                addItemDecoration(VerticalItemDecorator())
        }
    }

    private fun observeViewModel() = with(viewModel) {
        placeTypes.observe(viewLifecycleOwner, Observer {
            placeTypeAdapter.apply {
                items = it
                notifyDataSetChanged()
            }
        })

        place.observe(viewLifecycleOwner, Observer {
            emptyLayout.isVisible = it.isNullOrEmpty()
            placeAdapter.apply {
                items = it
                notifyDataSetChanged()
            }
        })

        error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        loading.observe(viewLifecycleOwner, Observer {
            progressLayout.isVisible = it
        })
    }
}
