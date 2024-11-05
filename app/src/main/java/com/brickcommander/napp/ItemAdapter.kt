package com.brickcommander.napp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.napp.model.Item

class ItemAdapter(private val items: MutableList<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.nameId)
        val buyingPrice: TextView = itemView.findViewById(R.id.buyingPriceId)
        val sellingPrice: TextView = itemView.findViewById(R.id.sellingPriceId)
        val totalCount: TextView = itemView.findViewById(R.id.totalCountId)
        val remainingCount: TextView = itemView.findViewById(R.id.remainingCountId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = "Name: " + item.getName()
        holder.buyingPrice.text = "Buy: " + item.getBuyingPrice().toString()
        holder.sellingPrice.text = "Sell: " + item.getSellingPrice().toString()
        holder.totalCount.text = "Total: " + item.getTotalCount().toString()
        holder.remainingCount.text = "Remaining: " + item.getRemainingCount()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: Item) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    private fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

}
