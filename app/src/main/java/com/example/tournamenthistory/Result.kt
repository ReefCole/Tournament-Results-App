package com.example.tournamenthistory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(val id: Int, val p1: String, val p2: String, val p1Score: Int, val p2Score: Int, val winner: Boolean): Parcelable {
}