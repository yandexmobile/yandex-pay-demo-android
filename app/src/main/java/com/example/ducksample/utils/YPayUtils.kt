package com.example.ducksample.utils

import android.content.Context
import com.example.ducksample.R
import com.yandex.pay.MerchantData
import com.yandex.pay.MerchantId
import com.yandex.pay.MerchantName
import com.yandex.pay.MerchantUrl
import com.yandex.pay.PaymentSession
import com.yandex.pay.PaymentSessionKey
import com.yandex.pay.YPay
import com.yandex.pay.YPayApiEnvironment
import com.yandex.pay.YPayConfig
import com.yandex.pay.inventory.api.YPayInventory
import com.yandex.pay.inventory.api.YPayMerchantId
import com.yandex.pay.inventory.api.YPayNetworkEnvironment
import com.yandex.pay.widgets.badge.api.badges
import com.yandex.pay.widgets.badge.api.model.config.HidingPolicy
import com.yandex.pay.widgets.info.api.widgets
import java.util.UUID

object YPayUtils {

    // This function initializes the YPay inventory. It must be called
    // before you actually start using widgets and badges in your UI:
    fun initInventory(context: Context) {
        YPayInventory.init {
            // common inventory params:
            appContext  = context.applicationContext
            merchantId  = YPayMerchantId(YPAY_MERCHANT_ID)
            environment = YPayNetworkEnvironment.SANDBOX
        }.let {
            // setting up the widgets:
            it.widgets { }
            // setting up the badges:
            it.badges { hidingPolicy = HidingPolicy.INVISIBLE }
        }
    }

    // This function de-initializes the YPay inventory. Call it when the
    // widgets and the badges are no longer needed:
    fun clearInventory() {
        YPayInventory.clear().let {
            // clearing up the widgets:
            it.widgets()
            // clearing up the badges:
            it.badges()
        }
    }

    // Payment session is an identifier, used to track the payment attempt.
    // It is recommended to create the session before the user interacts
    // with the checkout button:
    fun getYPaySession(context: Context): PaymentSession {
        val merchantData = MerchantData(
            id = MerchantId(YPAY_MERCHANT_ID),
            name = MerchantName(YPAY_MERCHANT_NAME),
            url = MerchantUrl(YPAY_MERCHANT_URL)
        )
        val ypayConfig = YPayConfig(
            merchantData = merchantData,
            environment = YPayApiEnvironment.SANDBOX
        )
        val sessionKey = PaymentSessionKey(
            value = "DuckSample:SessionId:${UUID.randomUUID()}"
        )
        return YPay.getYandexPaymentSession(context, ypayConfig, sessionKey)
    }

    // In the real app, this function should be using your backend's api to
    // create the order and get the corresponding payment URL. This sample,
    // however, will imitate the backend by accessing the Pay API directly:
    suspend fun getPaymentUrl(duckItems: List<DuckItem>): Result<String> {
        val orderId = "DuckSample:OrderId:${UUID.randomUUID()}"
        return YPayApi.getPaymentUrl(duckItems, orderId, YPAY_MERCHANT_URL, YPAY_MERCHANT_API_KEY)
    }

    fun formatPrice(context: Context, price: Long): String {
        return context.resources.getString(R.string.price_format, price)
    }

    // private helpers:

    private const val YPAY_MERCHANT_NAME = "DuckSample"
    private const val YPAY_MERCHANT_URL = "https://pay.yandex.ru"
    private const val YPAY_MERCHANT_ID = "a5f49c84-0baa-41e1-814f-6f99746a6987"
    private const val YPAY_MERCHANT_API_KEY = "a5f49c84-0baa-41e1-814f-6f99746a6987"
}
