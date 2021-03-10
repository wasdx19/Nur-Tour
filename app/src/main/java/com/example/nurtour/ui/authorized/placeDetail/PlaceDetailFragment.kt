package com.example.nurtour.ui.authorized.placeDetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.drakeet.multitype.MultiTypeAdapter
import com.example.nurtour.R
import com.example.nurtour.common.constants.ARGConstants.ARG_PLACE_DETAIL
import com.example.nurtour.data.model.PlaceDTO
import com.example.nurtour.ui.authorized.notes.dialog.newNote.NewNoteDialog
import com.example.nurtour.ui.authorized.placeDetail.viewBinder.ImageViewBinder
import com.example.nurtour.ui.authorized.placeDetail.viewBinder.PublicNotesViewBinder
import com.example.nurtour.utils.decorator.HorizontalItemDecorator
import com.example.nurtour.utils.decorator.VerticalItemDecorator
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.fragment_place_detail.*
import kotlinx.android.synthetic.main.fragment_place_detail.progressLayout
import kotlinx.android.synthetic.main.fragment_place_detail.toolbarView
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceDetailFragment : Fragment(R.layout.fragment_place_detail) {

    private val viewModel by viewModel<PlaceDetailViewModel>()

    private val multiType = MultiTypeAdapter().apply {
        register(ImageViewBinder())
    }

    private val notesAdapter = MultiTypeAdapter().apply {
        register(PublicNotesViewBinder())
    }

    private val data: PlaceDTO?
        get() = activity?.intent?.extras?.getParcelable<PlaceDTO>(ARG_PLACE_DETAIL)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setupView()
        observeViewModel()
        data?.id?.let {
            viewModel.onViewCreated(it)
        }
    }

    private fun setOnClickListeners() {
        toolbarView.setNavigationOnClickListener {
            activity?.finish()
        }
        addToFavs.setOnClickListener {
            favText.text = "В избранных"
            data?.let { it1 -> viewModel.addUserFavorite(it1) }
        }
        addNotes.setOnClickListener {
            NewNoteDialog.setData(
                bundleOf(ARG_PLACE_DETAIL to data)
            ).show(childFragmentManager, "")
        }
    }

    private fun setupView() {
        toolbarView.title = data?.name
        activity?.let { Glide.with(it).load(data?.images?.firstOrNull()).into(imageView) }
        description.text = data?.description

        publicNotesRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = notesAdapter
        }

        imageRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = multiType
        }

        val list = arrayListOf<Any>()
        data?.images?.forEach {
            list.add(it)
        }

        imageRecycler.isVisible = !list.isNullOrEmpty()
        imagesLabel.isVisible = !list.isNullOrEmpty()
        multiType.apply {
            items = list
            notifyDataSetChanged()
        }
    }

    private fun observeViewModel() = with(viewModel) {
        error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        loading.observe(viewLifecycleOwner, Observer {
            progressLayout.isVisible = it
        })

        notes.observe(viewLifecycleOwner, Observer {
            publicNotesRecycler.isVisible = !it.isNullOrEmpty()
            notesLabel.isVisible = !it.isNullOrEmpty()
            notesLine.isVisible = !it.isNullOrEmpty()
            notesAdapter.apply {
                items = it
                notifyDataSetChanged()
            }
        })

        isFavorite.observe(viewLifecycleOwner, Observer {
            val isFavorite = it
            if (isFavorite) {
                favText.text = "В избранных"
                addToFavs.isClickable = false
            }
        })
    }
}