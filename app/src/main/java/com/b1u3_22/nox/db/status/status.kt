package com.b1u3_22.nox.db.status

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = status.TABLE_NAME)
data class status(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    var statusID:Int ?= null,
    @ColumnInfo(name = STATUS)
    var status: Boolean? = true
)
{
    companion object {
        const val TABLE_NAME = "status"
        const val ID = "id"
        const val STATUS = "status"
    }
}