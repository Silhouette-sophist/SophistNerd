package com.example.sophistnerd.component.fragment

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.sophistnerd.component.jetpack.MainSavedStateViewModel
import com.example.sophistnerd.data.UnsplashPhotoWithStatus
import com.example.sophistnerd.databinding.FragmentItemViewBinding
import com.example.sophistnerd.service.DownloadImageImpl
import com.example.sophistnerd.util.showSnackbarMessage
import com.unsplash.pickerandroid.photopicker.data.UnsplashUrls
import kotlinx.coroutines.*
import kotlin.collections.HashMap

/**
 * [RecyclerView.Adapter] that can display.
 */
class ItemRecyclerViewAdapter(
    private val values: List<UnsplashPhotoWithStatus>,
    private val savedStateViewModel: MainSavedStateViewModel
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    //注意SupervisorJob+CoroutineExceptionHandler一起使用，才不会导致子协程崩溃影响到父协程！！！
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    })

    /**
     * 注意每一个layout文件都生成了一个binding对象，按照布局名反写并加上binding后缀!!!!
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if (item.bitmap != null) {
            holder.itemImageView.setImageBitmap(item.bitmap)
        } else  {
            coroutineScope.launch {
                val bitmap = withContext(Dispatchers.IO) {
                    item.unsplashPhoto.urls.regular?.let { downloadImage(it) }
                }
                //下载后需要记录
                item.bitmap = bitmap
                holder.itemImageView.setImageBitmap(bitmap)
            }
        }
        holder.itemImageView.setOnLongClickListener { it ->
            val sourceUrlMap = getValidUrlMap(values[holder.absoluteAdapterPosition].unsplashPhoto.urls)
            val sourceUrlKeyArray = sourceUrlMap.keys.toTypedArray()
            it as ImageView
            AlertDialog.Builder(it.context)
                .setTitle("选择要保存的源图类型？")
                .setItems(sourceUrlKeyArray) { p0, p1 ->
                    coroutineScope.launch {
                        val validUrl = sourceUrlMap[sourceUrlKeyArray[p1]]
                        validUrl?.let { url ->
                            savedStateViewModel.saveUrlImage(url, sourceUrlKeyArray[p1], values[holder.absoluteAdapterPosition].unsplashPhoto)
                        }
                        showSnackbarMessage(it, "下载${sourceUrlKeyArray[p1]}完成")
                    }
                }
                .setNegativeButton("取消") { p0, p1 -> }
                .show()
            true
        }
        holder.authorName.text = item.unsplashPhoto.user.name
        holder.createAt.text = item.unsplashPhoto.created_at
        holder.favorCounts.text = "${item.unsplashPhoto.likes}"
    }

    /**
     * 解析unsplashUrls中的有效字段
     */
    private fun getValidUrlMap(unsplashUrls: UnsplashUrls?) : Map<String, String> {
        val result = HashMap<String, String>()
        unsplashUrls?.let { urls ->
            urls.regular?.let {
                result.put("regular", it)
            }
            urls.medium?.let {
                result.put("medium", it)
            }
            urls.full?.let {
                result.put("full", it)
            }
            urls.large?.let {
                result.put("large", it)
            }
            urls.raw?.let {
                result.put("raw", it)
            }
            urls.small?.let {
                result.put("small", it)
            }
            urls.thumb?.let {
                result.put("thumb", it)
            }
        }
        return result
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemImageView: ImageView = binding.itemImageView
        val authorName: TextView = binding.authorName
        val createAt: TextView = binding.createAt
        val favorCounts: TextView = binding.favorCounts
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //由于RecyclerView的onBindViewHolder()方法，只有在getItemViewType()返回类型不同时才会调用，
    // 这点是跟ListView的getView()方法不同的地方，所以如果想要每次都调用onBindViewHolder()刷新item数据，
    // 就要重写getItemViewType()，让其返回position，否则很容易产生数据错乱的现象。
    override fun getItemViewType(position: Int): Int {
        return position
    }

    private suspend fun downloadImage(url: String): Bitmap? {
        return DownloadImageImpl().download(url)
    }

}