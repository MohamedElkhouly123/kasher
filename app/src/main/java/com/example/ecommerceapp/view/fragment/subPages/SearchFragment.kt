package com.example.ecommerceapp.view.fragment.subPages

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.SearchAdapter
import com.example.ecommerceapp.databinding.FragmentSearchFragmentBinding
import com.example.ecommerceapp.databinding.FragmentSubCategoryBinding
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.utils.Post
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin


class SearchFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchFragmentBinding
    private var root: View?=null
    var navController: NavController? = null



    var FragmentSearchProductRv: RecyclerView? = null


    private var gLayout: GridLayoutManager? = null
    var searchProductList = ArrayList<Post>()
    var searchAdapter: SearchAdapter? = null
    private var dataBase: DataBaseKotlin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchFragmentBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        initData(root!!)
        initSearchProductRv()
    }

    private fun initSearchProductRv() {
//        gLayout = GridLayoutManager(getContext(), 1)
//        FragmentSearchProductRv?.layoutManager = gLayout
//        searchAdapter = SearchAdapter(
//            searchProductList, requireActivity(), requireContext(),
//            navController!!,
//            this::
//        )
//        FragmentSearchProductRv?.adapter = searchAdapter
//        dataBase = DataBaseKotlin.getInstance(activity!!)
//        root!!.fragment_home_search_Et.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
//            Log.d("actionId", "OnKeyListener: " + keyCode + " - " + KeyEvent.KEYCODE_ENTER)
//            if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                searchLocation(root!!)
//                return@OnKeyListener true
//            }
//            false
//        })
    }

    private fun searchLocation(root: View) {
        val searchText = binding.fragmentHomeSearchEt.text.toString()

    }

    private fun initData(root: View) {
        navController = Navigation.findNavController(root)
        FragmentSearchProductRv=binding.FragmentSearchProductRv
        homeCycleActivity?.hidenav()
        onClick()
    }

    fun onClick() {
            binding.backBtn.setOnClickListener{ super.onBack()}
            binding.fragmentSearchProductDeleteAllBtnProducts.setOnClickListener{ }
    }
}