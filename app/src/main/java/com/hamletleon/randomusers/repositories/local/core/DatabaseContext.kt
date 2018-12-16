package com.hamletleon.randomusers.repositories.local.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hamletleon.randomusers.models.User
import com.hamletleon.randomusers.repositories.local.services.RandomUsersLocalService

@Database(entities = [ User::class ], version = 1)
abstract class DatabaseContext: RoomDatabase() {
    abstract fun getRandomUserLocalService() : RandomUsersLocalService

    companion object {
        private var INSTANCE: DatabaseContext? = null

        fun getInstance(context: Context): DatabaseContext =
            INSTANCE ?: synchronized(DatabaseContext::class) { INSTANCE ?: buildDatabase(context).also { INSTANCE = it } }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(context.applicationContext, DatabaseContext::class.java, "RandomUsers.Db")
            .fallbackToDestructiveMigration()
            .build()

        fun destroyInstance() {
            INSTANCE = null
        }

        fun clearAndResetAllTables(): Boolean {
            if (INSTANCE == null) return false

            // reset all auto-incrementalValues
            val query = SimpleSQLiteQuery("DELETE FROM sqlite_sequence")

            INSTANCE!!.beginTransaction()
            return try {
                INSTANCE!!.clearAllTables()
                INSTANCE!!.query(query)
                INSTANCE!!.setTransactionSuccessful()
                true
            } catch (e: Exception){
                false
            } finally {
                INSTANCE!!.endTransaction()
            }
        }
    }
}