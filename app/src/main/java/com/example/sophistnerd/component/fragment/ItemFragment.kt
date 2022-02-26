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
import com.example.sophistnerd.component.jetpack.MainViewModel
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private var columnCount = 1

    private lateinit var viewModel: MainViewModel
    private val dataSource = ArrayList<UnsplashPhoto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取activity的viewmodel
        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
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
                viewModel.imageSource.observeForever {
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