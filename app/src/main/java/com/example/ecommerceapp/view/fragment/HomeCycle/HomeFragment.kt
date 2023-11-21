package com.example.ecommerceapp.view.fragment.HomeCycle

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.ecommerceapp.R

import com.example.ecommerceapp.adapter.*
import com.example.ecommerceapp.data.local.SharedPreferencesManger
import com.example.ecommerceapp.data.model.HomeListItems
import com.example.ecommerceapp.data.model.HomeModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentFavouriteBinding
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.utils.Dialogs.UpdateNowDialog
import com.example.ecommerceapp.utils.GPSTracker
import com.example.ecommerceapp.utils.GPSTracker.Companion.getAddressWithDetails
import com.example.ecommerceapp.utils.ZoomOutPageTransformer
import com.example.ecommerceapp.utils.interfaces.MakeChangeInFragment
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.ViewModel.GeneralViewModelMethods.checkUpdateApp
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import com.example.ecommerceapp.view.main.data.models.AllProductForRom
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils

import kotlinx.coroutines.*
import java.lang.Math.abs
import java.util.*
import java.util.concurrent.Executors


class HomeFragment : BaseFragment(), TryAgainOncall ,MakeChangeInFragment{
    private lateinit var binding: FragmentHomeBinding
    private var counter: CountDownTimer? = null
    private var itemCounts: Int=0
    private var currentAddress2: AddressesForRoom?=null
    private lateinit var addressSelected: AddressesForRoom
    private var homeItemsList= mutableListOf<HomeListItems>()
    private var homeModel= HomeModel()
    private var homeMainAdapter: HomeMainAdapter?=null
    private var imageList= mutableListOf<Int>()
    private var left: Boolean=true

    //    private lateinit var runnable: Runnable
    private var viewPager: ViewPager2? = null
    var handler: Handler? = null



//    @BindView(R.id.fragment_home_show_all_category_btu_tv)
//    var fragmentHomeShowAllCategoryBtuTv: TextView? = null
//
//    @BindView(R.id.fragment_home_category_recycle)
//    var fragmentHomeCategoryRecycle: RecyclerView? = null


    var categoryNameAdapter: CategoryNameAdapter? = null
    var moreSellerAdapter: MoreSellerAdapter? = null
    var recyclerViewLayoutManager: LayoutManager? = null


//    @BindView(R.id.fragment_home_show_all_more_seller_btu_tv)
//    var fragmentHomeShowAllMoreSellerBtuTv: TextView? = null

////    @BindView(R.id.fragment_home_more_seller_recycle)
//    var fragmentHomeMoreSellerRecycle: RecyclerView? = null
//
////    @BindView(R.id.fragment_home_famous_mark_recycle)
//    var fragmentHomeFamousMarkRecycle: RecyclerView? = null
//    var famousMarkAdapter: FamousMarkAdapter? = null
//
////    @BindView(R.id.fragment_home_today_offers_recycle)
//    var fragmentHomeTodayOffersRecycle: RecyclerView? = null
//    var todayOffersAdapter: TodayOffersAdapter? = null
    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private lateinit var gpsTracker: GPSTracker

