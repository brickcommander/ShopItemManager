package com.brickcommander.napp.logic

import com.brickcommander.napp.db.GitHubJsonHandler
import android.util.Log
import com.brickcommander.napp.data.Data
import com.brickcommander.napp.model.Item

class Calculate() {

    private val handler = GitHubJsonHandler()

    companion object {
        const val TAG = "Calculate"
    }

    private fun sortList(list: MutableList<Item>) {
        list.sortBy { it.getName() }
    }

    fun getItemList(): MutableList<Item> {
        Log.d(TAG, "getItemList: ${Data.itemList.size}")
        return handler.getItemList()
    }

    fun updateItem(newItem: Item, itemPosition: Int): Boolean {
        Log.d(TAG, "updateItem: $newItem : $itemPosition")
        if(newItem.getName().isEmpty()) return false
        if (itemPosition == -1) {
            for (item in Data.itemList) {
                if(item.getName().equals(newItem.getName(), ignoreCase = true)) return false
            }
            Data.itemList.add(newItem)
        } else {
            Data.itemList[itemPosition] = newItem
        }

        Thread {
            val newList = Data.itemList.toMutableList()
            sortList(newList)
            handler.updateItemList(newList)
        }.start()

        return true
    }

    fun deleteItem(itemPosition: Int) {
        Log.d(TAG, "deleteItem : $itemPosition")
        Data.itemList.removeAt(itemPosition)

        Thread {
            handler.updateItemList(Data.itemList)
        }.start()
    }
}
