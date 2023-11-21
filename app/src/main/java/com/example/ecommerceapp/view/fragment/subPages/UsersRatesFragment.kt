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
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.example.ecommerceapp.databinding.FragmentUsersRatesBinding
import com.example.ecommerceapp.view.fragment.BaseFragment

class UsersRatesFragment : BaseFragment() {
    private lateinit var binding: FragmentUsersRatesBinding
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
        binding = FragmentUsersRatesBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        navController = Navigation.findNavController(root!!)
        onClick(root!!)
    }

    fun onClick(root: View) {
        binding.backBtn.setOnClickListener{
            super.onBack()
        }
    }
}