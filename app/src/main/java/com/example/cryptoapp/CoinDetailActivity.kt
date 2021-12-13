package com.example.cryptoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_detail.*

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinVewModel

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(context: Context, fromSymbol: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)

        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }

        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL)

        if (fromSymbol == null) {
            finish()
            return
        }

        viewModel = ViewModelProvider(this)[CoinVewModel::class.java]
        viewModel.getDetailInfo(fromSymbol).observe(this, Observer {
            Picasso.get().load(it.getFullImageUrl()).into(ivLogoCoinDetail)

            tvFromSymbol.text = it.fromSymbol.toString()
            tvToSymbol.text = it.toSymbol.toString()

            tvDetailPrice.text = it.price.toString()
            tvMinPrice.text = it.lowDay.toString()
            tvMaxPrice.text = it.highDay.toString()
            tvLastDeal.text = it.lastMarket.toString()
            tvDetailLastUpdateTime.text = it.getFormattedTime()
        })
    }
}