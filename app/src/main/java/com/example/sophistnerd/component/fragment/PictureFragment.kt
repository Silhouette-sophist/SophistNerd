package com.example.sophistnerd.component.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.sophistnerd.R
import com.example.sophistnerd.component.jetpack.MainSavedStateViewModel
import com.example.sophistnerd.util.partial1
import com.example.sophistnerd.util.showSnackbarMessage
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 * Use the [PictureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PictureFragment : Fragment() {

    private val savedStateViewModel by lazy {
        //获取activity的viewmodel
        ViewModelProviders.of(requireActivity()).get(MainSavedStateViewModel::class.java)
    }
    private lateinit var imageView: ImageView
    private lateinit var searchKeywords: EditText
    private lateinit var lifecycleObserver: LifecycleObserver

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        initLiveData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }

    private fun initView() {

        imageView = requireActivity().findViewById<ImageView>(R.id.image_view)
        searchKeywords = requireActivity().findViewById<EditText>(R.id.search_keywords)

        requireActivity().findViewById<Button>(R.id.search_image).setOnClickListener {
            //隐藏软键盘
            val manager: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val text = searchKeywords.text
            if (text != null && text.trim().isNotEmpty()) {
                coroutineScope.launch {
                    //1.偏函数功能
                    savedStateViewModel.search(text.trim().toString(), ::showSnackbarMessage.partial1(it))
                }
            }
        }

        requireActivity().findViewById<Button>(R.id.previous_image).setOnClickListener {
            //savedStateViewModel.previous(::showSnackbarMessage.partial1(it))
            //2.匿名函数形式，等效偏函数功能
            savedStateViewModel.previous(fun(msg : String){
                showSnackbarMessage(it, msg = msg)
            })
        }

        requireActivity().findViewById<Button>(R.id.next_image).setOnClickListener {
            //savedStateViewModel.next(::showSnackbarMessage.partial1(it))
            //3.lambda表达式形式，等效偏函数功能
            savedStateViewModel.next { msg : String ->
                showSnackbarMessage(it, msg)
            }
        }

        requireActivity().findViewById<Button>(R.id.goto_page).setOnClickListener {
            findNavController().navigate(R.id.action_pictureFragment_to_itemFragment)
        }
    }

    private fun initLiveData() {
        savedStateViewModel.currentImage.observeForever {
            it.urls.regular?.let {
                coroutineScope.launch {
                    val imageBitmap = withContext(Dispatchers.IO) {
                        savedStateViewModel.downloadImage(it)
                    }
                    imageBitmap?.let {
                        imageView.setImageBitmap(imageBitmap)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycle.removeObserver(lifecycleObserver)
    }
}