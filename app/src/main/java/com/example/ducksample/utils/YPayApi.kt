package com.example.ducksample.utils

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object YPayApi {

    private val okHttpClient = OkHttpClient.Builder().build()
    private val json = Json { ignoreUnknownKeys = true }

    @Serializable
    private data class RequestDto(
        @SerialName("orderId") val orderId: String,
        @SerialName("currencyCode") val currencyCode: String,
        @SerialName("cart") val cart: CartDto,
        @SerialName("availablePaymentMethods") val paymentMethod: List<String>,
        @SerialName("redirectUrls") val redirectUrls: RedirectUrlsDto,
    )

    @Serializable
    private data class CartDto(
        @SerialName("total") val total: TotalDto,
        @SerialName("items") val items: List<CartItemDto>,
    )

    @Serializable
    private data class TotalDto(
        @SerialName("amount") val amount: String
    )

    @Serializable
    private data class CartItemDto(
        @SerialName("productId") val id: String,
        @SerialName("total") val total: String,
        @SerialName("quantity") val quantity: QuantityDto,
    )

    @Serializable
    private data class QuantityDto(
        @SerialName("count") val count: String,
    )

    @Serializable
    data class RedirectUrlsDto(
        @SerialName("onSuccess") val onSuccess: String,
        @SerialName("onError") val onError: String,
    )

    @Serializable
    data class ResponseDto(
        @SerialName("data") val data: PaymentUrlDto
    )

    @Serializable
    data class PaymentUrlDto(
        @SerialName("paymentUrl") val paymentUrl: String
    )

    suspend fun getPaymentUrl(duckItems: List<DuckItem>, orderId: String, merchUrl: String, merchKey: String): Result<String> = runCatching {
        val requestDto = createRequestDto(duckItems, orderId, merchUrl)
        val requestBody = json.encodeToString(json.serializersModule.serializer(), requestDto).toRequestBody()

        val request = Request.Builder()
            .url("https://sandbox.pay.yandex.ru/api/merchant/v1/orders")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Api-Key $merchKey")
            .post(requestBody)
            .build()

        val response: Response = suspendCancellableCoroutine { continuation ->
            val call = okHttpClient.newCall(request)
            call.enqueue(object : Callback {
                // resume in case of the call failure:
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
                // resume in case of the call success:
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }
            })
            continuation.invokeOnCancellation {
                // silently cancel the call:
                try { call.cancel() } catch (ignored: Throwable) { }
            }
        }

        val responseBody = checkNotNull(response.body)
        val responseDto: ResponseDto = json.decodeFromString(responseBody.string())

        // finally, our payment url:
        responseDto.data.paymentUrl
    }

    // serialization helpers:

    private fun createRequestDto(duckItems: List<DuckItem>, orderId: String, merchUrl: String) = RequestDto(
        orderId = orderId,
        currencyCode = "RUB",
        cart = createCartDto(duckItems),
        paymentMethod = listOf("CARD", "SPLIT"),
        redirectUrls = createRedirectUrlDto(merchUrl)
    )

    private fun createCartDto(duckItems: List<DuckItem>) = CartDto(
        total = createTotalDto(duckItems),
        items = duckItems.map { createCartItemDto(it) }
    )

    private fun createTotalDto(duckItems: List<DuckItem>) = TotalDto(
        amount = duckItems.sumOf { it.price }.toString()
    )

    private fun createCartItemDto(item: DuckItem) = CartItemDto(
        id = item.id,
        total = item.price.toString(),
        quantity = QuantityDto("1")
    )

    private fun createRedirectUrlDto(merchUrl: String) = RedirectUrlsDto(
        onSuccess = merchUrl,
        onError = merchUrl
    )
}