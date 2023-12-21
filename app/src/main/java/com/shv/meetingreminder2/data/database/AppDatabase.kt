package com.shv.meetingreminder2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ReminderDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    companion object {

        private const val DB_NAME = "reminders.db"
        private val LOCK = Any()
        private var db: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                db?.let {
                    return it
                }
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() //TODO don't forget delete this
                    .build()

                db = instance
                return instance
            }
        }
    }
}