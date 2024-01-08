package com.shv.meetingreminder2.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query(
        "SELECT * " +
                "FROM reminders " +
                "ORDER BY isReminderDone " +
                "AND dateTime"
    )
    fun getRemindersList(): Flow<List<ReminderDbModel>>

    @Query("SELECT * FROM reminders WHERE id=:reminderId LIMIT 1")
    suspend fun getReminder(reminderId: Int): ReminderDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReminder(reminderDto: ReminderDbModel)

    @Query("DELETE FROM reminders WHERE id=:reminderId")
    suspend fun deleteReminder(reminderId: Int)

    @Query(
        "SELECT * " +
                "FROM reminders " +
                "WHERE isReminderDone = 0 " +
                "AND dateTime > :currentTime"
    )
    fun getActiveAlarms(currentTime: Long): List<ReminderDbModel>
}