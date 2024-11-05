package com.brickcommander.napp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brickcommander.napp.logic.Calculate
import com.brickcommander.napp.model.Item
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


        fab.setOnClickListener {
            val intent = Intent(this@ShowItemsActivity, EditItemActivity::class.java)
            startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE)
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
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        // Swipe right to delete
                        items.removeAt(position)
                        calculate.deleteItem(position)
                        adapter.notifyItemRemoved(position)
                    }
                    ItemTouchHelper.LEFT -> {
                        // Swipe left to edit
                        val intent = Intent(this@ShowItemsActivity, EditItemActivity::class.java)
                        intent.putExtra("work_item", position) // Pass the item to the edit activity
                        adapter.notifyItemChanged(position) // Refresh item after swipe
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
            }
            adapter.notifyDataSetChanged()
        }
    }

}
