package com.example.armazenamento

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Armazenamento(var arm_title: String, var arm_content: String, var storage_type: String):Parcelable