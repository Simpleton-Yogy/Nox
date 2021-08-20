package com.b1u3_22.nox.db.status

import androidx.room.*
import com.b1u3_22.nox.db.status.status
import io.reactivex.Completable

@Dao
interface statusDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStatus(data:status)

    @Query("SELECT * FROM ${status.TABLE_NAME} WHERE id = 0")
    fun getStatus():status

    @Delete
    fun deleteStatus(status:status)

    @Update
    fun updateStatus(status:status)
}