package com.brickcommander.napp.activity.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brickcommander.napp.R
import com.brickcommander.napp.data.Data
import com.brickcommander.napp.logic.Calculate
import com.brickcommander.napp.model.Profile
import com.brickcommander.napp.utils.Utility

class EditProfileActivity : AppCompatActivity() {
    companion object {
        const val TAG = "EditProfileActivity"
    }

    private lateinit var shopNameEditText: EditText
    private lateinit var ownerNameEditText: EditText
    private lateinit var gstEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var saveButton: Button

    private var profile: Profile = Data.profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_activity)

        Log.d(TAG, "onCreate called")

        shopNameEditText = findViewById(R.id.shopNameEditText)
        ownerNameEditText = findViewById(R.id.ownerNameEditText)
        gstEditText = findViewById(R.id.gstEditText)
        mobileEditText = findViewById(R.id.mobileEditText)
        emailEditText = findViewById(R.id.emailEditText)
        locationEditText = findViewById(R.id.locationEditText)
        saveButton = findViewById(R.id.saveButton)

        profile?.let {
            shopNameEditText.setText(it.getShopName())
            ownerNameEditText.setText(it.getOwner())
            mobileEditText.setText(it.getMobile())
            emailEditText.setText(it.getEmail())
            gstEditText.setText(it.getGstin())
            locationEditText.setText(it.getLocation())
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

    private fun revertChanges(oldProfile: Profile) {
        profile?.apply {
            setShopName(oldProfile.getShopName())
            setOwner(oldProfile.getOwner())
            setMobile(oldProfile.getMobile())
            setEmail(oldProfile.getEmail())
            setGstin(oldProfile.getGstin())
            setLocation(oldProfile.getLocation())
        }
    }

    private fun saveChanges() {
        // Update the workItem with new values
        val oldProfile: Profile? = profile?.copy()
        profile?.apply {
            setShopName(shopNameEditText.text.toString())
            setOwner(ownerNameEditText.text.toString())
            setMobile(mobileEditText.text.toString())
            setEmail(emailEditText.text.toString())
            setGstin(gstEditText.text.toString())
            setLocation(locationEditText.text.toString())
        }

        if(profile?.getShopName()?.isEmpty() == true) {
            Toast.makeText(applicationContext, "Shop Name cannot be empty", Toast.LENGTH_SHORT).show()
            if (oldProfile != null) {
                revertChanges(oldProfile)
            }
        } else {
            val calculate = Calculate()
            if(calculate.updateProfile(profile!!)) {
                Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
