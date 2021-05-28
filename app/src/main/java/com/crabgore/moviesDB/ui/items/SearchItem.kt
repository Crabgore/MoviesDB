package com.crabgore.moviesDB.ui.items

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.crabgore.moviesDB.Const.Addresses.Companion.IMAGES_API_HOST
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.formatDate
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.squareup.picasso.Picasso

class SearchItem(
    private val context: Context,
    val id: Int,
    private val image: String? = null,
    private val title: String? = null,
    private val year: String? = null,
    private val rating: Double? = null
) : AbstractItem<SearchItem.ViewHolder>() {
    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.search_item

    /** defines the layout which will be used for this item in the list */
    override val layoutRes: Int
        get() = R.layout.search_item_layout

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<SearchItem>(view) {
        private val image: ImageView = view.findViewById(R.id.search_image)
        private val title: TextView = view.findViewById(R.id.search_title)
        private val year: TextView = view.findViewById(R.id.search_year)
        private val rating: TextView = view.findViewById(R.id.search_rating)

        override fun bindView(item: SearchItem, payloads: List<Any>) {
            item.image?.let {
                Picasso.get().load(IMAGES_API_HOST + it).fit().centerCrop().into(image)
            }
            title.text = item.title
            item.year?.let {
                year.text = item.context.getString(R.string.year, formatDate(it))
            }
            rating.text = String.format("%.1f", item.rating)
        }

        override fun unbindView(item: SearchItem) {
            title.text = null
            image.setImageResource(R.drawable.ic_no_photo_night)
        }
    }
}