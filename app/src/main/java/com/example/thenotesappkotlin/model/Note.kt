package com.example.thenotesappkotlin.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize // mechanism that converts complex data into simple object that can be easily transferred b/w activities and fragments
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val noteTitle: String,
    val noteDesc: String,
) : Parcelable