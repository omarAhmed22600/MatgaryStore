package com.brandsin.store.realtimeDatabase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RealtimeDatabase {

    private val messageListRef: String = "Message"

    private var database: FirebaseDatabase? = null

    private fun getInstance(): FirebaseDatabase {
        if (database == null) {
            database = FirebaseDatabase.getInstance()
            // database!!.setPersistenceEnabled(false)
        }
        return database!!
    }

    fun getMessageListRef(): DatabaseReference {
        return getInstance().reference.child(messageListRef)
    }
}