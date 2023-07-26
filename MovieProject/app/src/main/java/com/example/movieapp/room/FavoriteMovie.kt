package com.example.movieapp.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_data_table")
data class FavoriteMovie(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Int?,
    @ColumnInfo(name = "movie_title")
    val title: String?,
    @ColumnInfo(name = "movie_poster_path")
    val poster_path: String?,
    @ColumnInfo(name = "movie_overview")
    val overview: String?,
    @ColumnInfo(name = "movie_release_date")
    val release_date: String?,
    @ColumnInfo(name = "movie_vote_average")
    val vote_average: Double?,
    @ColumnInfo(name = "movie_original_title")
    val original_title: String?,
    @ColumnInfo(name = "movie_adult")
    val adult: Boolean?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(poster_path)
        parcel.writeString(overview)
        parcel.writeString(release_date)
        parcel.writeValue(vote_average)
        parcel.writeString(original_title)
        parcel.writeValue(adult)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteMovie> {
        override fun createFromParcel(parcel: Parcel): FavoriteMovie {
            return FavoriteMovie(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteMovie?> {
            return arrayOfNulls(size)
        }
    }
}
