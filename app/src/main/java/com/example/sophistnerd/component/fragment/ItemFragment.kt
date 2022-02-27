package com.example.sophistnerd.component.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sophistnerd.R
import com.example.sophistnerd.component.jetpack.MainSavedStateViewModel
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlin.random.Random

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private val random = Random(10)
    private var columnCount = 2

    private val savedStateViewModel by lazy {
        //获取activity的viewmodel
        ViewModelProviders.of(requireActivity()).get(MainSavedStateViewModel::class.java)
    }
    private val dataSource = ArrayList<UnsplashPhoto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //图片列数增加随机
        val next = random.nextInt(100)
        columnCount = (next % 3) + 1
        println("columnCount $columnCount $next")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = ItemRecyclerViewAdapter(dataSource)
                savedStateViewModel.imageSource.observeForever {
                    dataSource.clear()
                    dataSource.addAll(it)
                    adapter?.notifyDataSetChanged()

                    println("dataSource ${dataSource.size}")
                }
            }
        }
        return view
    }
}