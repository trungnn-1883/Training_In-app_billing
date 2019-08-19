package gooner.demo.training_in_app_billing

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.billingclient.api.SkuDetails

class SkuAdapter(var listSku: List<SkuDetails>, val onProductClicked: (SkuDetails) -> Unit) :
    RecyclerView.Adapter<SkuAdapter.SkuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sku, parent, false)

        return SkuViewHolder(view).apply {
            view.setOnClickListener {
                onProductClicked(listSku[adapterPosition])
            }

        }
    }

    fun updateList(listSku: List<SkuDetails>) {
        this.listSku = listSku
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listSku.size

    override fun onBindViewHolder(holder: SkuViewHolder, position: Int) {
        holder.mSkuTitleTv?.text = "Title: ${listSku[position].title} \nSku: ${listSku[position].sku}"
        holder.mSkuPriceTv?.text = "Description: ${listSku[position].description} \nPrice: ${listSku[position].price}"
    }

    inner class SkuViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var mSkuTitleTv: TextView? = null
        var mSkuPriceTv: TextView? = null
        var mRootView: ConstraintLayout? = null

        init {
            mRootView = view.findViewById(R.id.item_sku_root)
            mSkuTitleTv = view.findViewById(R.id.item_sku_title_txt)
            mSkuPriceTv = view.findViewById(R.id.item_sku_price_txt)
        }
    }

}