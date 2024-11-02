package com.example.ducksample.fragments.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ducksample.ScreenRouter
import com.example.ducksample.utils.DuckCart
import com.example.ducksample.utils.DuckItem
import com.example.ducksample.utils.YPayUtils
import com.yandex.pay.PaymentData
import com.yandex.pay.PaymentSession
import com.yandex.pay.YPayContractParams
import com.yandex.pay.YPayLauncher
import com.yandex.pay.YPayResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel: ViewModel() {

    enum class PayStatus { NoItems, Ready, WaitData, WaitResult, Cancelled, Failure, Success }
    data class State (val duckItems: List<DuckItem>, val totalSum: Long, val payStatus: PayStatus)

    private val _stateFlow = MutableStateFlow(resolveInitialState())

    // observing the content:

    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    // initialization:

    private fun resolveInitialState(): State {
        val cartItems = DuckCart.stateFlow.value
        val itemsList = mutableListOf<DuckItem>()
        var totalSum  = 0L
        // generate items list:
        DuckItem.entries.forEach {
            if (cartItems.contains(it)) {
                itemsList += it
                totalSum  += it.price
            }
        }
        // generate pay status:
        val payStatus = if (itemsList.isEmpty()) PayStatus.NoItems else PayStatus.Ready
        return State(duckItems = itemsList, totalSum = totalSum, payStatus = payStatus)
    }

    // public interface:

    fun handleBack(router: ScreenRouter) {
        router.handleBack()
    }

    fun handleCheckout(launcher: YPayLauncher, session: PaymentSession) {
        // fix the items to checkout:
        val duckItems = stateFlow.value.duckItems

        // start the payment process:
        viewModelScope.launch(Dispatchers.IO) {
            // update the pay status:
            _stateFlow.update { it.copy(payStatus = PayStatus.WaitData) }
            // get the payment URL. Please note, that the real app should rather rely
            // on its backend to generate the order ID and the actual payment URL:
            val paymentUrl = YPayUtils.getPaymentUrl(duckItems).getOrThrow()
            val paymentData = PaymentData.PaymentUrlFlowData(paymentUrl)
            // update the pay status again:
            _stateFlow.update { it.copy(payStatus = PayStatus.WaitResult) }
            // actually start the payment. Its is recommended to create the
            // payment session beforehand to let the Pay SDK pre-initialize:
            launcher.launch(YPayContractParams(
                paymentSession = session,
                paymentData = paymentData
            ))
        }
    }

    // In the real app, the final source of the payment status should be your
    // app's backend. Here we imitate the backend polling by introducing delay:
    fun handlePayResult(payResult: YPayResult) {
        viewModelScope.launch {
            // "poll" your backend to obtain the correct order status:
            delay(1000L)
            // clear the cart if the payment completed successfully:
            if (payResult is YPayResult.Success) { DuckCart.clear() }
            // finally, update the pay status:
            _stateFlow.update { it.copy(payStatus = payResult.toPayStatus()) }
        }
    }

    // private helpers:

    private fun YPayResult.toPayStatus() = when (this) {
        is YPayResult.Cancelled -> PayStatus.Cancelled
        is YPayResult.Failure -> PayStatus.Failure
        is YPayResult.Success -> PayStatus.Success
    }
}
