package com.example.ecommerceapp.view.fragment.subPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.UserRateAdapter
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentUsersRatesBinding
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.utils.Post
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall

class UsersRateFragment : BaseFragment(), TryAgainOncall {
    private lateinit var fragmentUserRateRv: RecyclerView
    private lateinit var binding: FragmentUsersRatesBinding
    private var root: View?=null
    var userRateAdapter: UserRateAdapter? = null
    var navController: NavController? = null
    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1
    private var generalDataSendedModel: GeneralDataSendedModel?=null

    private var gLayout: GridLayoutManager? = null
    var binList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUsersRatesBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        navController = Navigation.findNavController(root!!)
        initData()
        initBinRecycle()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

//        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    private fun initData() {
        homeCycleActivity?.hidenav()
        fragmentUserRateRv=binding.fragmentUserRateRv
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel!!.fragment_sr_refresh=binding.fragmentUserRateSrRefresh
//        generalDataSendedModel!!.load_more=binding.load_more
        generalDataSendedModel?.context=requireContext()
        generalDataSendedModel!!.maxPage=maxPage
        onClick()
    }

    private fun initBinRecycle() {

        gLayout = GridLayoutManager(getContext(), 1)
        fragmentUserRateRv!!.layoutManager = gLayout
        userRateAdapter = UserRateAdapter(binList, generalDataSendedModel!!)
        fragmentUserRateRv!!.adapter = userRateAdapter
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