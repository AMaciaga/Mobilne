package com.example.gallery

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
class MyPicture(
    var title: String,
    var icon: Int?,
    var direct: String?,
    var rating: Float,
    var description: String
) : Parcelable