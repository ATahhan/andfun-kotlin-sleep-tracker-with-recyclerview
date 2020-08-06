package com.example.android.trackmysleepquality.database

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

enum class ListingType {
    GRID, LIST;
}

class UserPrefs (context: Context) {
    val PREFS_FILENAME = "com.example.android.trackmysleepquality.database.userprefs"
    val LISTING_TYPE = "listing_type"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    val listingTypeLiveData: LiveData<ListingType>
        get() = Transformations.map(prefs.stringLiveData(LISTING_TYPE, ListingType.GRID.name)) {
                ListingType.valueOf(it)
            }

    var listingType: ListingType
        get() = ListingType.valueOf(prefs.getString(LISTING_TYPE, ListingType.GRID.name))
        set(value) = prefs.edit().putString(LISTING_TYPE, value.name).apply()
}