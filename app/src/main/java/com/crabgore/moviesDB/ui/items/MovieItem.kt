package com.crabgore.moviesDB.ui.items

import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.crabgore.moviesDB.Const.Addresses.Companion.IMAGES_API_HOST
import com.crabgore.moviesDB.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.squareup.picasso.Picasso

class MovieItem(
    val id: Int,
    private val title: String? = null,
    private val poster: String? = null,
    private val rating: Double? = null,
    private val adult: Boolean? = null
) : AbstractItem<MovieItem.ViewHolder>() {
    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.movie_item

    /** defines the layout which will be used for this item in the list */
    override val layoutRes: Int
        get() = R.layout.movie_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<MovieItem>(view) {
        private val poster: ImageView = view.findViewById(R.id.poster)
        private val title: TextView = view.findViewById(R.id.movie_title)
        private val rating: TextView = view.findViewById(R.id.rating)
        private val adult: CardView = view.findViewById(R.id.plus_18_mark)

        override fun bindView(item: MovieItem, payloads: List<Any>) {
            item.poster?.let {
                Picasso.get().load(IMAGES_API_HOST + it).fit().centerCrop().into(poster)
            }
            title.text = item.title
            rating.text = item.rating.toString()
            item.adult?.let {
                if (it) adult.visibility = VISIBLE
            }
        }

        override fun unbindView(item: MovieItem) {
            title.text = null
            poster.setImageResource(R.drawable.ic_no_image)
        }
    }
}