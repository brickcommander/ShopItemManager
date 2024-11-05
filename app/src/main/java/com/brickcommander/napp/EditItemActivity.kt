package com.brickcommander.napp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brickcommander.napp.data.Data
import com.brickcommander.napp.logic.Calculate
import com.brickcommander.napp.model.Item
import com.brickcommander.napp.utils.Utility

class EditItemActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var buyEditText: EditText
    private lateinit var sellEditText: EditText
    private lateinit var totalEditText: EditText
    private lateinit var remainingEditText: EditText
    private lateinit var saveButton: Button
    private var item: Item? = null
    private var itemPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_item_activity)

        Log.d("EditItemActivity", "onCreate called")

        nameEditText = findViewById(R.id.nameEditText)
        buyEditText = findViewById(R.id.buyEditText)
        sellEditText = findViewById(R.id.sellEditText)
        totalEditText = findViewById(R.id.totalEditText)
        remainingEditText = findViewById(R.id.remainingEditText)
        saveButton = findViewById(R.id.saveButton)

        // Retrieve the work item from the intent
        itemPosition = intent.getIntExtra("work_item", -1)
        if(itemPosition == -1) {
            item = Item()
        } else if(itemPosition >= Data.itemList.size) {
            finish()
        } else {
            item = Data.itemList[itemPosition]

            // Populate the fields with existing data
            item?.let {
                nameEditText.setText(it.getName())
                buyEditText.setText(it.getBuyingPrice().toString())
                sellEditText.setText(it.getSellingPrice().toString())
                totalEditText.setText(it.getTotalCount().toString())
                remainingEditText.setText(it.getRemainingCount().toString())
            }
        }


        // Save the changes when the button is clicked
        saveButton.setOnClickListener {
            if(Utility.isInternetAvailable(this)) {
                saveChanges()
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close the activity after saving
            } else {
                Toast.makeText(applicationContext, "Please Connect to Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun revertChanges(oldItem: Item) {
        item?.apply {
            setName(oldItem.getName())
            setBuyingPrice(oldItem.getBuyingPrice())
            setSellingPrice(oldItem.getSellingPrice())
            setTotalCount(oldItem.getTotalCount())
            setRemainingCount(oldItem.getRemainingCount())
        }
    }

    private fun saveChanges() {
        // Update the workItem with new values
        val oldItem: Item? = item?.copy()
        item?.apply {
            setName(nameEditText.text.toString())
            setBuyingPrice(buyEditText.text.toString().toIntOrNull() ?: 0)
            setSellingPrice(sellEditText.text.toString().toIntOrNull() ?: 0)
            setTotalCount(totalEditText.text.toString().toIntOrNull() ?: 0)
            setRemainingCount(remainingEditText.text.toString().toIntOrNull() ?: 0)
        }

        if(item?.getName()?.isEmpty() == true) {
            Toast.makeText(applicationContext, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            if (oldItem != null) {
                revertChanges(oldItem)
            }
        } else {
            val calculate = Calculate()
            if(calculate.updateItem(item!!, itemPosition)) {
                Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Item Already Exists", Toast.LENGTH_SHORT).show()
                if (oldItem != null) {
                    revertChanges(oldItem)
                }
            }
        }
    }
}
