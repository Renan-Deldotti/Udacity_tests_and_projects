/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao {

    // Insert a row in the table
    @Insert
    suspend fun insert(night: SleepNight)

    // Update a row in the table - based on the id
    @Update
    suspend fun update(night: SleepNight)

    // Return everything from a row with the provided id
    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId = :key")
    suspend fun get(key: Long): SleepNight?

    // Delete only the row specified
    @Delete
    suspend fun deleteOneNight(night: SleepNight)

    // Will delete all nights passed and return the total of deleted lines
    @Delete
    suspend fun deleteSomeNights(nights: List<SleepNight>): Int

    // Clear the table - delete all rows without deleting the table
    @Query("DELETE FROM daily_sleep_quality_table")
    suspend fun clear()

    // Return all the rows in the table ordered by id
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    // Returns the most recent night
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?
}
