package com.example.ecommerceapp.view.fragment.subPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.BinAdapter
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentOrders2Binding
import com.example.ecommerceapp.databinding.FragmentOrdersDetailsBinding
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.main.data.models.AllProductForRom


class OrderInfoFragment : BaseFragment(), TryAgainOncall {
    private lateinit var fragmentOrderInfoRv: RecyclerView
    private lateinit var binding: FragmentOrdersDetailsBinding
    private var root: View?=null
    var binAdapter: BinAdapter? = null
    var navController: NavController? = null
    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1
    private var generalDataSendedModel: GeneralDataSendedModel?=null

    private var gLayout: GridLayoutManager? = null
    var binList = ArrayList<AllProductForRom>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrdersDetailsBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        navController = Navigation.findNavController(root!!)
        initData(root!!)
        initBinRecycle()
    }

    private fun initData(root: View) {
        fragmentOrderInfoRv=binding.fragmentOrderInfoRv
        homeCycleActivity?.hidenav()
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.type="info"
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel!!.fragment_sr_refresh=binding.fragmentFaqSrRefresh
//        generalDataSendedModel!!.load_more=load_more
        generalDataSendedModel?.context=requireContext()
        generalDataSendedModel!!.maxPage=maxPage
        onClick()
        addList()
    }

    private fun initBinRecycle() {
        gLayout = GridLayoutManager(getContext(), 1)
        fragmentOrderInfoRv!!.layoutManager = gLayout
        binAdapter = BinAdapter(binList, generalDataSendedModel!!)
        fragmentOrderInfoRv!!.adapter = binAdapter
    }

    fun onClick() {
        binding.backBtn.setOnClickListener {
            super.onBack()
        }
    }

    private fun addList() {
        binList.add(AllProductForRom(
            "ali",
            "50",
            "cat",
            "1",
            "ddfdf",
            "very good place",
            "",
            "t",
            "f",
            "maincat",
            "elmasry",
            "0"
        ))
        binList.add(AllProductForRom(
            "shewips",
            "50",
            "cat",
            "1",
            "ddfdf",
            "very good place",
            "",
            "t",
            "f",
            "maincat",
            "elmasry",
            "0"
        ))
        binList.add(AllProductForRom(
            "shewips",
            "50",
            "cat",
            "1",
            "ddfdf",
            "very good place",
            "",
            "t",
            "f",
            "maincat",
            "elmasry",
            "0"
        ))
        binList.add(AllProductForRom(
            "shewips",
            "50",
            "cat",
            "1",
            "ddfdf",
            "very good place",
            "",
            "t",
            "f",
            "maincat",
            "elmasry",
            "0"
        ))
        binList.add(AllProductForRom(
            "shewips",
            "50",
            "cat",
            "1",
            "ddfdf",
            "very good place",
            "",
            "t",
            "f",
            "maincat",
            "elmasry",
            "0"
        ))
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        TODO("Not yet implemented")
    }
}