package com.example.ecommerceapp.view.fragment.subPages

import android.content.ContentValues
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.PagerAdapter

import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.MoreSellerAdapter
import com.example.ecommerceapp.adapter.ProductOverviewAdapter
import com.example.ecommerceapp.adapter.SliderViewAdapter
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentProductDetailsBinding
import com.example.ecommerceapp.databinding.FragmentRentalPaidBinding
import com.example.ecommerceapp.utils.HelperMethod.shareLink
import com.example.ecommerceapp.utils.Post
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AllProductForRom
import com.tmall.ultraviewpager.UltraViewPager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class ProductDetailsFragment : BaseFragment(), TryAgainOncall {
    private lateinit var FragmentProductDetailsRelatedProductRv: RecyclerView
    private lateinit var binding: FragmentProductDetailsBinding
    private var itemCounts: Int=0
    var binList = ArrayList<AllProductForRom>()
    private var root: View?=null
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private var relatedProductList2= mutableListOf<AllProductForRom>()

    private var itemId: Int = 0
    private lateinit var allProductForRom: AllProductForRom
    private var productItemCount: Int=0
    var navController: NavController? = null
    var ultraViewPager: UltraViewPager? = null
    var adapter: PagerAdapter? = null
    var images = ArrayList<Int>()

    var fragmentProductDetailsOutLineHeart: ImageView? = null

    var fragmentProductDetailsFillHeart: ImageView? = null

    var cardItemLike: FrameLayout? = null

//    @BindView(R.id.Fragment_product_details_overview_rv)
    var FragmentProductDetailsOverviewRv: RecyclerView? = null

    var fragmentProductDetailsIncreaseNumberProduct: FrameLayout? = null

    var fragmentProductDetailsNumberOfProductTv: TextView? = null

    var fragmentProductDetailsDecreaseNumberProduct: FrameLayout? = null

    var fragmentProductDetailsNumberProductLayout: LinearLayout? = null

    private var items: List<AllProductForRom?>?= ArrayList<AllProductForRom>()
    private var dataBase: DataBaseKotlin? = null


    var fragmentAdvertisementDetailsUltraViewpager: UltraViewPager? = null
    private var gLayout: GridLayoutManager? = null
    var productOverviewAdapter: ProductOverviewAdapter? = null
    var overViewProductList = ArrayList<Post>()
    var relatedProductList = ArrayList<Post>()
    var recyclerViewLayoutManager: LayoutManager? = null
    var relatedProductAdapter: MoreSellerAdapter? = null
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val window: Window = requireActivity().getWindow()
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//        }
    }

    override fun onBack() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        super.onBack()
    }

    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            requireActivity().window.statusBarColor = Color.TRANSPARENT
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (this.arguments != null) {
            generalBundelDataToSend = this.requireArguments().getSerializable("generalBundelDataToSend") as GeneralBundelDataToSend?
        }
        // Inflate the layout for this fragment
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        navController = Navigation.findNavController(root!!)

        ultraViewPager = binding.fragmentAdvertisementDetailsUltraViewpager
        initData(root!!)
        initProductOverViewRv()
        initRelatedProductRv(root!!)
        setScrolImagesAndData()
    }

    private fun initRelatedProductRv(root: View) {
        addList()
        recyclerViewLayoutManager =
            LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.FragmentProductDetailsRelatedProductRv?.layoutManager = recyclerViewLayoutManager
//        relatedProductAdapter = MoreSellerAdapter(
//            relatedProductList, getActivity(), requireContext(),
//            navController!!
//        )
        relatedProductAdapter= MoreSellerAdapter(relatedProductList2, generalDataSendedModel!!, root!!)
        binding.FragmentProductDetailsRelatedProductRv?.adapter = relatedProductAdapter
    }

    private fun addList() {
        relatedProductList2.add(AllProductForRom(
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
        relatedProductList2.add(AllProductForRom(
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
        relatedProductList2.add(AllProductForRom(
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
        relatedProductList2.add(AllProductForRom(
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
        relatedProductList2.add(AllProductForRom(
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


    private fun initProductOverViewRv() {
        gLayout = GridLayoutManager(getContext(), 1)
        FragmentProductDetailsOverviewRv?.layoutManager = gLayout
        productOverviewAdapter = ProductOverviewAdapter(
            overViewProductList,generalDataSendedModel!!
        )
        FragmentProductDetailsOverviewRv?.adapter = productOverviewAdapter
    }

    private fun initData(root: View) {
        dataBase = DataBaseKotlin.getInstance(requireContext())
        FragmentProductDetailsRelatedProductRv=binding.FragmentProductDetailsRelatedProductRv
        FragmentProductDetailsOverviewRv = binding.FragmentProductDetailsOverviewRv
        homeCycleActivity?.hidenav()
        fragmentProductDetailsOutLineHeart=binding.fragmentProductDetailsOutLineHeart
        fragmentProductDetailsFillHeart=binding.fragmentProductDetailsFillHeart
        cardItemLike=binding.cardItemLike
        fragmentProductDetailsIncreaseNumberProduct=binding.fragmentProductDetailsIncreaseNumberProduct
        fragmentProductDetailsDecreaseNumberProduct=binding.fragmentProductDetailsDecreaseNumberProduct
        fragmentProductDetailsNumberOfProductTv=binding.fragmentProductDetailsStoreNumberOfProduct
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel!!.fragment_sr_refresh=binding.fragmentFaqSrRefresh
        generalDataSendedModel?.context=requireContext()
        if ("favourite".equals(generalBundelDataToSend?.from)) {
            makeHartRed()
        }
        onClick(root)
    }

    private fun setScrolImagesAndData() {
        ultraViewPager!!.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        adapter = SliderViewAdapter<Any>(images, generalDataSendedModel!!)
        ultraViewPager!!.adapter = adapter
        ultraViewPager!!.initIndicator()
        ultraViewPager!!.indicator
            .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
            .setFocusColor(getResources().getColor(R.color.app_color))
            .setNormalColor(Color.parseColor("#FFFFFF"))
            .setRadius(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    5f,
                    getResources().getDisplayMetrics()
                )
                    .toInt()
            )
        //set the alignment
        ultraViewPager!!.indicator.setGravity(Gravity.CENTER or Gravity.BOTTOM)
        ultraViewPager!!.indicator.setMargin(10, 0, 10, 80)
        //construct built-in indicator, and add it to  UltraViewPager
        ultraViewPager!!.indicator.build()

//set an infinite loop
        ultraViewPager!!.setInfiniteLoop(true)
        ultraViewPager!!.disableAutoScroll()
        //enable auto-scroll mode
        ultraViewPager!!.setAutoScroll(4000);
//        adapter!!.notifyDataSetChanged()
    }



    fun onClick(root: View) {
        var bundle = Bundle()
        var generalBundelDataToSend = GeneralBundelDataToSend()
        generalBundelDataToSend.from="productDetails"
        bundle.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
        binding.showBinAdditionDesign.fragmentProductDetailsBottomCartGoToCartBtn?.setOnClickListener {
            navController!!.navigate(R.id.binFragment,bundle)
        }
        binding.fragmentProductBackBtu?.setOnClickListener {
                super.onBack()
            }
        binding.fragmentProductDetailsShareImage?.setOnClickListener {
            shareLink(
                requireActivity(),
                "AppCourse/" +"courseData.getId().toString()" + "7e237eueefjsdjksf932ek64d5"
            )
        }
        fragmentProductDetailsIncreaseNumberProduct?.setOnClickListener {
            productItemCount++
            GlobalScope.launch { Dispatchers.IO
                Dispatchers.IO  // equal newSingleThreadExecutor
                dataBase!!.addNewOrderItemDao()!!.update(productItemCount.toString(), itemId)
                Log.i(ContentValues.TAG+""+items?.size, dataBase!!.addNewOrderItemDao()!!.loadSingle(itemId).quantity)
            }
            fragmentProductDetailsNumberOfProductTv?.text=""+productItemCount
            setCartNumber()
            binding.fragmentProductDetailsShowBinAdditionDesign.visibility=View.VISIBLE
            binding.fragmentProductDetailsPaddingTv.visibility=View.VISIBLE
            checkIcon()
        }
        fragmentProductDetailsDecreaseNumberProduct?.setOnClickListener {
            if(productItemCount>0) {
                setCartNumber()
                if(productItemCount==1) {
                    binding.fragmentProductDetailsShowBinAdditionDesign.visibility=View.GONE
                    binding.fragmentProductDetailsPaddingTv.visibility=View.GONE
                    binding.productDetailsAddToBinBtu?.visibility = View.VISIBLE
                    binding.fragmentProductDetailsNumberProductLayout!!.visibility = View.GONE
                    Executors.newSingleThreadExecutor()
                        .execute {
//                            Log.i(ContentValues.TAG+""+items?.size, itemId.toString())
                            dataBase!!.addNewOrderItemDao()!!.deleteById(itemId)
                        }
                    productItemCount--
                }else{
                    setCartNumber()
                    binding.fragmentProductDetailsShowBinAdditionDesign.visibility=View.VISIBLE
                    binding.fragmentProductDetailsPaddingTv.visibility=View.VISIBLE
                    productItemCount--
                    Executors.newSingleThreadExecutor()
                        .execute {
                            dataBase!!.addNewOrderItemDao()!!.update(productItemCount.toString(),itemId)
                            dataBase!!.addNewOrderItemDao()!!.loadSingle(itemId)
                    }
                        }
                    fragmentProductDetailsNumberOfProductTv?.text = "" + productItemCount
                checkIcon()

            }
        }
        binding.cardItemLike?.setOnClickListener {
            if (fragmentProductDetailsOutLineHeart!!.visibility == View.VISIBLE) {
              makeHartRed()
            } else {
                fragmentProductDetailsOutLineHeart!!.visibility = View.VISIBLE
                fragmentProductDetailsFillHeart!!.visibility = View.GONE
                cardItemLike!!.setBackgroundDrawable(
                    requireActivity().getResources().getDrawable(R.drawable.circle_shap_love)
                )
            }
        }
        binding.productDetailsAddToBinBtu?.setOnClickListener {
            binding.productDetailsAddToBinBtu?.visibility = View.GONE
            binding.fragmentProductDetailsNumberProductLayout!!.visibility = View.VISIBLE
            productItemCount=1
            fragmentProductDetailsNumberOfProductTv?.text=""+productItemCount
            Executors.newSingleThreadExecutor()
                .execute {
                    allProductForRom = AllProductForRom(
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
                        "1"
                    )
                    dataBase!!.addNewOrderItemDao()!!.insert(allProductForRom)
                    items= ArrayList<AllProductForRom>()
                    items = dataBase!!.addNewOrderItemDao()!!.allItems
                    itemId= items?.get((items!!.size-1))!!.itemId
                }
            setCartNumber()
            binding.fragmentProductDetailsShowBinAdditionDesign.visibility=View.VISIBLE
            binding.fragmentProductDetailsPaddingTv.visibility=View.VISIBLE
//            root!!.fragment_product_details_bottom_cart_lotti.auto=1
            checkIcon()
            }
        binding.showBinAdditionDesign.fragmentProductDetailsBottomCartCloseBtn?.setOnClickListener {
            binding.fragmentProductDetailsShowBinAdditionDesign.visibility=View.GONE
            binding.fragmentProductDetailsPaddingTv.visibility=View.GONE
        }
        binding.fragmentProductDetailsNumberOfRateTv?.setOnClickListener {
            navController!!.navigate(R.id.usersRateFragment)
        }

        binding.fragmentProductDetailsNumberOfRateRb?.setOnClickListener {
            navController!!.navigate(R.id.usersRateFragment)

        }

        }

    private fun setCartNumber() {
//        root!!.fragment_product_details_bottom_cart_lotti.loop(true)
        binding.showBinAdditionDesign.fragmentProductDetailsBottomCartLotti.playAnimation()


//        root!!.fragment_product_details_bottom_cart_lotti.cancelAnimation()
        binList =
            dataBase!!.addNewOrderItemDao()!!.allItems as ArrayList<AllProductForRom>
        itemCounts=0
        for (j in 0 until binList.size) {
            itemCounts+=binList[j].quantity.toInt()
        }
        if (itemCounts>99){
            binding.showBinAdditionDesign.fragmentProductDetailsBottomCartGoToCartNumTv.text = "99+"
        }else {
            binding.showBinAdditionDesign.fragmentProductDetailsBottomCartGoToCartNumTv.text = itemCounts.toString()
        }

    }

    fun setupAnimation(){
        val animation = root!!.findViewById<LottieAnimationView>(R.id.fragment_product_details_bottom_cart_lotti)
        animation.speed = 2.0F // How fast does the animation play
        animation.progress = 0.5F // Starts the animation from 50% of the beginning
        animation.addAnimatorUpdateListener {
            // Called everytime the frame of the animation changes
        }
        animation.repeatMode = LottieDrawable.RESTART // Restarts the animation (you can choose to reverse it as well)
        animation.cancelAnimation() // Cancels the animation
    }

    private fun checkIcon() {

        if(productItemCount==0) {
        }else if(productItemCount==1) {
            binding.fragmentProductDetailsDecreaseNumberDeleteImg.visibility=View.VISIBLE
            binding.fragmentProductDetailsDecreaseNumberImg.visibility=View.GONE
        }else{
            binding.fragmentProductDetailsDecreaseNumberDeleteImg.visibility=View.GONE
            binding.fragmentProductDetailsDecreaseNumberImg.visibility=View.VISIBLE
//            root!!.fragment_product_details_decrease_number_img.setImageResource(R.drawable.ic_minus_icon)
        }

    }

    private fun makeHartRed() {
        fragmentProductDetailsOutLineHeart!!.visibility = View.GONE
        fragmentProductDetailsFillHeart!!.visibility = View.VISIBLE
        cardItemLike!!.setBackgroundDrawable(
            requireActivity().getResources().getDrawable(R.drawable.circle_shap_love_red)
        )
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        TODO("Not yet implemented")
    }
}