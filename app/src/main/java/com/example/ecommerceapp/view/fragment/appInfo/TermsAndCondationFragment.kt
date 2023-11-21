package com.example.ecommerceapp.view.fragment.appInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentSplashPageBinding
import com.example.ecommerceapp.databinding.FragmentTermsAndconditionBinding
import com.example.ecommerceapp.view.fragment.BaseFragment


class TermsAndCondationFragment : BaseFragment() {
    private lateinit var binding: FragmentTermsAndconditionBinding
    private var root: View?=null
    var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTermsAndconditionBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        navController = Navigation.findNavController(root!!)
        initData()
    }

    private fun initData() {
        homeCycleActivity?.hidenav()
        onClick()
    }

    fun onClick() {
        binding.backBtu.setOnClickListener {
            super.onBack()
        }
    }
}