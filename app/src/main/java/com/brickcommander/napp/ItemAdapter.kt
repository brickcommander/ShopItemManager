package com.brickcommander.napp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.napp.model.Item
import java.util.Locale

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

    fun capitalizeWords(text: String): String {
        return text.split(" ").joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()) else it.toString() } }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = capitalizeWords(item.getName())
        holder.buyingPrice.text = item.getBuyingPrice().toString()
        holder.sellingPrice.text = item.getSellingPrice().toString()
        holder.totalCount.text = item.getTotalCount().toString()
        holder.remainingCount.text = item.getRemainingCount().toString()
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
