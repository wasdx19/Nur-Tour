package com.example.nurtour.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteDTO(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val placeId: Int = 0,
    val public: Boolean = false,
    val imageArray: List<String> = emptyList(),
    val userName: String = ""
): Parcelable