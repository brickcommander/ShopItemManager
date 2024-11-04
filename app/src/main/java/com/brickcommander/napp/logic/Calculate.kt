package com.brickcommander.napp.logic

import com.brickcommander.napp.db.GitHubJsonHandler
import android.util.Log
import com.brickcommander.napp.Data
import com.brickcommander.napp.model.Item

class Calculate() {

    private val handler = GitHubJsonHandler()

    companion object {
        const val TAG = "Calculate"
    }

    private fun sortList(list: MutableList<Item>) {
        list.sortByDescending { it.getId() }
    }

    fun getItemList(): MutableList<Item> {
        Log.d(TAG, "getItemList: ${Data.itemList.size}")
        return handler.getItemList()
    }

    fun updateItem(newItem: Item, itemPosition: Int) {
        Log.d(TAG, "updateItem: $newItem : $itemPosition")
        if (itemPosition == -1) {
            Data.itemList.add(newItem)
        } else {
            Data.itemList[itemPosition] = newItem
        }

        Thread {
            val newList = Data.itemList
            sortList(newList)
            handler.updateItemList(Data.itemList)
        }.start()
    }

    fun deleteItem(itemPosition: Int) {
        Log.d(TAG, "deleteItem : $itemPosition")
        Data.itemList.removeAt(itemPosition)

        Thread {
            handler.updateItemList(Data.itemList)
        }.start()
    }
}