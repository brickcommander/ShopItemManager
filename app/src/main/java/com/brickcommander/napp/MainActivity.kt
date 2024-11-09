package com.brickcommander.napp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import com.brickcommander.napp.model.Item

class MainActivity : AppCompatActivity() {
    private var items = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // Get the existing ActionBar
        supportActionBar?.apply {
            title = "Shop Manager" // Set custom title
        }

        // Get the ListView
        val listView: ListView = findViewById(R.id.list_of_items)

        items.add(Item())
        items.add(Item())
        items.add(Item())

        val adapter = ListItemAdapter(this, items.toList())

        // Set the adapter to the ListView
        listView.adapter = adapter
    }

    // Inflate custom menu in ActionBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Handle ActionBar button clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                Toast.makeText(this, "Add clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_sortBy -> {
                Toast.makeText(this, "Sort By clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
