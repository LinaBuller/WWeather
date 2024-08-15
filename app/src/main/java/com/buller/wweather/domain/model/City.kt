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
    val id: Int = 0,
    @ColumnInfo(name = DatabaseConstants.CITY_NAME)
    val name: String,
    @ColumnInfo(name = DatabaseConstants.CITY_COUNTRY)
    val country: String,
    @ColumnInfo(name = DatabaseConstants.CITY_REGION)
    val region: String,
    @ColumnInfo(name = DatabaseConstants.CITY_IS_PIN)
    val isPin: Boolean = false,
    @ColumnInfo(name = DatabaseConstants.CITY_POSITION)
    val position: Int = 0

) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var currentTempC: String? = null
    @IgnoredOnParcel
    @Ignore
    var currentTempF: String? = null

    @IgnoredOnParcel
    @Ignore
    var timeZoneId: String? = null

    @IgnoredOnParcel
    @Ignore
    var currentTimestamp: Int = 0

    @IgnoredOnParcel
    @Ignore
    val weatherInfo: WeatherInfo?=null
    @IgnoredOnParcel
    @Ignore
    var condition: WeatherType? = null
}
