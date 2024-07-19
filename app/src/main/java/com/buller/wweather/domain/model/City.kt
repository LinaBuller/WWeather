package com.buller.wweather.domain.model


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.buller.wweather.data.room.DatabaseConstants
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Entity(tableName = DatabaseConstants.CITY_TABLE_NAME)
@Parcelize

data class City(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DatabaseConstants.CITY_ID)
    val id: Long = 0,
    @ColumnInfo(name = DatabaseConstants.CITY_NAME)
    val name: String,
    @ColumnInfo(name = DatabaseConstants.CITY_IS_PIN)
    val isPin: Boolean = false,
    @ColumnInfo(name = DatabaseConstants.CITY_POSITION)
    val position: Int = 0

) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var currentTemp: String? = null
    @IgnoredOnParcel
    @Ignore
    val weatherInfo: WeatherInfo?=null
    @IgnoredOnParcel
    @Ignore
    val condition: WeatherType? = null
}
