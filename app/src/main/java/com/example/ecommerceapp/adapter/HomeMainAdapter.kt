package com.example.ecommerceapp.adapter

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.HomeModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.Post
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import com.example.ecommerceapp.view.main.data.models.AllProductForRom
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class HomeMainAdapter(var homeModel: HomeModel,var generalDataSendedModel: GeneralDataSendedModel, val root :View) :
    RecyclerView.Adapter<HomeMainAdapter.HomeMainViewHolder>() {

    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController? = null
    private val payload: String? = null
    private val first = true
    var selectedItem = -1
    var vindorId = -1
    private var dataBase: DataBaseKotlin? = null
    var categoryNameList = mutableListOf<Post>()
    var moreSellerList = mutableListOf<AllProductForRom>()
    var famousMarkList = mutableListOf<Post>()
    private var adressesList= mutableListOf<AddressesForRoom>()
    var todayOffersList = mutableListOf<Post>()
    private var imageList= mutableListOf<Int>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeMainViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_home_main, null, false)
        return HomeMainViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: HomeMainViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: HomeMainViewHolder, position: Int) {
        dataBase = DataBaseKotlin.getInstance(context!!)
        var homeItem = homeModel.homeListItems[position]
        val bundle = Bundle()

        holder.cardHomeMainGoToMapBtn!!.setOnClickListener {
            val bundle = Bundle()
//            bundle.putString("type", "home")
//            Navigation.findNavController(root!!)?.navigate(R.id.addressFragment,bundle)
            Executors.newSingleThreadExecutor()
                .execute {
                    adressesList.clear()
                    adressesList.addAll(dataBase!!.addNewOrderItemDao()!!.allAddressesItems as ArrayList<AddressesForRoom>)
                    activity?.baseFragment?.homeCycleActivity?.addressList!!.clear()
                    activity?.baseFragment?.homeCycleActivity?.addressList!!.addAll(adressesList)

                    try {
                        Log.i(
                            ContentValues.TAG + "" + adressesList?.size,
                            adressesList[0].address.toString()
                        )
                    } catch (e: Exception) {
                    }
                    runBlocking (Dispatchers.Main) {
                        if (adressesList.size>0) {
//                            bundle.putSerializable("adressesList", adressesList as Serializable?)
                            bundle.putString("type", "home")
                            var generalBundelDataToSend = GeneralBundelDataToSend()
                            generalBundelDataToSend.from="haveList"
//                            HelperMethod.showToast(
//                                activity,
//                                generalBundelDataToSend?.from + ""
//                            )
                            bundle.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
                            Navigation.findNavController(root!!)
                                ?.navigate(R.id.action_homeFragment2_to_addressFragment, bundle)
                        }else{
                            Navigation.findNavController(root!!)
                                ?.navigate(R.id.mapFragment, bundle)
                        }
                    }
                }
        }
        holder.fragment_home_show_all_btn!!.setOnClickListener {
            val bundle = Bundle()
            generalDataSendedModel.generalBundelDataToSend?.from="home"
            bundle?.putSerializable("generalBundelDataToSend",generalDataSendedModel.generalBundelDataToSend)
            if ("Category".equals(homeItem.type)) {
                bundle.putString("mark", "show_all_category")
                Navigation.findNavController(root!!).navigate(R.id.categoryFragment, bundle)
            }
            if ("MoreSeller".equals(homeItem.type)) {
                Navigation.findNavController(root!!).navigate(R.id.rentalPaidFragment, bundle)
            }
            if ("FamousMark".equals(homeItem.type)) {
                Navigation.findNavController(root!!).navigate(R.id.subCategoryFragment, bundle)
            }
            if ("TodayOffers".equals(homeItem.type)) {
                Navigation.findNavController(root!!).navigate(R.id.subCategoryFragment, bundle)
            }
            if ("Ads".equals(homeItem.type)) {
//                Navigation.findNavController(root!!).navigate(R.id.categoryFragment, bundle)
            }
        }

    }
    private fun setData(holder: HomeMainViewHolder, position: Int) {
        var homeItem = homeModel.homeListItems[position]
        if (getItemViewType(position) == 0) {
            holder.cardHomeMainGoToMapBtn.setVisibility(View.VISIBLE)
            try{
                var addressTitle = homeModel.AddressSelected?.title?.trim()
//                activity?.baseFragment?.homeCycleActivity?.addressSelectedItem?.address
                if(homeModel.AddressSelected?.address?.trim()?.length!!>0){
//                holder.card_home_main_select_location_details_tv.visibility=View.VISIBLE
//                holder.card_home_main_select_location_details_tv.text=homeModel.AddressSelected?.address
                    holder.card_home_main_select_location_tv.text=homeModel.AddressSelected?.title+" ( "+ homeModel.AddressSelected?.street+" ، "+ homeModel.AddressSelected?.area+" )"
                }
            }catch (e:Exception){}
        } else {
            holder.cardHomeMainGoToMapBtn.setVisibility(View.GONE)
        }
        if("Slider".equals(homeItem.type)){
            holder.card_home_main_image_slider_cardview.setVisibility(View.VISIBLE)
            initSlider(holder,position)
            holder.card_home_main_show_type_name_parent_ly.setVisibility(View.GONE)
        }else{
            holder.card_home_main_image_slider_cardview.setVisibility(View.GONE)
            holder.card_home_main_show_type_name_parent_ly.setVisibility(View.VISIBLE)
            holder.card_home_main_show_type_name_ly.setVisibility(View.VISIBLE)
            holder.fragment_home_show_all_btn!!.setVisibility(View.VISIBLE)
            holder.fragment_home_addition_text_tv!!.setVisibility(View.GONE)
//            holder.card_home_main_type_name_tv.text=activity!!.getText(R.string.Most_Rated_Products)

            if("Category".equals(homeItem.type)) {
                initCategoryRv(holder,position)
            }
            if("MoreSeller".equals(homeItem.type)) {
                holder.card_home_main_type_name_tv.text=activity!!.getText(R.string.more_seller)
                initMoreSellerRv(holder,position)
            }
            if("FamousMark".equals(homeItem.type)) {
                holder.card_home_main_type_name_tv.text=activity!!.getText(R.string.more_seller)
                initFamousMarkRv(holder,position)
            }
            if("phonesAndTaplets".equals(homeItem.type)) {
                holder.card_home_main_type_name_tv.text=activity!!.getText(R.string.phones_and_tablets)
                initCircleItemRv(holder,position)
            }
            if("TodayOffers".equals(homeItem.type)) {
                holder.card_home_main_type_name_tv.text=activity!!.getText(R.string.today_offer)
                holder.fragment_home_addition_text_tv!!.setVisibility(View.VISIBLE)
                initMoreSellerRv(holder,position)
            }
            if ("Ads".equals(homeItem.type)) {
                holder.card_home_main_show_type_name_ly.setVisibility(View.GONE)
                holder.fragment_home_show_all_btn!!.setVisibility(View.GONE)
                initAdsRv(holder, position, "one")
            }
            if ("TwoAds".equals(homeItem.type)) {
                holder.card_home_main_show_type_name_ly.setVisibility(View.GONE)
                holder.fragment_home_show_all_btn!!.setVisibility(View.GONE)
                initAdsRv(holder, position, "two")
            }
        }

    }
    override fun getItemCount(): Int {
//        return 7
        return homeModel.homeListItems.size
    }

    private fun initSlider(holder: HomeMainViewHolder, position: Int) {
        imageList.clear()
        imageList.add(R.drawable.devise)
        imageList.add(R.drawable.devise)
        imageList.add(R.drawable.devise)
        imageList.add(R.drawable.carss)
        imageList.add(R.drawable.carss)
        imageList.add(R.drawable.carss)
        var adapter = SliderAdapter(generalDataSendedModel,imageList)
        holder.sliderView!!.setSliderAdapter(adapter)
        holder.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        holder.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        holder.sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
//        holder.sliderView.indic
//        val zoomOutPageTransformer = ZoomOutPageTransformer()
//        sliderView!!.setCustomSliderTransformAnimation { page, position ->
//            zoomOutPageTransformer.transformPage(page, position)
//        }
//        sliderView.indicatorSelectedColor = Color.WHITE
//        sliderView.indicatorUnselectedColor = Color.GRAY
        holder.sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :
        holder.sliderView.startAutoCycle()
    }

    private fun initAdsRv(holder: HomeMainViewHolder, position: Int, type: String) {
        if("two".equals(type)){
            holder.gLayout = GridLayoutManager(activity, 2)
        }else {
            holder.gLayout = GridLayoutManager(activity, 1)
        }
        holder.fragmentHomeMainRecycle?.layoutManager = holder.gLayout
        holder.adsAdapter = AdsAdapter(famousMarkList,generalDataSendedModel!!)
        holder.fragmentHomeMainRecycle?.adapter = holder.adsAdapter
    }

    private fun initFamousMarkRv(holder: HomeMainViewHolder, position: Int) {
        holder.card_home_main_type_name_tv.text=activity!!.getText(R.string.famous_mark)
        holder.recyclerViewLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        holder.fragmentHomeMainRecycle?.layoutManager = holder.recyclerViewLayoutManager
        holder.famousMarkAdapter = FamousMarkAdapter(famousMarkList, generalDataSendedModel!!)
        holder.fragmentHomeMainRecycle?.adapter = holder.famousMarkAdapter
    }

    private fun initCircleItemRv(holder: HomeMainViewHolder, position: Int) {
        holder.recyclerViewLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        holder.fragmentHomeMainRecycle?.layoutManager = holder.recyclerViewLayoutManager
        holder.homeCircleItemAdapter = HomeCircleItemAdapter(famousMarkList, generalDataSendedModel!!,root)
        holder.fragmentHomeMainRecycle?.adapter = holder.homeCircleItemAdapter
    }

    private fun initMoreSellerRv(holder: HomeMainViewHolder, position: Int) {
        addList()
        holder.recyclerViewLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        holder.fragmentHomeMainRecycle?.layoutManager = holder.recyclerViewLayoutManager
        holder.moreSellerAdapter = MoreSellerAdapter(moreSellerList, generalDataSendedModel, root!!)
        holder.fragmentHomeMainRecycle?.adapter = holder.moreSellerAdapter
    }

    private fun addList() {
        moreSellerList.add(AllProductForRom(
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
        moreSellerList.add(AllProductForRom(
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
        moreSellerList.add(AllProductForRom(
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
        moreSellerList.add(AllProductForRom(
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
        moreSellerList.add(AllProductForRom(
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

    private fun initCategoryRv(holder: HomeMainViewHolder, position: Int) {
        holder.card_home_main_type_name_tv.text=activity!!.getText(R.string.categories)
        holder.recyclerViewLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        holder.fragmentHomeMainRecycle?.layoutManager = holder.recyclerViewLayoutManager
        holder.categoryNameAdapter = CategoryNameAdapter(categoryNameList,generalDataSendedModel)
        holder.fragmentHomeMainRecycle?.adapter = holder.categoryNameAdapter
    }

    fun changeLocation(addressSelected: AddressesForRoom?) {
        homeModel.AddressSelected=addressSelected
//        HelperMethod.showToast(
//            activity,
//            "ok" + homeModel.AddressSelected?.address + homeModel.AddressSelected?.street
//        )
        notifyDataSetChanged()
    }

    inner class HomeMainViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        lateinit var homeCircleItemAdapter: HomeCircleItemAdapter
        lateinit var moreSellerAdapter: MoreSellerAdapter
        lateinit var categoryNameAdapter: CategoryNameAdapter
        lateinit var famousMarkAdapter: FamousMarkAdapter
        lateinit var recyclerViewLayoutManager: LinearLayoutManager
        lateinit var adsAdapter: AdsAdapter
        lateinit var gLayout: GridLayoutManager
        var sliderView: SliderView = view.findViewById(R.id.card_home_main_image_slider)
        var fragmentHomeMainRecycle: RecyclerView = view.findViewById(R.id.fragment_home_main_rv)
        var cardHomeMainGoToMapBtn: LinearLayout = view.findViewById(R.id.card_home_main_go_to_map_btu)
        var card_home_main_image_slider_cardview: CardView = view.findViewById(R.id.card_home_main_image_slider_cardview)
        var card_home_main_show_type_name_ly: LinearLayout = view.findViewById(R.id.card_home_main_show_type_name_ly)
        var card_home_main_show_type_name_parent_ly: LinearLayout = view.findViewById(R.id.card_home_main_show_type_name_parent_ly)
        var card_home_main_type_name_tv: TextView = view.findViewById(R.id.card_home_main_type_name_tv)
        var fragment_home_show_all_btn: TextView = view.findViewById(R.id.fragment_home_show_all_btn)
        var fragment_home_addition_text_tv: TextView = view.findViewById(R.id.fragment_home_addition_text_tv)
        var card_home_main_select_location_details_tv: TextView = view.findViewById(R.id.card_home_main_select_location_details_tv)
        var card_home_main_select_location_tv: TextView = view.findViewById(R.id.card_home_main_select_location_tv)


    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
    }


}