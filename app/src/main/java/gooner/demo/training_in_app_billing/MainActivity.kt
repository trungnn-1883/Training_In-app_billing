package gooner.demo.training_in_app_billing

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import com.android.billingclient.api.*

class MainActivity : AppCompatActivity(), PurchasesUpdatedListener {

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        Toast.makeText(this@MainActivity, "mBillingClient.isReady is false: " + billingResult.responseCode, Toast.LENGTH_SHORT).show()
    }


    lateinit var mGetBtn: Button
    lateinit var mSkuList: RecyclerView
    lateinit var mSkuAdapter: SkuAdapter
    lateinit var mListSkuDetail: List<SkuDetails>
    var mListSku = listOf("object_1", "object_2", "object_3", "object_4")
    lateinit var mBillingClient: BillingClient
    lateinit var mSkuParam: SkuDetailsParams


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mGetBtn = findViewById(R.id.main_get_btn)
        mSkuList = findViewById(R.id.main_sku_rv)

        mSkuAdapter = SkuAdapter(mutableListOf()) {}
        mSkuList.adapter = mSkuAdapter
        mSkuList.layoutManager = LinearLayoutManager(this)

        mSkuParam = SkuDetailsParams.newBuilder()
            // list to query
            .setSkusList(mListSku)
            .setType(BillingClient.SkuType.INAPP).build()

        setUpBillingClient()

        mGetBtn.setOnClickListener {
            onLoadProductClicked()
        }

    }

    private fun setUpBillingClient() {

        mBillingClient = BillingClient.newBuilder(this).setListener(this).build()

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
                            }
                            false -> Toast.makeText(this@MainActivity, "Query failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
            false -> Toast.makeText(this@MainActivity, "mBillingClient.isReady is false", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initProductAdapter(skuDetailsList: List<SkuDetails>) {
        mSkuAdapter = SkuAdapter(skuDetailsList) {
            val billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(it).build()
            mBillingClient.launchBillingFlow(this, billingFlowParams)
        }

        mSkuAdapter.notifyDataSetChanged()

    }

}
