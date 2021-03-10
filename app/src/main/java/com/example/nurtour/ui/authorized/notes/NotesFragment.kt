package com.example.nurtour.ui.authorized.notes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.example.nurtour.R
import com.example.nurtour.common.constants.ARGConstants
import com.example.nurtour.data.model.NoteDTO
import com.example.nurtour.ui.authorized.notes.dialog.newNote.NewNoteDialog
import com.example.nurtour.ui.authorized.notes.dialog.note.NoteDialog
import com.example.nurtour.ui.authorized.notes.viewBinder.NoteViewBinder
import com.example.nurtour.utils.decorator.VerticalItemDecorator
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.fragment_notes.progressLayout
import kotlinx.android.synthetic.main.fragment_place_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotesFragment : Fragment(R.layout.fragment_notes) {

    private val viewModel by viewModel<NotesViewModel>()

    private val multiAdapter = MultiTypeAdapter().apply {
        register(NoteViewBinder {
            NoteDialog.setData(
                bundleOf(ARGConstants.ARG_PLACE_DETAIL to it)
            ).show(childFragmentManager, "")
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeViewModel()
        viewModel.onViewCreated()
    }

    private fun setupView() {
        notesRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = multiAdapter
            if (itemDecorationCount == 0)
                addItemDecoration(VerticalItemDecorator())
        }
    }

    private fun observeViewModel() = with(viewModel) {
        notes.observe(viewLifecycleOwner, Observer {
            emptyLayout.isVisible = it.isNullOrEmpty()
            multiAdapter.apply {
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