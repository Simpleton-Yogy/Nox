package com.b1u3_22.nox.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.b1u3_22.nox.db.settings.setting
import com.b1u3_22.nox.db.settings.settingsDAO
import com.b1u3_22.nox.db.status.status
import com.b1u3_22.nox.db.status.statusDAO

@Database(entities = [status::class, setting::class], version = 2)
abstract class internalDB : RoomDatabase() {
    abstract fun statusDAO(): statusDAO
    abstract fun settingsDAO(): settingsDAO

    companion object {
        @Volatile
        private var databaseInstance: internalDB? = null

        fun getDatabaseInstance(mContext: Context): internalDB = databaseInstance ?: synchronized(this){
            databaseInstance ?: buildDatabaseInstance(mContext).also {
                databaseInstance = it
            }
        }

        private fun buildDatabaseInstance(mContext: Context) = Room.databaseBuilder(mContext, internalDB::class.java, "internalDB.db").allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }
}