package com.shv.meetingreminder2.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders") //TODO add sort later
    fun getRemindersList(): LiveData<List<ReminderDbModel>>

    @Query("SELECT * FROM reminders WHERE id=:reminderId LIMIT 1")
    suspend fun getReminder(reminderId: Int): ReminderDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReminder(reminderDto: ReminderDbModel)

    @Query("DELETE FROM reminders WHERE id=:reminderId")
    suspend fun deleteReminder(reminderId: Int)
}