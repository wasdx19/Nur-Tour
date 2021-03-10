package com.example.nurtour.ui.authorized.notes.dialog.newNote

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.nurtour.R
import com.example.nurtour.common.constants.ARGConstants.ARG_PLACE_DETAIL
import com.example.nurtour.data.model.PlaceDTO
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_new_note.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewNoteDialog : BottomSheetDialogFragment(){

    companion object {
        fun setData(args: Bundle) = NewNoteDialog()
            .apply {
                arguments = args
            }
    }

    private val placeData: PlaceDTO?
        get() = arguments?.getParcelable<PlaceDTO>(ARG_PLACE_DETAIL)

    private val viewModel by viewModel<NewNoteViewModel>()

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_new_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnclickListener()
        observeViewModel()
        setupView()
        viewModel.onViewCreated()
    }

    private fun setupView(){
        titleText.text = placeData?.name
    }

    private fun setOnclickListener(){
        saveButton.setOnClickListener {
            validate()
        }
        closeBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun observeViewModel() = with(viewModel){
        loading.observe(viewLifecycleOwner, Observer {
            progressLayout.isVisible = it
            saveButton.isClickable = !it
        })
        error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun validate(){
        if(noteEditText.text.toString().isNullOrEmpty())
            noteEditText.error = "Поле не должно быть пустым"
        else
            placeData?.id?.let {
                viewModel.addNoteUseCase(
                    placeData?.name.orEmpty(),
                    noteEditText.text.toString(),
                    it,
                    toggle.isChecked,
                    placeData?.images.orEmpty()
                )
            }
    }
}