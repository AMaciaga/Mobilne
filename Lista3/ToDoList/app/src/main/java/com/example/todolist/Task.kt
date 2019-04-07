package com.example.todolist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
class Task(
    var title: String,
    var icon: Int,
    var date: LocalDateTime,
    var priority: String
) : Parcelable