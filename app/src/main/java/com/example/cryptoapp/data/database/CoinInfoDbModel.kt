package com.example.cryptoapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "full_price_list")
data class CoinInfoDbModel(
    @PrimaryKey
    var fromSymbol: String,
    var toSymbol: String?,
    var price: Double?,
    var lastUpdate: Long?,
    var highDay: Double?,
    var lowDay: Double?,
    var lastMarket: String?,
    var imageUrl: String
)