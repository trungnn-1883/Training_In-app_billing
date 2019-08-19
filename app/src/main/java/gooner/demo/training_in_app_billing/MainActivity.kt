package gooner.demo.training_in_app_billing

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.SkuType.INAPP

class MainActivity : AppCompatActivity(), PurchasesUpdatedListener {

    lateinit var mGetBtn: Button
    lateinit var mSkuList: RecyclerView
    lateinit var mSkuAdapter: SkuAdapter
    lateinit var mListSkuDetail: List<SkuDetails>
    var mListSku =
        listOf(
            "object_1",
            "object_2",
            "object_3",
            "object_4",
            "new_1",
            "reward_1",
            "android.test.item_unavailable",
            "android.test.item_unavailable",
            "android.test.canceled",
            "android.test.purchase",
            "android.test.reward"
        )
    lateinit var mBillingClient: BillingClient
    lateinit var mSkuParam: SkuDetailsParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mGetBtn = findViewById(R.id.main_get_btn)
        mSkuList = findViewById(R.id.main_sku_rv)

        mSkuAdapter = SkuAdapter(mutableListOf()) {}
        mSkuParam = SkuDetailsParams.newBuilder()
            // list to query
            .setSkusList(mListSku)
            .setType(BillingClient.SkuType.INAPP).build()

        var divider: DividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager(this).orientation)

        mSkuList.addItemDecoration(divider)

        setUpBillingClient()

        mGetBtn.setOnClickListener {
            onLoadProductClicked()
        }

    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Toast.makeText(
                    this@MainActivity,
                    "ITEM_ALREADY_OWNED",
                    Toast.LENGTH_SHORT
                ).show()

                Log.d("MainAB", "ITEM_ALREADY_OWNED")


                val queryPurchases: Purchase.PurchasesResult = mBillingClient.queryPurchases(INAPP)
                if (queryPurchases.responseCode == BillingClient.BillingResponseCode.OK) {
                    val purchaseList: List<Purchase> = queryPurchases.purchasesList
                    purchaseList.forEach { item ->
                        val params =
                            ConsumeParams.newBuilder().setPurchaseToken(item.purchaseToken).build()
                        mBillingClient.consumeAsync(params) { billingResult, _ ->
                            when (billingResult.responseCode) {
                                BillingClient.BillingResponseCode.OK ->
                                    Toast.makeText(this@MainActivity, "Consumed ok", Toast.LENGTH_SHORT)
                                else -> Toast.makeText(
                                    this@MainActivity,
                                    "Consumed failed " + billingResult.responseCode,
                                    Toast.LENGTH_SHORT
                                )

                            }
                        }
                    }
                }

                val consumeParams =
                    ConsumeParams.newBuilder()
                        .setPurchaseToken(purchases!![0].purchaseToken)
                        .build()

                mBillingClient.consumeAsync(consumeParams, object : ConsumeResponseListener {

                    override fun onConsumeResponse(billingResult: BillingResult?, purchaseToken: String?) {
                        Toast.makeText(this@MainActivity, "onConsumeResponse", Toast.LENGTH_SHORT).show()
                    }
                })
            }


            BillingClient.BillingResponseCode.OK -> {

            }


            else -> {
                Log.d("MainAB", "Other: " + billingResult.responseCode)
            }

        }

    }

    private fun setUpBillingClient() {

        mBillingClient = BillingClient.newBuilder(this).setListener(this).enablePendingPurchases()
            .setUnderAgeOfConsent(BillingClient.UnderAgeOfConsent.NOT_UNDER_AGE_OF_CONSENT)
            .setChildDirected(BillingClient.ChildDirected.NOT_CHILD_DIRECTED).build()

        mBillingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                when {
                    billingResult.responseCode == BillingClient.BillingResponseCode.OK -> {
                        Toast.makeText(this@MainActivity, "Setup ok", Toast.LENGTH_SHORT).show()
                    }
                    else -> Toast.makeText(this@MainActivity, "Setup fail", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onBillingServiceDisconnected() {
                Toast.makeText(this@MainActivity, "Setup Disconnected", Toast.LENGTH_SHORT).show()

            }

        })
    }

    fun onLoadProductClicked() {
        when (mBillingClient.isReady) {
            true -> {
                Toast.makeText(this@MainActivity, "mBillingClient.isReady is true", Toast.LENGTH_SHORT).show()
                mBillingClient.querySkuDetailsAsync(mSkuParam) { billingResult, skuDetailsList ->
                    run {
                        when (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            true -> {
                                initProductAdapter(skuDetailsList)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Query ok: " + billingResult.responseCode,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            false -> Toast.makeText(
                                this@MainActivity,
                                "Query failed: " + billingResult.responseCode,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
            false -> Toast.makeText(
                this@MainActivity,
                "mBillingClient.isReady is false: ITEM_ALREADY_OWNED",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun initProductAdapter(skuDetailsList: List<SkuDetails>) {
        mSkuAdapter = SkuAdapter(skuDetailsList) {
            if (it.isRewarded) {
                val params = RewardLoadParams.Builder()
                    .setSkuDetails(it)
                    .build()

                mBillingClient.loadRewardedSku(
                    params, object : RewardResponseListener {
                        override fun onRewardResponse(billingResult: BillingResult) {
                            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "RewardResponseListener",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                    })
            }

            val billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(it).build()
            mBillingClient.launchBillingFlow(this, billingFlowParams)

        }

        mSkuList.adapter = mSkuAdapter
    }

}
