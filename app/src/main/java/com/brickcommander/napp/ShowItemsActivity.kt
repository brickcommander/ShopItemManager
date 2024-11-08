package com.brickcommander.napp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.napp.data.Data
import com.brickcommander.napp.logic.Calculate
import com.brickcommander.napp.model.Item
import com.brickcommander.napp.utils.Utility
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowItemsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton
    private var items = mutableListOf<Item>()
    private val calculate: Calculate = Calculate()
    private val EDIT_ITEM_REQUEST_CODE: Int = 1
    private var itemPosition: Int = -1

    companion object {
        const val TAG = "ShowItemsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itemlist_activity)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.textView4)
        fab = findViewById(R.id.fab)

        adapter = ItemAdapter(items)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Set up swipe functionality
        setupSwipeActions()

        if(Utility.isInternetAvailable(this)) {
            lifecycleScope.launch(Dispatchers.IO) { // Use coroutines for background work
                progressBar.isVisible = true // Update UI on main thread
                Data.itemList = calculate.getItemList()
                Log.i(TAG, "itemList=${Data.itemList}")

                withContext(Dispatchers.Main) { // Switch to main thread for UI updates
                    progressBar.isVisible = false

                    if(Data.itemList.isNotEmpty()) {
                        Data.itemList.forEach { item ->
                            adapter.addItem(item)
                            recyclerView.scrollToPosition(items.size - 1)
                        }
                    } else {
                        textView.isVisible = true
                        textView.text = "No Items Available"
                    }
                }
            }
        } else {
            Toast.makeText(applicationContext, "Please Connect to Internet", Toast.LENGTH_SHORT).show()
        }

        fab.setOnClickListener {
            if(Utility.isInternetAvailable(this)) {
                val intent = Intent(this@ShowItemsActivity, EditItemActivity::class.java)
                startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE)
            } else {
                Toast.makeText(applicationContext, "Please Connect to Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSwipeActions() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false // Not used, as we only care about swipes

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                itemPosition = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        // Swipe right to delete
                        if(Utility.isInternetAvailable(this@ShowItemsActivity)) {
                            val itemName = items[itemPosition].getName()
                            items.removeAt(itemPosition)
                            calculate.deleteItem(itemPosition)
                            adapter.notifyItemRemoved(itemPosition)
                            Toast.makeText(applicationContext, itemName + " Deleted", Toast.LENGTH_SHORT).show()
                        } else {
                            adapter.notifyItemChanged(itemPosition) // Refresh item after swipe
                            Toast.makeText(applicationContext, "Please Connect to Internet", Toast.LENGTH_SHORT).show()
                        }
                    }
                    ItemTouchHelper.LEFT -> {
                        // Swipe left to edit
                        val intent = Intent(this@ShowItemsActivity, EditItemActivity::class.java)
                        intent.putExtra("work_item", itemPosition) // Pass the item to the edit activity
                        adapter.notifyItemChanged(itemPosition) // Refresh item after swipe
                        startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE)
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult : ${items.size} : ${Data.itemList.size} : ${Data.itemList.last()}")
            if(items.size < Data.itemList.size) {
                adapter.addItem(Data.itemList.last())
                recyclerView.scrollToPosition(items.size-1)
                itemPosition = items.size-1
            } else {
                adapter.notifyItemChanged(itemPosition)
            }
        }
    }

}
