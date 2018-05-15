package com.shakenbeer.animations.zubko

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.shakenbeer.animations.R

import java.util.ArrayList

class PagerAdapter : RecyclerView.Adapter<PagerAdapter.PagerHolder>() {

    private var items: List<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerHolder {
        val container = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
        return PagerHolder(container)
    }

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PagerHolder, position: Int) {
        holder.image.setImageResource(items[position].imageRes)
    }

    fun getItem(adapterPos: Int): Item {
        return items[adapterPos]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var image: SquareImageView = itemView.findViewById(R.id.image)
    }
}
