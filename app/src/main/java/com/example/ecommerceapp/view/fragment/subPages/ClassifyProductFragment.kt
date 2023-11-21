package com.example.ecommerceapp.view.fragment.subPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.ColorProductAdapter
import com.example.ecommerceapp.adapter.TradeMarkAdapter
import com.example.ecommerceapp.data.model.Color
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentClassifyProductBinding
import com.example.ecommerceapp.databinding.FragmentCompleteBinding
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.utils.Post
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.google.android.material.slider.RangeSlider


class ClassifyProductFragment : BaseFragment(), TryAgainOncall {
    private lateinit var binding: FragmentClassifyProductBinding
    private lateinit var recyclerViewHZLayoutManager: LinearLayoutManager
    private var rangeSlider: RangeSlider?=null
    private var root: View?=null
    var navController: NavController? = null


//    @BindView(R.id.Fragment_classify_product_rv)
    var FragmentClassifyProductRv: RecyclerView? = null

//    @BindView(R.id.Fragment_classify_product_color_rv)
    var FragmentClassifyProductColorRv: RecyclerView? = null
    var colorProductAdapter: ColorProductAdapter? = null
    private var gLayout: GridLayoutManager? = null
    var tradeMarkAdapter: TradeMarkAdapter? = null
    var tradeMarkList = ArrayList<Post>()
    var colorProductList = ArrayList<Color>()
    private var generalDataSendedModel: GeneralDataSendedModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentClassifyProductBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        navController = Navigation.findNavController(root!!)
        initData(root!!)
        initTradeMarkRv()
        fillColor()
        initColorProductRv()
    }
    private fun fillColor() {
        colorProductList.add(
            Color(
                requireActivity().getResources().getColor(R.color.red_C91543),
                "red"
            )
        )
        colorProductList.add(
            Color(
                requireActivity().getResources().getColor(R.color.black_000000),
                "black"
            )
        )
        colorProductList.add(Color(requireActivity().getResources().getColor(R.color.white), "white"))
        colorProductList.add(
            Color(
                requireActivity().getResources().getColor(R.color.grean_199B00),
                "green"
            )
        )
        colorProductList.add(
            Color(
                requireActivity().getResources().getColor(R.color.blue_1F28BF),
                "blue"
            )
        )
        colorProductList.add(
            Color(
                requireActivity().getResources().getColor(R.color.bink_7A2ABF),
                "pink"
            )
        )
    }

    private fun initColorProductRv() {
        recyclerViewHZLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        FragmentClassifyProductColorRv!!.layoutManager = recyclerViewHZLayoutManager
        colorProductAdapter = ColorProductAdapter(
            colorProductList, generalDataSendedModel!!
        )
        FragmentClassifyProductColorRv!!.adapter = colorProductAdapter
    }

    private fun initTradeMarkRv() {
        gLayout = GridLayoutManager(getContext(), 3)
        FragmentClassifyProductRv!!.layoutManager = gLayout
        tradeMarkAdapter =
            TradeMarkAdapter(tradeMarkList, generalDataSendedModel!!)
        FragmentClassifyProductRv!!.adapter = tradeMarkAdapter
    }

    private fun initData(root: View) {
        homeCycleActivity?.hidenav()
        FragmentClassifyProductRv=binding.FragmentClassifyProductRv
        FragmentClassifyProductColorRv=binding.FragmentClassifyProductColorRv
        rangeSlider=binding.fragmentSearchPriceRangeSlider
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel!!.fragment_sr_refresh=binding.fragmentFaqSrRefresh
//        generalDataSendedModel!!.load_more=load_more
        generalDataSendedModel?.context=requireContext()
//        rangeSlider!!.minSeparation
//        rangeSlider!!.valueTo
//        rangeSlider!!.valueFrom
//        ToastCreator.onCreateSuccessToast(requireActivity(), "Success"+rangeSlider!!.values[1])
//you get current (values when user start dragging the silder, also the update view
        rangeSlider!!.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = rangeSlider!!.values
                //Those are the satrt and end values of sldier when user start dragging
//                Log.i("SliderPreviousValue From", values[0].toString())
//                Log.i("SliderPreviousValue To", values[1].toString())
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = rangeSlider!!.values
                //Those are the new updated values of sldier when user has finshed dragging
//                Log.i("SliderNewValue From", values[0].toString())
//                Log.i("SliderNewValue To", values[1].toString())

//                textView.setText("Start value: ${values[0]}, End value: ${values[1]}")
            }
        })


        //If you only want the slider start and end value and don't care about the previous values
        rangeSlider!!.addOnChangeListener { slider, value, fromUser ->
            val values = rangeSlider!!.values
//            textView2.text = "Start value: ${values[0]}, End value: ${values[1]}";
        }
        onClick()
    }

    fun onClick() {
        binding.backBtn.setOnClickListener {
            super.onBack()
        }
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        TODO("Not yet implemented")
    }
}