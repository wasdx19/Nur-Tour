package com.example.nurtour.data.model

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class PlaceTypeDTO(
    val id: Int = 0,
    val type: String = ""
) : Parcelable

@Parcelize
data class PlaceDTO(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val typeId: Int = 0,
    val images: List<String> = emptyList(),
    val cost: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Parcelable