package com.example.ecommerceapp.view.fragment.subPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.OrdersAdapter
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentOrders2Binding
import com.example.ecommerceapp.databinding.FragmentRentalPaidBinding
import com.example.ecommerceapp.utils.Post
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.fragment.BaseFragment


class OrdersFragment : BaseFragment(), TryAgainOncall {
    private lateinit var binding: FragmentOrders2Binding
    private var root: View?=null
    var ordersAdapter: OrdersAdapter? = null
    var navController: NavController? = null
    var ordersList: ArrayList<Post> = ArrayList<Post>()

//    @BindView(R.id.Fragment_orders_cards_rv)
    var FragmentOrdersCardsRv: RecyclerView? = null

    private var gLayout: GridLayoutManager? = null
    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1
    private var generalDataSendedModel: GeneralDataSendedModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrders2Binding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(requireActivity(), R.id.home_activity_fragment_normal)
        setUpActivity()
        initData(root!!)
        initOrdersRv()
    }

    private fun initOrdersRv() {
        gLayout = GridLayoutManager(getContext(), 1)
        FragmentOrdersCardsRv!!.layoutManager = gLayout
        ordersAdapter = OrdersAdapter(ordersList, generalDataSendedModel!!)
        FragmentOrdersCardsRv!!.adapter = ordersAdapter
    }

    private fun initData(root: View) {
        FragmentOrdersCardsRv=binding.FragmentOrdersCardsRv
        homeCycleActivity?.hidenav()
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.navController=navController
//        generalDataSendedModel!!.fragment_sr_refresh=binding.Fragment_category_sr_refresh
        generalDataSendedModel!!.load_more=binding.itemLoadMore.loadMore
        generalDataSendedModel?.context=requireContext()
        generalDataSendedModel!!.maxPage=maxPage
//        if(ordersList.size==0){
//            root?.no_products_img?.setImageResource(R.drawable.ic_orders_more_gray_icon)
//            root?.no_products_tv1?.text=getString(R.string.no_orders)
////            root?.no_products_tv2?.text=getString(R.string.add_to_favourite_first)
//            root?.no_products?.visibility=View.VISIBLE
//            root?.no_products_browse_products_btn?.visibility=View.INVISIBLE
//            root?.no_products_tv2?.visibility=View.INVISIBLE
//            //            root!!.no_products_browse_products_btn.setOnClickListener {
////                navController?.navigate(R.id.subCategoryFragment,bundle)
////            }
//        }else{
//            root?.no_products?.visibility=View.GONE
//        }
        onClick(root)
    }

    fun onClick(root: View) {
        binding.backBtn!!.setOnClickListener { super.onBack() }


    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        TODO("Not yet implemented")
    }
}