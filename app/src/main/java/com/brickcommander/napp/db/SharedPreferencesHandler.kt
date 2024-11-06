//package com.brickcommander.napp.db
//
//import android.content.Context
//import com.brickcommander.napp.data.CONSTANTS
//
//class SharedPreferencesHandler {
//    companion object {
//        fun getUsername(context: Context): String {
//            val sharedPreferences = context.getSharedPreferences(CONSTANTS.sharedPerferenceName, Context.MODE_PRIVATE)
//            return sharedPreferences.getString(CONSTANTS.userNameString, "Default").toString()
//        }
//
//        fun update(username: String, context: Context) {
//            val sharedPreferences = context.getSharedPreferences(CONSTANTS.sharedPerferenceName, Context.MODE_PRIVATE)
//
//            val editor = sharedPreferences.edit()
//            editor.putString(CONSTANTS.userNameString, username)
//            editor.apply()
//        }
//
//        fun clear(context: Context) {
//            val sharedPreferences = context.getSharedPreferences(CONSTANTS.sharedPerferenceName, Context.MODE_PRIVATE)
//
//            val editor = sharedPreferences.edit()
//            editor.clear()
//            editor.commit()
//        }
//    }
//
//}