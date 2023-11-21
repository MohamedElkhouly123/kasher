package com.example.ecommerceapp.adapter

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.RentalPaidAdapter.RentalPaidViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.utils.Post

class RentalPaidAdapter(
    var BinList: List<Post>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<RentalPaidViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    var open = true
    var oldHght = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalPaidViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_rental_paid, null, false)
        return RentalPaidViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onBindViewHolder(holder: RentalPaidViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setSubCategoryRv(subMultiProductRv: RecyclerView?, child: List<Post>) {
        val subMultiProductAdapter = SubRentalPaidAdapter(child, generalDataSendedModel)
        subMultiProductRv!!.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        subMultiProductRv.adapter = subMultiProductAdapter
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun setAction(holder: RentalPaidViewHolder, position: Int) {
        holder.cardParent!!.setOnClickListener {
            if (holder.relativeRecycle!!.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(
                    holder.cardParent,
                    AutoTransition()
                )
                holder.relativeRecycle!!.visibility = View.VISIBLE
            } else {
                TransitionManager.beginDelayedTransition(
                    holder.cardParent,
                    AutoTransition()
                )
                holder.relativeRecycle!!.visibility = View.GONE
            }
        }
        holder.cardParent!!.setOnClickListener {
            if (holder.relativeRecycle!!.visibility == View.GONE) {
                holder.arrowDownImage!!.animate().rotation(180f).start()
            } else {
                holder.arrowDownImage!!.animate().rotation(360f).start()
            }
            val v =
                if (holder.relativeRecycle!!.visibility == View.GONE) View.VISIBLE else View.GONE
            holder.categoryRv!!.measure(
                View.MeasureSpec.makeMeasureSpec(
                    holder.categoryRv!!.height,
                    View.MeasureSpec.EXACTLY
                ), View.MeasureSpec.UNSPECIFIED
            )
            if (holder.relativeRecycle!!.visibility == View.GONE) {
                val anim = ValueAnimator.ofInt(
                    holder.cardParent!!.measuredHeight, holder.categoryRv!!.measuredHeight
                )
                anim.addUpdateListener { valueAnimator ->
                    holder.relativeRecycle!!.visibility = View.VISIBLE
                    val `val` = valueAnimator.animatedValue as Int
                    val layoutParams = holder.cardParent!!.layoutParams
                    layoutParams.height = `val`
                    holder.cardParent!!.layoutParams = layoutParams
                    Toast.makeText(
                        context,
                        "" + holder.categoryRv!!.measuredHeight,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                anim.duration = 800
                anim.start()
            } else {
                val anim = ValueAnimator.ofInt(
                    holder.cardParent!!.measuredHeight, 140
                )
                anim.addUpdateListener { valueAnimator ->
                    val `val` = valueAnimator.animatedValue as Int
                    val layoutParams = holder.cardParent!!.layoutParams
                    layoutParams.height = `val`
                    holder.cardParent!!.layoutParams = layoutParams
                    Handler().postDelayed({
                        holder.relativeRecycle!!.visibility = v
                        // addOrRemoveFavouriteItem.addOrRemoveFavouriteItem(false, favouriteList.get(position).getId());
                    }, 100)
                }
                anim.duration = 800
                anim.start()
            }
        }
    }

    private fun setData(holder: RentalPaidViewHolder, position: Int) {
        oldHght = holder.cardParent!!.height
        setSubCategoryRv(holder.categoryRv, BinList)
    }

    override fun getItemCount(): Int {
        return 6
    }

    inner class RentalPaidViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        var categoryName: TextView? = view.findViewById(R.id.category_name)
        var updateBtu: TextView? = view.findViewById(R.id.update_btu)
        var arrowDownImage: ImageView? = view.findViewById(R.id.arrow_down_image)
        var categoryRv: RecyclerView? = view.findViewById(R.id.category_rv)
        var linearParent: LinearLayout? = view.findViewById(R.id.linear_parent)
        var cardParent: CardView? = view.findViewById(R.id.card_parent)
        var relativeRecycle: RelativeLayout? = view.findViewById(R.id.relative_recycle)

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}