    private var gLayout: GridLayoutManager? = null
    private var root: View? = null
    var navController: NavController? = null
    private var dataBase: DataBaseKotlin? = null
    var binList = ArrayList<AllProductForRom>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        root=binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        onClick(root!!)
        counter = object : CountDownTimer(115, 50) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                setUpActivity()
                homeCycleActivity?.showenav()
            }
        }.start()
    }
    override fun onStart() {
        super.onStart()
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
    private fun initHomeRv() {
        gLayout = GridLayoutManager(activity, 1)
        binding.FragmentHomeMainRv?.layoutManager = gLayout
        homeMainAdapter = HomeMainAdapter(homeModel, generalDataSendedModel!!,
            root!!
        )
        binding.FragmentHomeMainRv?.adapter = homeMainAdapter
    }


    private fun initData() {
        try {
            setUpActivity()
            dataBase = DataBaseKotlin.getInstance(requireContext()!!)
            try {
                homeCycleActivity?.showenav()
                homeCycleActivity?.hideLoading()
            }catch (e:Exception){}
            homeItemsList.clear()
            generalDataSendedModel= GeneralDataSendedModel()
            generalDataSendedModel!!.activity=requireActivity()
            generalDataSendedModel!!.tryAgainCall=this
            generalDataSendedModel!!.makeChangeInFragment=this
            generalDataSendedModel!!.navController=navController
            generalDataSendedModel!!.fragment_sr_refresh=binding.fragmentFaqSrRefresh
            generalDataSendedModel!!.load_more=binding.itemLoadMore.loadMore
            generalDataSendedModel?.context=requireContext()
            generalDataSendedModel?.homeCycleActivity=homeCycleActivity
            generalDataSendedModel!!.maxPage=maxPage

//            no_result_error_title.visibility=View.VISIBLE

//            Log.i("centerLat",getAddressWithDetails(requireContext(), gpsTracker.getLatitude(), gpsTracker.getLongitude())+ homeCycleActivity?.addressSelectedItem?.lang.toString())
            homeItemsList.add(HomeListItems("Slider"))
            homeItemsList.add(HomeListItems("TodayOffers"))
            homeItemsList.add(HomeListItems("TwoAds"))
            homeItemsList.add(HomeListItems("MoreSeller"))
            homeItemsList.add(HomeListItems("Ads"))
            homeItemsList.add(HomeListItems("phonesAndTaplets"))
            homeItemsList.add(HomeListItems("Category"))
            homeItemsList.add(HomeListItems("FamousMark"))
            homeItemsList.add(HomeListItems("phonesAndTaplets"))
            homeItemsList.add(HomeListItems("MoreSeller"))

            homeModel.homeListItems = homeItemsList
            initHomeRv()
            homeMainAdapter?.notifyDataSetChanged()
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    Executors.newSingleThreadExecutor()
                        .execute {
                            addressSelected =
                                dataBase!!.addNewOrderItemDao()!!.loadSingleAddressSelected(true)
                            generalDataSendedModel?.mustUpdate = 0
                            checkUpdateApp(generalDataSendedModel!!)
                            launch {
                                try {
                                    if (!homeCycleActivity?.addressSelectedItem?.selected!!) {
                                        homeModel.AddressSelected = addressSelected
                                        homeMainAdapter?.changeLocation(homeModel.AddressSelected)
                                    }
                                } catch (e: Exception) {
                                }
                                binList =
                                    dataBase!!.addNewOrderItemDao()!!.allItems as ArrayList<AllProductForRom>

                                if (binList.size == 0) {
                                    binding.fragmentHomeCartFrameTv.visibility=View.GONE
//                                    root!!.fragment_home_cart_frame_tv2.visibility=View.VISIBLE

                                } else {
                                    itemCounts=0
                                    for (j in 0 until binList.size) {
                                        itemCounts+=binList[j].quantity.toInt()
                                    }
//                                    root!!.fragment_home_cart_frame_tv2.visibility=View.GONE
                                    binding.fragmentHomeCartFrameTv.visibility=View.VISIBLE
                                    if (itemCounts>99){
                                        binding.fragmentHomeCartFrameTv.text = "99+"
                                    }else {
                                        binding.fragmentHomeCartFrameTv.text = itemCounts.toString()
                                    }

//                                    val badgeDrawable = BadgeDrawable.create(requireContext())
//                                    badgeDrawable.number = 1
//                                    badgeDrawable.badgeGravity = BadgeDrawable.TOP_END
//                                    badgeDrawable.backgroundColor = resources.getColor(R.color.white)
//                                    badgeDrawable.badgeTextColor = resources.getColor(R.color.app_color)                                    //Note that there is a third argument which is our FrameLayout
//                                    //Note that there is a third argument which is our FrameLayout
//                                    BadgeUtils.attachBadgeDrawable(
//                                        badgeDrawable,
//                                        root!!.fragment_home_cart_img,
//                                        root!!.fragment_home_cart_frame
//                                    )
                                }
                            }
                        }

                    launch {
                        try{
                            if (homeCycleActivity?.addressSelectedItem?.selected!!) {
                                currentAddress2 =
                                    SharedPreferencesManger.LoadLastCurrentLocationData(
                                        requireActivity()
                                    )!!
                                homeModel.AddressSelected = currentAddress2
                                homeMainAdapter?.changeLocation(homeModel.AddressSelected)
                            }
//                            showToast(getActivity(), "success2 ")
                        }catch (e:Exception){}

                       gpsTracker = GPSTracker(generalDataSendedModel)

                        gpsTracker.startTrace()
                        homeCycleActivity?.addressSelectedItem?.title = getString(R.string.current_location)
                    }


//                    showToast(getActivity(), "success ")
                } catch (e: Exception) {
                }
            }
//            val dialog2 = UpdateNowDialog()
//            dialog2.showDialog(generalDataSendedModel!!)
//            GlobalScope.launch(Dispatchers.Main) {
//                var tt = async {
//                    homeItemsList.add(HomeListItems("Slider"))
//
//                    //from retrofit
//                }
//                var tt2 = async {
//                    homeItemsList.add(HomeListItems("TodayOffers"))
//                    homeItemsList.add(HomeListItems("TwoAds"))
//                    //from database /room
//                }
//
//
//
//                if(tt.await()==tt2.await()){ // عاوزهم يستنو على ميتنفزوا وبعدين يدخل هنا يتشك
////                    delay(5000)
//                    homeItemsList.add(HomeListItems("MoreSeller"))
//                    homeItemsList.add(HomeListItems("Ads"))
//                    homeItemsList.add(HomeListItems("phonesAndTaplets"))
//
//                    homeModel.homeListItems=homeItemsList
//                    homeMainAdapter.notifyDataSetChanged()
//                }
//                launch {
//                    homeItemsList.add(HomeListItems("Category"))
//                    homeItemsList.add(HomeListItems("FamousMark"))
//                    homeItemsList.add(HomeListItems("phonesAndTaplets"))
//                    homeItemsList.add(HomeListItems("MoreSeller"))
//                    homeModel.homeListItems=homeItemsList
//                    homeMainAdapter.notifyDataSetChanged()
//                }
//        }

//            launch == GlobalScope.launch  بس لازم جوه لو عاوز تنفذ اكتر من حاجه فى نفس الوقت
//           1- Dispatchers.Main    اى حاجه خفيفه زى الديزين اللايف داتا
//           2- Dispatchers.IO       هتجيب حاجه من الروم هتجيب حاجه من الداتا بيز هتجيب حاجه من الرتروفيت من الباك /api
//           3- Dispatchers.Default  لو هتعمل عمليات حسابيه هتاخد وقت كبير و اى حاجه بتاخد وقت كبير فى التنفيذ ودى الديفولط لو مضيفتوش
//            Dispatchers.Default is limited to the number of CPU cores (with a minimum of 2) so only N (where N == cpu cores) tasks can run in parallel in this dispatcher.
//           2- On the IO dispatcher there are by default 64 threads, so there could be up to 64 parallel tasks running on that dispatcher.
//            The idea is that the IO dispatcher spends a lot of time waiting (IO blocked), while the Default dispatcher is intended for CPU intensive tasks, where there is little or no sleep.
//           4- 5Dispatchers.Unconfined coroutine dispatcher starts a coroutine in the caller thread, but only until the first suspension point. After suspension it resumes the coroutine in the thread that is fully determined by the suspending function that was invoked. The unconfined dispatcher is appropriate for coroutines which neither consume CPU time nor update any shared data (like UI) confined to a specific thread.
//           5-  newSingleThreadContext("mohamed thread")  my thread





//            fragmentHomeCategoryRecycle=root!!.fragment_home_category_recycle
//            fragmentHomeFamousMarkRecycle=root!!.fragment_home_famous_mark_recycle
//            fragmentHomeMoreSellerRecycle=root!!.fragment_home_more_seller_recycle
//            fragmentHomeTodayOffersRecycle=root!!.fragment_home_today_offers_recycle
//            init()
//            setUpTransformer()




//            with(view_pager) {
////                adapter = DotIndicatorPagerAdapter()
//                setPageTransformer(true, ZoomOutPageTransformer())
//                worm_dots_indicator.attachTo(this)
//            }


        } catch (e: Exception) {
        }
    }

    private fun checkNoData() {
        if(homeItemsList.size==0){
            binding.fragmentHomeCartFrameTv.visibility=View.VISIBLE
            binding.FragmentHomeMainRv.visibility=View.GONE
        }else{
            binding.notFoundResultError.noResultErrorView.visibility=View.GONE
        }
        binding.notFoundResultError.noResultErrorView.visibility=View.VISIBLE
        binding.FragmentHomeMainRv.visibility=View.GONE
    }

    override fun onBack() {
        requireActivity().finish()
    }


    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(1))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager!!.setPageTransformer(transformer)
    }

    private fun init(){
        handler = Handler(Looper.myLooper()!!)
       val wormDotsIndicator = binding.wormDotsIndicator
            viewPager = binding.viewPager2



//            val viewPagerAdapter = ViewPagerAdapter(requireContext(), emptyList())
            val adapter = DotIndicatorPager2Adapter(generalDataSendedModel!!,viewPager!!,imageList)

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        viewPager!!.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        viewPager!!.adapter = adapter

        viewPager!!.offscreenPageLimit = 3
        viewPager!!.clipToPadding = false
        viewPager!!.clipChildren = false
        viewPager!!.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//        var ultraViewPager: UltraViewPager? = null

        wormDotsIndicator.attachTo(viewPager!!)
        viewPager!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            private var myState = 0
            private var currentPosition = 0
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

//                showToast(requireActivity(),viewPager!!.getScrollState().toString() )
                if (myState == ViewPager2.SCROLL_STATE_DRAGGING && currentPosition == position && currentPosition == 0) viewPager!!.setCurrentItem(
                    imageList.size-1
                ) else if (myState == ViewPager2.SCROLL_STATE_DRAGGING && currentPosition == position && currentPosition ==( imageList?.size!!-1)) {
                    viewPager!!.setCurrentItem(0)
                    viewPager!!.requestDisallowInterceptTouchEvent(true)
//                    handler!!.removeCallbacks(runnable)
//                    handler!!.postDelayed(runnable , 2000)
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                if(currentPosition < position) {
                    left=true
                } else if(currentPosition > position){
                    left=false
                    // handle swipe RIGHT
                }

                currentPosition = position; // Update current position
                super.onPageSelected(position)
//                handler!!.removeCallbacks(runnable)
//                handler!!.postDelayed(runnable , 2000)
            }

            override fun onPageScrollStateChanged(state: Int) {
                myState = state
                super.onPageScrollStateChanged(state)
            }

        })



    }



    override fun onPause() {
        super.onPause()

//        handler!!.removeCallbacks(runnable!!)
    }




    override fun onResume() {
        super.onResume()

//        handler!!.postDelayed(runnable , 2000)
    }

