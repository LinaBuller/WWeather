package com.buller.wweather.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.buller.wweather.domain.model.City

@Database(entities = [City::class], version = DatabaseConstants.DATABASE_VERSION)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getDao(): WeatherDao

    companion object {
        fun newInstance(context: Context): LocalDatabase {
            return Room.databaseBuilder(
                context = context,
                LocalDatabase::class.java,
                DatabaseConstants.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .setJournalMode(JournalMode.TRUNCATE)
                .build()
        }
    }
}