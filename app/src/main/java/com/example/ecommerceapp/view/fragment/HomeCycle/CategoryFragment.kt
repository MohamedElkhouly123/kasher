package com.example.ecommerceapp.view.fragment.HomeCycle

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.CategoryAdapter
import com.example.ecommerceapp.data.api.ApiClient
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.databinding.FragmentCategoryBinding
import com.example.ecommerceapp.databinding.FragmentSplashPageBinding
import com.example.ecommerceapp.utils.Post
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.ViewModel.ViewModelCategoryAndFavouriteGetLists
import com.example.ecommerceapp.view.fragment.BaseFragment

import retrofit2.Call

class CategoryFragment : BaseFragment(), TryAgainOncall {
    private lateinit var binding: FragmentCategoryBinding
    private var counter: CountDownTimer?=null
    private lateinit var generalBundelDataToSend: GeneralBundelDataToSend
    private lateinit var bundle: Bundle
    private var fromBack: Boolean=false
    private var viewModel: ViewModelCategoryAndFavouriteGetLists?=null
    private var root: View? = null
    var navController: NavController? = null

//    @BindView(R.id.Fragment_category_cards_rv)
    var FragmentCategoryCardsRv: RecyclerView? = null

    var categoryAdapter: CategoryAdapter? = null
    private var gLayout: GridLayoutManager? = null
    var categoryList = ArrayList<Post>()
    var mark = ""
    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1
    private var generalDataSendedModel: GeneralDataSendedModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initCategoryRv()
        counter = object : CountDownTimer(115, 50) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                setUpActivity()
                homeCycleActivity?.showenav()
            }
        }.start()
    }

    private fun initData() {
        if (this.getArguments() != null) {
            mark = this.getArguments()?.getString("mark").toString()
        }
        setUpActivity()
        if (mark.equals("show_all_category", ignoreCase = true)) {
            homeCycleActivity?.hidenav()
        }
        navController = Navigation.findNavController(root!!)
        homeCycleActivity?.showenav()
        bundle = Bundle()
        generalBundelDataToSend = GeneralBundelDataToSend()
        generalBundelDataToSend?.from="favourite"
        bundle?.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.fragment_sr_refresh=binding.FragmentCategorySrRefresh
        generalDataSendedModel!!.load_more=binding.itemLoadMore.loadMore
        generalDataSendedModel!!.maxPage=maxPage
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel?.context=requireContext()
//        if(categoryList.size==0){
//            root?.no_products_img?.setImageResource(R.drawable.ic_category_disactive_icon)
//            root?.no_products_tv1?.text=getString(R.string.there_are_no_product_categories)
//            root?.no_products_tv2?.text=getString(R.string.you_can_come_back_again_later)
//            root?.no_products_browse_products_btn?.text=getString(R.string.go_to_home)
////            root!!.no_products_browse_products_btn.visibility=View.INVISIBLE
//            root?.no_products?.visibility=View.VISIBLE
//            root!!.no_products_browse_products_btn.setOnClickListener {
//               onBack()
//            }
//        }else{
//            root?.no_products?.visibility=View.GONE
//        }
    }

    private fun initCategoryRv() {
        FragmentCategoryCardsRv=binding.FragmentCategoryCardsRv
        gLayout = GridLayoutManager(getContext(), 1)
        FragmentCategoryCardsRv?.layoutManager = gLayout
        categoryAdapter = CategoryAdapter(
            categoryList, generalDataSendedModel!!
        )
        FragmentCategoryCardsRv?.adapter = categoryAdapter
    }


    private fun initCategoryRv2() {
        navController = Navigation.findNavController(root!!)
        homeCycleActivity?.showenav()
        FragmentCategoryCardsRv=binding.FragmentCategoryCardsRv
        gLayout = GridLayoutManager(getContext(), 1)
        FragmentCategoryCardsRv?.layoutManager = gLayout
        FragmentCategoryCardsRv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    val visibleItemCount = gLayout!!.childCount
                    val totalItemCount = gLayout!!.itemCount
                    val pastVisiblesItems = gLayout!!.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount && totalItemCount != 0 && totalItemCount % perPage!! == 0) {
                        if (loading) {
                            loading = false
                            if (Filter) {
                                maxPage++
                            }
                            //                            showToast(getActivity(), totalItemCount+"");
                            if (maxPage <= allPages!!) {
                                binding.itemLoadMore.loadMore!!.visibility = View.VISIBLE
                                getAllCategoryList(maxPage)
                            }
                        }
                    }
                }
            }
        })
        categoryAdapter = CategoryAdapter(
            categoryList, generalDataSendedModel!!
        )
        FragmentCategoryCardsRv?.adapter = categoryAdapter

        if (categoryList.size == 0) {
            getAllCategoryList(1)
        }

        binding.FragmentCategorySrRefresh.setOnRefreshListener(OnRefreshListener {
            maxPage = 1
            lastItem = 0
            getAllCategoryList(1)
        })
    }

    private fun getAllCategoryList(page: Int) {
        if (page == 1) {
            maxPage = 1
        }
        val getAllUserResponceCall: Call<GetGeneralResponse?>
//        startShimmer(page);
        reInit(page)
//        if (Filter) {
//            searchText = fragmentAllPeopleWithSearchAutoEdtxt.getText().toString()
//            //                        Toast.makeText(getContext(), ""+maxPage, Toast.LENGTH_SHORT).show();
//            getAllUserResponceCall = if (searchText.length == 0) {
//                ApiClient.apiClient?.getCategories(page)!!
//            } else {
//                checkSearchText(searchText)
//                ApiClient.apiClient?.getCategories(page,text)!!
//            }
//        } else {
            getAllUserResponceCall = ApiClient.apiClient?.getCategories(page)!!
//        }
        viewModel?.getGeneralResponse(
            generalDataSendedModel!!,
            getAllUserResponceCall,

        )


    }

    private fun reInit(page: Int) {
        if (page == 1) {
            categoryList = ArrayList<Post>()
            categoryAdapter = CategoryAdapter(
                categoryList, generalDataSendedModel!!
            )
            FragmentCategoryCardsRv?.adapter = categoryAdapter
        }
 }

    fun  initListener() {
        maxPage = 1
        viewModel = ViewModelProvider(this).get(ViewModelCategoryAndFavouriteGetLists::class.java)
        viewModel!!.makeGetGeneralResponse.observe(requireActivity(),
            Observer<GetGeneralResponse?> { response ->
                try {
                    if (response != null) {
                        if (response.message != null) {
//                            FragmentAllUsersCardsShimmer.setVisibility(View.GONE)
                            if (maxPage == 1) {
                                categoryList.clear()
//                                lastItem = response.getData().size()
//                                allPages = response.getLastPage()
//                                perPage = response.getPerPage()
                            }
                            if (lastItem > 0 && maxPage > 2) {
//                                lastItem = lastItem + response.getData().size()
                            }
                            fromBack = true
                            binding.itemLoadMore.loadMore!!.visibility = View.GONE
//                            categoryList.addAll(response.getData())
                            categoryAdapter?.notifyDataSetChanged()

                            if (!Filter) {
                                maxPage++
                            }
                            loading = true
                            if (categoryList.size != 0) {
                                binding.fragmentCategoryNoProduct!!.visibility = View.GONE
                            } else {
                                binding.fragmentCategoryNoProduct!!.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        //                        showToast(getActivity(), "max=");
                    }
                } catch (e: Exception) {
                }
            })
    }
    override fun onBack() {
        navController?.popBackStack()
        navController?.navigate(R.id.homeFragment2)
    }
    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        getAllCategoryList(0)
    }

}