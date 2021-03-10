package com.example.nurtour.ui.authorized.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.example.nurtour.R
import com.example.nurtour.common.constants.ARGConstants
import com.example.nurtour.ui.authorized.favorite.viewBinder.FavoriteViewBinder
import com.example.nurtour.ui.authorized.placeDetail.PlaceDetailActivity
import com.example.nurtour.utils.decorator.GridItemDecorator
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_favorites.progressLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(R.layout.fragment_favorites){

    private val viewModel by viewModel<FavoriteViewModel>()

    private val multiType = MultiTypeAdapter().apply {
        register(FavoriteViewBinder{
            val intent = Intent(activity, PlaceDetailActivity::class.java)
            intent.putExtra(ARGConstants.ARG_PLACE_DETAIL, it)
            startActivity(intent)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupView()
        viewModel.onViewCreated()
    }

    private fun setupView(){
        favoriteRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = multiType
            if(itemDecorationCount == 0)
                addItemDecoration(GridItemDecorator())
        }


    }

    private fun observeViewModel() = with(viewModel){
        favPlaces.observe(viewLifecycleOwner, Observer {
            emptyLayout.isVisible = it.isNullOrEmpty()
            multiType.apply {
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