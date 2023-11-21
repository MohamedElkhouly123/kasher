package com.example.ecommerceapp.view.fragment.subPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.BinAdapter
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentBinBinding
import com.example.ecommerceapp.databinding.FragmentClassifyProductBinding
import com.example.ecommerceapp.utils.interfaces.MakeChangeInFragment
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AllProductForRom


class BinFragment : BaseFragment(), TryAgainOncall ,MakeChangeInFragment{
    private lateinit var binding: FragmentBinBinding
    private var root: View?=null
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private var bundle: Bundle?=null
    var binAdapter: BinAdapter? = null
    var navController: NavController? = null
    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1
    private var generalDataSendedModel: GeneralDataSendedModel?=null


//    @BindView(R.id.Fragment_bin_cards_rv)
    var FragmentBinCardsRv: RecyclerView? = null


    private var gLayout: GridLayoutManager? = null
    var binList = ArrayList<AllProductForRom>()
    private var dataBase: DataBaseKotlin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBinBinding.inflate(inflater, container, false)
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
        dataBase = DataBaseKotlin.getInstance(requireContext())
        FragmentBinCardsRv=binding.FragmentBinCardsRv
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.navController=navController
//        generalDataSendedModel!!.fragment_sr_refresh=binding.Fragment_category_sr_refresh
//        generalDataSendedModel!!.load_more=load_more
        generalDataSendedModel?.context=requireContext()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.makeChangeInFragment=this
        generalDataSendedModel!!.maxPage=maxPage
        generalDataSendedModel!!.no_products= binding.noProductsInBin.noProducts

        onClick(root)
        homeCycleActivity?.hidenav()
        binList= dataBase!!.addNewOrderItemDao()!!.allItems as ArrayList<AllProductForRom>
        if(binList.size==0){
            binding.noProductsInBin.noProducts?.visibility=View.VISIBLE
        }else{
            binding.noProductsInBin.noProducts?.visibility=View.GONE
        }
//        addList()
    }

    private fun initBinRecycle() {
        gLayout = GridLayoutManager(getContext(), 1)
        FragmentBinCardsRv!!.layoutManager = gLayout
        binAdapter = BinAdapter(binList, generalDataSendedModel!!)
        FragmentBinCardsRv!!.adapter = binAdapter
    }

    fun onClick(root: View) {
        bundle = Bundle()
        generalBundelDataToSend = GeneralBundelDataToSend()
        generalBundelDataToSend?.from="bin"
        bundle?.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
        binding.backBtn2.setOnClickListener {
            super.onBack()
        }
        binding.noProductsInBin.noProductsBrowseProductsBtn.setOnClickListener {

            navController?.navigate(R.id.subCategoryFragment,bundle)
        }
        binding.fragmentBinCompleteBtu.setOnClickListener {
            navController!!.navigate(R.id.completeOrderFragment)
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
    }

    override fun doChanges(generalDataSendedModel: GeneralDataSendedModel) {
        binding.fragmentBinTotalPriceTv.text="200"
        binding.fragmentBinTotalShippingTv.text="200"
        binding.fragmentBinTotalSellingTv.text="200"

    }
}