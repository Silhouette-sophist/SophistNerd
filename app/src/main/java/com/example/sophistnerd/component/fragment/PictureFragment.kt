package com.example.sophistnerd.component.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.sophistnerd.R
import com.example.sophistnerd.component.jetpack.MainSavedStateViewModel
import com.example.sophistnerd.util.*
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

    //注意SupervisorJob+CoroutineExceptionHandler一起使用，才不会导致子协程崩溃影响到父协程！！！
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    })

    /**
     * 将视图压入显示
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }

    /**
     * 绑定视图上的控件
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initLiveData()
    }

    private fun initView() {

        //会拦截掉Activity最终对左滑和右滑的处理
        imageView = requireActivity().findViewById<ImageView>(R.id.image_view).also { it ->
            it.setOnLongClickListener { image ->
                AlertDialog.Builder(it.context)
                    .setMessage("是否保存图片？")
                    .setPositiveButton("下载") { p0, p1 ->
                        coroutineScope.launch {
                            saveImageViewBitmap(it)
                            showSnackbarMessage(it, "下载完成")
                        }
                    }
                    .setNegativeButton("取消") { p0, p1 -> }
                    .show()
                false
            }
        }
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
            } else {
                showSnackbarMessage(it, "请输入搜索英文关键词")
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
        savedStateViewModel.indexLiveData.observeForever { currentPhotoIndex ->
            if (currentPhotoIndex < savedStateViewModel.imageSource.value?.size ?: 0) {
                val specificUpsplashPhoto =
                    savedStateViewModel.imageSource.value?.get(currentPhotoIndex)
                if (specificUpsplashPhoto != null) {
                    if (specificUpsplashPhoto.bitmap != null) {
                        imageView.setImageBitmap(specificUpsplashPhoto.bitmap)
                    } else {
                        specificUpsplashPhoto.unsplashPhoto.urls.regular?.let {
                            coroutineScope.launch {
                                val imageBitmap = withContext(Dispatchers.IO) {
                                    savedStateViewModel.downloadImage(it)
                                }
                                imageBitmap?.let {
                                    imageView.setImageBitmap(imageBitmap)
                                    specificUpsplashPhoto.bitmap = imageBitmap
                                }
                            }
                            //picasso包替代上面的使用
                            //Picasso.get().load(it).into(imageView);
                        }
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