//    private val runnable = Runnable {
//        if(viewPager!!.currentItem==(imageList?.size!!-1)) {
//            viewPager!!.currentItem=0
//        }else{
//            viewPager!!.currentItem = viewPager!!.currentItem + 1
//        }
////        if(viewPager!!.currentItem==9) {
////          left=false
////        }else if(viewPager!!.currentItem==0){
////            left=true
////        }
////        if(left) {
////            viewPager!!.currentItem = viewPager!!.currentItem + 1
////        }else {
////            viewPager!!.currentItem = viewPager!!.currentItem - 1
////        }
//    }




//    private class RemindTask : TimerTask() {
//        var current: Int = viewPager.getCurrentItem()
//        override fun run() {
//            UiThreadStatement.runOnUiThread(Runnable {
//                if (current < strImages.size()) {
//                    viewPager.setCurrentItem(current)
//                    current += 1
//                } else {
//                    current = 0
//                    viewPager.setCurrentItem(current)
//                }
//            })
//        }
//    }

    fun onClick(root: View) {
        var bundle = Bundle()
        var generalBundelDataToSend = GeneralBundelDataToSend()
        generalBundelDataToSend.from="home"
        bundle.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
    binding.fragmentHomeCartImg!!.setOnClickListener { homeCycleActivity?.navController?.navigate(R.id.binFragment) }
    binding.fragmentHomeSearchImg!!.setOnClickListener {homeCycleActivity?.navController?.navigate(R.id.subCategoryFragment,bundle) }
    binding.fragmentHomeSearchEt!!.setOnClickListener {
        homeCycleActivity?.navController?.navigate(R.id.subCategoryFragment,bundle)
    }


    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        TODO("Not yet implemented")
    }

    override fun doChanges(generalDataSendedModel2: GeneralDataSendedModel) {
//        showToast(getActivity(), "success "+generalDataSendedModel2?.addressSelectedItem!!.lang)

        try {
//          generalDataSendedModel?.addressSelectedItem?.lat = gpsTracker.getLatitude()
//          generalDataSendedModel?.addressSelectedItem!!.lang=gpsTracker?.getLongitude()

          if (homeCycleActivity?.addressSelectedItem?.selected!!) {
              try {
                  generalDataSendedModel2?.addressSelectedItem!!.selected=homeCycleActivity?.addressSelectedItem?.selected!!
                  this.generalDataSendedModel = getAddressWithDetails(
                      generalDataSendedModel2!!
                  )
//                  showToast(getActivity(), "success "+generalDataSendedModel?.addressSelectedItem!!.address)
                  homeCycleActivity?.addressSelectedItem= generalDataSendedModel?.addressSelectedItem!!
                  homeCycleActivity?.addressSelectedItem?.title = getString(R.string.current_location)
//                  homeCycleActivity?.addressSelectedItem?.itemId = -1
                  val currentAddress = homeCycleActivity?.addressSelectedItem?.address
                  if (currentAddress == null || currentAddress?.trim()?.length == 0) {
                      homeCycleActivity?.addressSelectedItem = currentAddress2!!
//                      showToast(getActivity(), "success3 ")
                  } else {
                      SharedPreferencesManger.SaveData(
                          requireActivity()!!,
                          SharedPreferencesManger.ADRESS_DATA,
                          homeCycleActivity?.addressSelectedItem
                      )
//                            showToast(getActivity(), "success ")
                  }
//                  homeCycleActivity?.addressSelectedItem?.title = getString(R.string.current_location)

              }catch (e:Exception){
              }
              homeModel.AddressSelected = homeCycleActivity?.addressSelectedItem
          } else {
             homeModel.AddressSelected=addressSelected
          }

          homeMainAdapter?.changeLocation(homeModel.AddressSelected)
      }catch (e:Exception){
//          showToast(getActivity(), "success "+e.message)
      }


}


}

