package com.brickcommander.napp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.brickcommander.napp.data.CONSTANTS
import com.brickcommander.napp.model.Item
import java.util.Locale

class ListItemAdapter(
    private val context: MainActivity,
    private val items: List<Item>
) : ArrayAdapter<Item>(context, R.layout.item_layout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)

        // Get the current item
        val item = getItem(position)

        val name: TextView = itemView.findViewById(R.id.nameId)
        val buyingPrice: TextView = itemView.findViewById(R.id.buyingPriceId)
        val sellingPrice: TextView = itemView.findViewById(R.id.sellingPriceId)
        val totalCount: TextView = itemView.findViewById(R.id.totalCountId)
        val remainingCount: TextView = itemView.findViewById(R.id.remainingCountId)

        if(item != null) {
            name.text = capitalizeWords(item.getName())
            buyingPrice.text = item.getBuyingPrice().toString() + " Rs"
            sellingPrice.text = item.getSellingPrice().toString() + " Rs"
            totalCount.text = item.getTotalCount().toString() + " " + CONSTANTS.QUANTITY[item.getTotalQ()]
            remainingCount.text = item.getRemainingCount().toString() + " " + CONSTANTS.QUANTITY[item.getRemainingQ()]
        }

        return itemView
    }

    fun capitalizeWords(text: String): String {
        return text.split(" ").joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()) else it.toString() } }
    }
}