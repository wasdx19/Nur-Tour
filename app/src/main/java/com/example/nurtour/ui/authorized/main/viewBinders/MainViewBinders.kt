package com.example.nurtour.ui.authorized.main.viewBinders

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drakeet.multitype.ItemViewBinder
import com.example.nurtour.R
import com.example.nurtour.common.constants.Contants.DURATION
import com.example.nurtour.common.constants.Contants.INIT_SCALE_XY
import com.example.nurtour.common.constants.Contants.SCALE_X
import com.example.nurtour.common.constants.Contants.SCALE_Y
import com.example.nurtour.data.model.PlaceDTO
import com.example.nurtour.data.model.PlaceTypeDTO
import com.example.nurtour.utils.extensions.generateActions
import kotlinx.android.synthetic.main.item_place_card.view.*
import kotlinx.android.synthetic.main.item_places_type.view.*

/**
 *  viewBinder для выбора типов мест по городу
 */
class PlacesTypeViewBinder(val listener: (Int) -> Unit) :
    ItemViewBinder<PlaceTypeDTO, PlacesTypeViewBinder.ViewHolder>() {

    var previousCheckedPosition = -1
    var mCheckedPosition = 0

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        val view = inflater.inflate(R.layout.item_places_type, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: PlaceTypeDTO) {
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: PlaceTypeDTO) = with(itemView) {
            val isChecked = layoutPosition == mCheckedPosition

            valueTextView.text = data.type

            if (isChecked) {
                previousCheckedPosition = layoutPosition
                setCheckedView(true)
            } else {
                setCheckedView(false)
            }

            valueTextView.setOnClickListener {
                mCheckedPosition = if (isChecked) 0 else layoutPosition
                adapter.notifyItemChanged(previousCheckedPosition)
                adapter.notifyItemChanged(layoutPosition)
                listener.invoke(data.id)
            }
        }

        private fun setCheckedView(checked: Boolean) = with(itemView) {
            valueTextView.isChecked = checked
            when (checked) {
                true -> valueTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
                else -> valueTextView.setTextColor(ContextCompat.getColor(context, R.color.blue))
            }
        }
    }
}


/**
 *  viewBinder для мест
 */
class PlaceViewBinder(val listener: (PlaceDTO) -> Unit) :
    ItemViewBinder<PlaceDTO, PlaceViewBinder.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, item: PlaceDTO) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
        ViewHolder(inflater.inflate(R.layout.item_place_card, parent, false))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var data: PlaceDTO

        fun bind(data: PlaceDTO) = with(itemView) {
            this@ViewHolder.data = data
            placeNameView.text = data.name
            Glide.with(context).load(data.images.firstOrNull()).into(imageView)

            cardView.setOnClickListener {
                listener.invoke(data)
            }

//            relLay.generateActions({ view: View, _: MotionEvent ->
//                animateToInitialPosition(view)
//            }, { view: View, _: MotionEvent ->
//                animateOnPress(view)
//            }, { view: View ->
//                animateOnCancel(view)
//            })
        }

        private fun animateToInitialPosition(view: View) {
            view.animate().scaleX(INIT_SCALE_XY)
                .scaleY(INIT_SCALE_XY)
                .setDuration(DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        listener.invoke(data)
                    }
                })
        }

        private fun animateOnPress(view: View) {
            view.animate().scaleX(SCALE_X)
                .scaleY(SCALE_Y).setDuration(DURATION)
        }

        private fun animateOnCancel(view: View) {
            view.animate().scaleX(INIT_SCALE_XY)
                .scaleY(INIT_SCALE_XY).setDuration(DURATION)
        }
    }
}
