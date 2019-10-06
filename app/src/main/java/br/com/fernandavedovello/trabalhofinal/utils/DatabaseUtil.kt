package br.com.fernandavedovello.trabalhofinal.utils

import com.google.firebase.database.FirebaseDatabase

class DatabaseUtil {
    companion object {
        private val firebaseDatabase: FirebaseDatabase =
            FirebaseDatabase.getInstance()
        init {
            firebaseDatabase.setPersistenceEnabled(true)
        }
        fun getDatabase() : FirebaseDatabase {

            if (firebaseDatabase == null) {
                val firebaseDatabase = FirebaseDatabase.getInstance()
                firebaseDatabase.setPersistenceEnabled(true)
            }

            return firebaseDatabase
        }
    }
}