package com.example.nurtour.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDTO(
    val id : String = "",
    val name: String = ""
): Parcelable