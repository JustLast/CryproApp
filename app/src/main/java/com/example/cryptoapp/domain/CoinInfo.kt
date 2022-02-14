package com.example.cryptoapp.domain

data class CoinInfo(
    var fromSymbol: String = "",
    var toSymbol: String? = null,
    var price: Double? = null,
    var lastUpdate: Long? = null,
    var highDay: Double? = null,
    var lowDay: Double? = null,
    var lastMarket: String? = null,
    var imageUrl: String? = null
)