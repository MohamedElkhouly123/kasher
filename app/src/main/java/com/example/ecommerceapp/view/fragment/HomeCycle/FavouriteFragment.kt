package com.example.ecommerceapp.view.fragment.HomeCycle

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.FavouriteAdapter
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentCategoryBinding
import com.example.ecommerceapp.databinding.FragmentFavouriteBinding
import com.example.ecommerceapp.utils.interfaces.MakeChangeInFragment
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.ViewModel.GeneralViewModelMethods
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.view.main.data.models.AllProductForRom


class FavouriteFragment : BaseFragment(), TryAgainOncall,MakeChangeInFragment {
    private lateinit var binding: FragmentFavouriteBinding
    private var counter: CountDownTimer?=null
    private lateinit var generalBundelDataToSend: GeneralBundelDataToSend
    private lateinit var bundle: Bundle
    private var root: View?=null
    var navController: NavController? = null

    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    var FragmentFavouriteCardsRv: RecyclerView? = null


    var favouriteAdapter: FavouriteAdapter? = null
    private var gLayout: GridLayoutManager? = null
    var favouriteList = ArrayList<AllProductForRom>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentFavouriteCardsRv=binding.FragmentFavouriteCardsRv
        setUpActivity()
        homeCycleActivity?.showenav()

//        homeCycleActivity?.showLoading()
        navController = Navigation.findNavController(root!!)
        addAndInitList()
        initFavouriteRv()
        counter = object : CountDownTimer(115, 50) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                setUpActivity()
                homeCycleActivity?.showenav()
            }
        }.start()
    }

    private fun initFavouriteRv() {
        gLayout = GridLayoutManager(getContext(), 1)
        FragmentFavouriteCardsRv!!.layoutManager = gLayout
        favouriteAdapter = FavouriteAdapter(
            favouriteList,generalDataSendedModel!!
        )
        FragmentFavouriteCardsRv!!.adapter = favouriteAdapter
    }

    private fun addAndInitList() {
        bundle = Bundle()
        generalBundelDataToSend = GeneralBundelDataToSend()
        generalBundelDataToSend?.from="favourite"
        bundle?.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.makeChangeInFragment=this
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel!!.fragment_sr_refresh=binding.fragmentFaqSrRefresh
        generalDataSendedModel!!.load_more=binding.itemLoadMore.loadMore
        generalDataSendedModel?.context=requireContext()
        generalDataSendedModel!!.maxPage=maxPage
        if(! homeCycleActivity!!.userDataAuth) {
            generalDataSendedModel!!.startLogin = true
            homeCycleActivity!!.checkUserAuthAndInternetFun(generalDataSendedModel!!)
        }
        if(favouriteList.size==0){
            binding.noProductsInBin.noProductsImg?.setImageResource(R.drawable.ic_fav_disactive_icon)
            binding.noProductsInBin.noProductsTv1?.text=getString(R.string.The_favourite_does_not_contain_any_products)
            binding.noProductsInBin.noProductsTv2?.text=getString(R.string.add_to_favourite_first)
            binding.noProductsInBin.noProducts?.visibility=View.VISIBLE
            binding.noProductsInBin.noProductsBrowseProductsBtn.setOnClickListener {
                navController?.navigate(R.id.subCategoryFragment,bundle)
            }
        }else{
            binding.noProductsInBin.noProducts?.visibility=View.GONE
        }
        favouriteList.add(AllProductForRom(
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
        favouriteList.add(AllProductForRom(
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
        favouriteList.add(AllProductForRom(
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
        favouriteList.add(AllProductForRom(
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
        favouriteList.add(AllProductForRom(
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
        favouriteList.add(AllProductForRom(
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
        favouriteList.add(AllProductForRom(
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
        favouriteList.add(AllProductForRom(
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
        favouriteList.add(AllProductForRom(
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
        favouriteList.add(AllProductForRom(
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

    override fun onBack() {
        navController?.popBackStack()
        navController?.navigate(R.id.homeFragment2)
    }
    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        TODO("Not yet implemented")
    }

    override fun doChanges(generalDataSendedModel: GeneralDataSendedModel) {
        if("dialogLoginBack".equals(generalDataSendedModel.from)){
            onBack()
        }
    }
}