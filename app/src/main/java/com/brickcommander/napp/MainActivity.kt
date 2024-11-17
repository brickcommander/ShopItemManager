package com.brickcommander.napp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.brickcommander.napp.activity.edit.EditItemActivity
import com.brickcommander.napp.activity.list.ListFragment
import com.brickcommander.napp.model.Enums
import com.brickcommander.napp.model.Item

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private var items = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // Get the existing ActionBar
        supportActionBar?.apply {
            title = "Shop Manager" // Set custom title
        }

        val fragment = ListFragment.newInstance(Enums.ITEMS)

        // Check if the fragment is already added (useful for configuration changes)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        val intent = Intent(this, EditItemActivity::class.java)
        startActivity(intent)

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
