package com.example.sophistnerd.component.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sophistnerd.component.jetpack.MainSavedStateViewModel
import com.example.sophistnerd.data.UnsplashPhotoWithStatus
import com.example.sophistnerd.databinding.FragmentItemBinding
import kotlin.random.Random

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    companion object {
        val sRandom = Random(System.currentTimeMillis())
    }
    private var columnCount = 2
    private val savedStateViewModel by lazy {
        //获取activity的viewmodel
        ViewModelProviders.of(requireActivity()).get(MainSavedStateViewModel::class.java)
    }
    private val dataSource = ArrayList<UnsplashPhotoWithStatus>()
    //dataBinding
    private lateinit var binding : FragmentItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemBinding.inflate(inflater, container, false)

        //图片列数增加随机
        val next = sRandom.nextInt(100)
        columnCount = if (next < 80) 1 else 2

        // Set the adapter
        with(binding.root) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = ItemRecyclerViewAdapter(dataSource, savedStateViewModel)
            savedStateViewModel.imageSource.observeForever {
                dataSource.clear()
                dataSource.addAll(it)
                adapter?.notifyDataSetChanged()

                println("dataSource ${dataSource.size}")
            }
        }
        return binding.root
    }
}