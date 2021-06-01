package com.crabgore.moviesDB.ui.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.loadImageWithPlaceHolder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

class PeopleItem(
    val id: Int,
    private val name: String? = null,
    private val profile: String? = null
) : AbstractItem<PeopleItem.ViewHolder>() {
    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.people_item

    /** defines the layout which will be used for this item in the list */
    override val layoutRes: Int
        get() = R.layout.people_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<PeopleItem>(view) {
        private val profile: ImageView = view.findViewById(R.id.profile)
        private val name: TextView = view.findViewById(R.id.name)

        override fun bindView(item: PeopleItem, payloads: List<Any>) {
            item.profile?.let {
                loadImageWithPlaceHolder(it, profile)
            }
            name.text = item.name
        }

        override fun unbindView(item: PeopleItem) {
            name.text = null
            profile.setImageResource(R.drawable.ic_no_photo_night)
        }
    }
}