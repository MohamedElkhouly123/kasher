package com.example.ecommerceapp.view.fragment.subPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.RentalPaidAdapter
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentRentalPaidBinding
import com.example.ecommerceapp.databinding.FragmentSearchFragmentBinding
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.utils.Post
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall

class RentalPaidFragment : BaseFragment(), TryAgainOncall {
    private lateinit var binding: FragmentRentalPaidBinding
    private var root: View?=null
    var navController: NavController? = null


    private var gLayout: GridLayoutManager? = null
    var rentalPaidList = ArrayList<Post>()
    var rentalPaidAdapter: RentalPaidAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private var allPages: Int? = null
    private var loading = false
    private var lastItem = 0
    private var perPage: Int? = null
    private var Filter = false
    var maxPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRentalPaidBinding.inflate(inflater, container, false)
        root=binding.root

        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initRv()
    }

    private fun setData() {
        setUpActivity()
        homeCycleActivity?.hidenav()
        navController = Navigation.findNavController(root!!)
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel!!.fragment_sr_refresh=binding.fragmentFaqSrRefresh
        generalDataSendedModel!!.load_more=binding.itemLoadMore.loadMore
        generalDataSendedModel?.context=requireContext()
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
        rentalPaidList.add(Post("dfsdfsdfsdfsd", "asdsadassad", true))
    }

    private fun initRv() {
        linearLayoutManager = object : LinearLayoutManager(getContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        gLayout = GridLayoutManager(getContext(), 1)
        binding.fragmentRentalCardsRv!!.layoutManager = gLayout
        binding.fragmentRentalCardsRv!!.hasFixedSize()
        rentalPaidAdapter = RentalPaidAdapter(
            rentalPaidList, generalDataSendedModel!!
        )
        binding.fragmentRentalCardsRv!!.adapter = rentalPaidAdapter
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        TODO("Not yet implemented")
    }
}