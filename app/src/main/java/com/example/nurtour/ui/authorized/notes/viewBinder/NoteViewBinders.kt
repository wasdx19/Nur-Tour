package com.example.nurtour.ui.authorized.notes.viewBinder

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.example.nurtour.R
import com.example.nurtour.common.constants.Contants
import com.example.nurtour.data.model.NoteDTO
import com.example.nurtour.utils.extensions.generateActions
import kotlinx.android.synthetic.main.item_note.view.*

/**
 *  ViewBinder для заметок
 */
class NoteViewBinder(val listener: (NoteDTO) -> Unit): ItemViewBinder<NoteDTO,NoteViewBinder.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, item: NoteDTO) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup)
            = ViewHolder(inflater.inflate(R.layout.item_note, parent, false))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        lateinit var data: NoteDTO

        fun bind(data: NoteDTO) = with(itemView){
            this@ViewHolder.data = data
            Glide.with(context).load(data.imageArray.firstOrNull()).into(imageView)
            titleView.text = data.title
            descView.text = data.description

            cardView.setOnClickListener {
                listener.invoke(data)
            }

//            cardView.generateActions({ view: View, _: MotionEvent ->
//                animateToInitialPosition(view)
//            }, { view: View, _: MotionEvent ->
//                animateOnPress(view)
//            }, { view: View ->
//                animateOnCancel(view)
//            })
        }

        private fun animateToInitialPosition(view: View) {
            view.animate().scaleX(Contants.INIT_SCALE_XY)
                .scaleY(Contants.INIT_SCALE_XY)
                .setDuration(Contants.DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        listener.invoke(data)
                    }
                })
        }

        private fun animateOnPress(view: View) {
            view.animate().scaleX(Contants.SCALE_X)
                .scaleY(Contants.SCALE_Y).setDuration(Contants.DURATION)
        }

        private fun animateOnCancel(view: View) {
            view.animate().scaleX(Contants.INIT_SCALE_XY)
                .scaleY(Contants.INIT_SCALE_XY).setDuration(Contants.DURATION)
        }
    }
}