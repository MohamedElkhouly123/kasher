package com.example.ecommerceapp.view.fragment.subPages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.SubCategoriesHzAdapter
import com.example.ecommerceapp.adapter.SearchAdapter
import com.example.ecommerceapp.adapter.SubCategoryAdapter
import com.example.ecommerceapp.data.model.BasicItem
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.SearchesListForRoom
import com.example.ecommerceapp.databinding.FragmentSubCategoryBinding
import com.example.ecommerceapp.utils.Dialogs.ShowBottomSheetRecycleList
import com.example.ecommerceapp.utils.interfaces.ProductAdaptersActions
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AllProductForRom

import java.util.concurrent.Executors

class SubCategoryFragment : BaseFragment() , ProductAdaptersActions, TryAgainOncall {
    private var FragmentSubCategoryCardsRv: RecyclerView?=null
    private lateinit var binding: FragmentSubCategoryBinding
    private var recyclerViewHzSubCatsLayoutManager: LinearLayoutManager?=null
    private var subCategoriesHzAdapter: SubCategoriesHzAdapter?=null
    private lateinit var bundle: Bundle
    private lateinit var searchText: String
    private var textChanged: Boolean=false
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private var root: View?=null

    var subCategoryAdapter: SubCategoryAdapter? = null
    private var gLayout: GridLayoutManager? = null
    var subCategoryProductsList = ArrayList<AllProductForRom>()
    var subCategoryList = ArrayList<BasicItem>()
    var navController: NavController? = null
    private var gLayout2: GridLayoutManager? = null
    var searchProductList = mutableListOf<SearchesListForRoom>()
    var searchesList = mutableListOf<SearchesListForRoom>()
    var searchesListString = mutableListOf<String>()
    var searchesListForRoom = SearchesListForRoom()
    var searchAdapter: SearchAdapter? = null
    private var dataBase: DataBaseKotlin? = null
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
        if (this.arguments != null) {
            generalBundelDataToSend = this.requireArguments().getSerializable("generalBundelDataToSend") as GeneralBundelDataToSend?
        }
        // Inflate the layout for this fragment
        binding = FragmentSubCategoryBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        initData(root!!)
    }

    private fun productNotSearches() {
        binding.fragmentSubCategoryOldSearchKeysLy.visibility=View.GONE
        initSubCategoryRv()
    }

    private fun initSearchProductRv() {

        gLayout = GridLayoutManager(getContext(), 1)

        binding.FragmentSubCategoryCardsRv?.layoutManager = gLayout
        searchAdapter = SearchAdapter(
            searchProductList,generalDataSendedModel!!
        )
        binding.FragmentSubCategoryCardsRv?.adapter = searchAdapter

    }
    private fun initSubCategoryRSHz() {


        recyclerViewHzSubCatsLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.fragmentSubCategoryHzCardsRv.setLayoutManager(recyclerViewHzSubCatsLayoutManager)
        subCategoriesHzAdapter = SubCategoriesHzAdapter(
            subCategoryList,
            generalDataSendedModel!!
        )
        binding.fragmentSubCategoryHzCardsRv.setAdapter(subCategoriesHzAdapter)
//        if (courseItemList.size() == 0) {
////            showToast(getActivity(), " yes");
//            getgroupCourseHz(1);
//        }
    }
    private fun searchLocation(root: View) {
        searchText = binding.fragmentSubCategorySearchEt.text.toString()
        if (searchText.trim().length>0){
            textChanged=false
            searchesListForRoom.searchTextToSave=searchText
            Executors.newSingleThreadExecutor()
                .execute {
                    dataBase!!.addNewOrderItemDao()!!.insert(searchesListForRoom)
                }
            searchProductList.add(searchesListForRoom)
            searchesList.add(searchesListForRoom)
            productNotSearches()
        }
        binding.fragmentSubCategorySearchEt.setText(searchText)
    }

    private fun initData(root: View) {
        dataBase = DataBaseKotlin.getInstance(requireActivity())
        navController = Navigation.findNavController(root)
        homeCycleActivity?.hidenav()
        bundle = Bundle()
//        generalBundelDataToSend?.from="s"
//        bundle?.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.fragment_sr_refresh=binding.fragmentFaqSrRefresh
        generalDataSendedModel!!.load_more=binding.itemLoadMore.loadMore
        generalDataSendedModel!!.maxPage=maxPage
        generalDataSendedModel!!.productAdaptersActions=this
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel?.context=requireContext()
        generalDataSendedModel?.generalBundelDataToSend=generalBundelDataToSend
        subCategoryList.clear()
        subCategoryList.add(BasicItem(0,""))
        subCategoryList.add(BasicItem(0,"nokia"))
        subCategoryList.add(BasicItem(0,"hawawy"))
        subCategoryList.add(BasicItem(0,"opo"))
        subCategoryList.add(BasicItem(0,"nokia"))
        subCategoryList.add(BasicItem(0,"hawawy"))
        subCategoryList.add(BasicItem(0,"opo"))
        initSubCategoryRSHz()
        if("home".equals(generalBundelDataToSend?.from)){
            binding.fragmentSubCategoryOldSearchKeysLy.visibility=View.VISIBLE
            initSearchProductRv()
        }else {
            binding.fragmentSubCategoryHzCardsParentLy.visibility=View.VISIBLE
            binding.fragmentSubCategoryTitleTv.text="Devices"

            productNotSearches()
            if(subCategoryProductsList.size==0){
                binding.noProductsInBin.noProductsImg?.setImageResource(R.drawable.sad)
                binding.noProductsInBin.noProductsTv1?.text=getString(R.string.no_products)
//            root?.no_products_tv2?.text=getString(R.string.add_to_favourite_first)
                binding.noProductsInBin.noProducts?.visibility=View.VISIBLE
                binding.noProductsInBin.noProductsBrowseProductsBtn?.visibility=View.INVISIBLE
                binding.noProductsInBin.noProductsTv2?.visibility=View.INVISIBLE

                //            root!!.no_products_browse_products_btn.setOnClickListener {
//                navController?.navigate(R.id.subCategoryFragment,bundle)
//            }
            }else{
                binding.noProductsInBin.noProducts?.visibility=View.GONE
            }
        }

        FragmentSubCategoryCardsRv=binding.FragmentSubCategoryCardsRv
        searchProductList.clear()
        searchesList.clear()
        searchesList.addAll(dataBase!!.addNewOrderItemDao()!!.allSearchesItems as ArrayList<SearchesListForRoom>)
        searchProductList.addAll(searchesList)
        searchAdapter?.notifyDataSetChanged()
        for (i in 0 until searchesList.size) {
            searchesListString.add(searchesList[i].searchTextToSave!!)
        }

//        homeCycleActivity?.showenav()
        binding.fragmentSubCategorySearchEt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            Log.d("actionId", "OnKeyListener: " + keyCode + " - " + KeyEvent.KEYCODE_ENTER)
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                searchText = binding.fragmentSubCategorySearchEt.text.toString()
//                Handler().postDelayed({
                if (!searchesListString.contains(searchText)) {
                    searchesListString.add(searchText)
                    searchLocation(root!!)
                }
//                },300)
                return@OnKeyListener true
            }
            false
        })


        binding.fragmentSubCategorySearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

