package com.example.nurtour.ui.authorized.notes.dialog.note

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nurtour.R
import com.example.nurtour.common.constants.ARGConstants
import com.example.nurtour.data.model.NoteDTO
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_new_note.closeBtn
import kotlinx.android.synthetic.main.dialog_new_note.titleText
import kotlinx.android.synthetic.main.dialog_note.*

class NoteDialog : BottomSheetDialogFragment(){

    companion object {
        fun setData(args: Bundle) = NoteDialog()
            .apply {
                arguments = args
            }
    }

    private val noteData: NoteDTO?
        get() = arguments?.getParcelable<NoteDTO>(ARGConstants.ARG_PLACE_DETAIL)


    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnclickListener()
        setupView()
    }

    private fun setupView(){
        titleText.text = noteData?.title
        noteText.text = noteData?.description

        if(noteData?.public!!){
            publicText.text = "Да"
        }else{
            publicText.text = "Нет"
        }
    }

    private fun setOnclickListener(){
        closeBtn.setOnClickListener {
            dismiss()
        }
    }
}