package com.example.nurtour.ui.authorized.placeDetail.viewBinder

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.example.nurtour.R
import com.example.nurtour.data.model.NoteDTO
import kotlinx.android.synthetic.main.item_card_image.view.*
import kotlinx.android.synthetic.main.item_place_notes.view.*

/**
 *  ViewBinder для картинок в детальной странице
 */
class ImageViewBinder : ItemViewBinder<String, ImageViewBinder.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, item: String) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) = ViewHolder(inflater.inflate(R.layout.item_card_image, parent, false))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(link: String) = with( itemView){
            Glide.with(context).load(link).into(imageView)
        }
    }
}

/**
 *  ViewBinder для публичных заметок по месту
 */
class PublicNotesViewBinder: ItemViewBinder<NoteDTO, PublicNotesViewBinder.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, item: NoteDTO) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = ViewHolder(inflater.inflate(R.layout.item_place_notes, parent, false))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(item: NoteDTO) = with(itemView){
            name.text = item.userName
            note.text = item.description

//            if(note.lineCount > note.maxLines) {
//                more.isVisible = false
//                note.ellipsize = null
//            }else{
//                more.isVisible = false
//                note.ellipsize = TextUtils.TruncateAt.END
//            }

            note.setOnClickListener {
                if(note.maxLines == 3 && note.lineCount > 3) {
                    note.maxLines = note.lineCount
                    more.isVisible = false
                    note.ellipsize = null
                    note.lineCount
                    adapter.notifyItemChanged(layoutPosition)
                }else if(note.maxLines > 3){
                    note.maxLines = 3
                    more.isVisible = true
                    note.ellipsize = TextUtils.TruncateAt.END
                    adapter.notifyItemChanged(layoutPosition)
                }
            }

//            note.setOnClickListener {
//                if(note.maxLines == note.lineCount && note.lineCount > 3) {
//                    note.maxLines = 3
//                    more.isVisible = true
//                    note.ellipsize = TextUtils.TruncateAt.END
//                }
//            }
        }
    }
}