//                                             Your code .........
            }

            override fun afterTextChanged(text: Editable) {
                if (text.trim().length>1) {
                    textChanged=true
                    generalBundelDataToSend?.adapterType="server"
                    binding.noProductsInBin.noProducts?.visibility=View.GONE

                    // to get the result as list
                    searchProductList.clear()
                    searchesListForRoom.searchTextToSave= text.toString()
                    searchProductList.addAll(searchesList.filter { s -> s.searchTextToSave!!.contains(text.toString())})
//                    for (i in 0..list.size) {
//                        if (list.get(i).contains("")) {
//                        }
//                    }
                    // to get a string
//                    var selectedMonth: String = month.filter { s -> s == "January" }.single()
                    binding.fragmentSubCategoryOldSearchKeysLy.visibility=View.GONE
                    initSearchProductRv()
//                    searchLocation(root!!)
                }else{
                    if("home".equals(generalBundelDataToSend?.from)) {
                        generalBundelDataToSend?.adapterType = "local"
                        searchProductList.clear()
                        searchProductList.addAll(searchesList)
                        binding.fragmentSubCategoryOldSearchKeysLy.visibility = View.VISIBLE
                        initSearchProductRv()
                    }else{
                        if (text.trim().length==0) {
                            productNotSearches()
                        }
                    }
                }
//                getallProductsList(1)
//                                Toast.makeText(getContext(), "text changed"+s, Toast.LENGTH_SHORT).show();
            }
        })

        onClick(root)
    }

    private fun initSubCategoryRv() {
        gLayout = GridLayoutManager(getContext(), 2)
        FragmentSubCategoryCardsRv?.layoutManager = gLayout
        subCategoryAdapter =
            SubCategoryAdapter(subCategoryProductsList, generalDataSendedModel!!)
        FragmentSubCategoryCardsRv?.adapter = subCategoryAdapter
    }

    fun onClick(root: View) {
        binding.backBtn!!.setOnClickListener { super.onBack()}
        binding.fragmentSubCategoryFilterBtu!!.setOnClickListener { homeCycleActivity?.navController?.navigate(R.id.classifyProductFragment) }
        binding.fragmentSubCategoryHzCardsShowDialogAllBtn!!.setOnClickListener {
            ShowBottomSheetRecycleList(generalDataSendedModel).showBottomSheetDialog()
        }

        binding.fragmentSearchProductDeleteAllSearchKeysBtn!!.setOnClickListener {
            dataBase!!.addNewOrderItemDao()!!.deletAllSearches()
            searchProductList.clear()
            searchAdapter?.notifyDataSetChanged()
        }

    }

    override fun makeActionSearch(generalDataSendedModel: GeneralDataSendedModel) {
        productNotSearches()
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        TODO("Not yet implemented")
    }
}