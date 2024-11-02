package com.example.ducksample.fragments.cart

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ducksample.R
import com.example.ducksample.ScreenRouter
import com.example.ducksample.databinding.FragmentCartBinding
import com.example.ducksample.fragments.cart.adapter.CartAdapter
import com.example.ducksample.utils.YPayUtils
import com.yandex.pay.PaymentSession
import com.yandex.pay.YPayLauncher
import com.yandex.pay.YPayResult
import kotlinx.coroutines.launch

class CartFragment: Fragment() {

    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    private lateinit var viewBinding: FragmentCartBinding
    private lateinit var paymentSession: PaymentSession

    // YPayLauncher is an object, which actually starts the payment process
    // and receives the payment result. Please note that the real app should
    // rather rely on its backend to check the actual order status:
    private val yPayLauncher = YPayLauncher(this) { result: YPayResult ->
        viewModel.handlePayResult(result)
    }

    // implementing fragment interface:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = TransitionInflater.from(context).inflateTransition(R.transition.slide)
        // here we pre-initialize the payment session before the user actually
        // clicks the checkout button to let the Pay SDK pre-initialize:
        paymentSession = YPayUtils.getYPaySession(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val adapter = CartAdapter().apply {
             cartAdapter = this
        }
        val binding = FragmentCartBinding.inflate(inflater, container, false).apply {
            // set up cart recycler:
            cartRecycler.adapter = adapter
            cartRecycler.isNestedScrollingEnabled = false
            cartRecycler.layoutManager = LinearLayoutManager(context)
            cartRecycler.itemAnimator = null
            // lateinit the binding:
            viewBinding = this
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
    }

    // private helpers:

    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect(::renderViewModelState)
            }
        }
    }

    private fun renderViewModelState(state: CartViewModel.State) {
        // bind the items:
        cartAdapter.submitList(state.duckItems)
        viewBinding.cartEmpty.isVisible = state.duckItems.isEmpty()
        // bind the total price:
        viewBinding.cartTotalSum.setText(YPayUtils.formatPrice(requireContext(), state.totalSum))
        viewBinding.cartInfoWidget.setSum(state.totalSum.toBigDecimal())
        // bind the payment status:
        renderPayStatus(state.payStatus)
    }

    private fun renderPayStatus(status: CartViewModel.PayStatus) {
        when (status) {
            CartViewModel.PayStatus.NoItems -> setCloseAction(R.string.button_close)
            CartViewModel.PayStatus.Cancelled -> setCloseAction(R.string.button_res_cancel)
            CartViewModel.PayStatus.Failure -> setCloseAction(R.string.button_res_failure)
            CartViewModel.PayStatus.Success -> setCloseAction(R.string.button_res_success)
            CartViewModel.PayStatus.Ready -> setCheckoutAction()
            CartViewModel.PayStatus.WaitData -> setProgressAction(R.string.button_wait_url)
            CartViewModel.PayStatus.WaitResult -> setProgressAction(R.string.button_wait_res)
        }
    }

    private fun setCloseAction(@StringRes titleRes: Int) {
        viewBinding.cartButtonProgress.isVisible = false
        viewBinding.cartButtonTitle.setText(titleRes)
        viewBinding.cartButton.setOnClickListener { viewModel.handleBack(activity as ScreenRouter) }
    }

    private fun setCheckoutAction() {
        viewBinding.cartButtonProgress.isVisible = false
        viewBinding.cartButtonTitle.setText(R.string.button_checkout)
        viewBinding.cartButton.setOnClickListener { viewModel.handleCheckout(yPayLauncher, paymentSession) }
    }

    private fun setProgressAction(@StringRes titleRes: Int) {
        viewBinding.cartButtonProgress.isVisible = true
        viewBinding.cartButtonTitle.setText(titleRes)
        viewBinding.cartButton.setOnClickListener { }
    }
}
