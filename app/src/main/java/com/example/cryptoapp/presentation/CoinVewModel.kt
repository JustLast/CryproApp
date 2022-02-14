package com.example.cryptoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinVewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)

    val priceList = db.coinPriceInfoDao().getPriceList()

    private val compositeDisposable = CompositeDisposable()

    init {
        loadData()
    }

    private fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 50)
//            .observeOn(AndroidSchedulers.mainThread())
            .map { it.coinNameContainerDtos?.map { it1 -> it1.coinNameDto?.name }?.joinToString(",") + "" }
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            .map { getPriceListFromRawData(it) }
            .delaySubscription(10, TimeUnit.SECONDS)
            .repeat()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)
                Log.d("TEST_OF_LOADING_DATA", "Success: $it")
            }, {
                it.message?.let { it1 -> Log.d("TEST_OF_LOADING_DATA", "Failure : $it1") }
            })
        compositeDisposable.add(disposable)
    }

    fun getDetailInfo(fSym: String): LiveData<CoinInfoDto> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    private fun getPriceListFromRawData(
        coinInfoJsonContainerDto: CoinInfoJsonContainerDto
    ): List<CoinInfoDto> {
        val result = ArrayList<CoinInfoDto>()

        val jsonObject = coinInfoJsonContainerDto.json ?: return result

        val coinKeySet = jsonObject.keySet()

        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()

            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(currencyJson.getAsJsonObject(currencyKey), CoinInfoDto::class.java)
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}