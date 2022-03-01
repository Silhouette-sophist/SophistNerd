package com.example.sophistnerd.component.fragment

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.sophistnerd.data.UnsplashPhotoWithStatus
import com.example.sophistnerd.databinding.FragmentItemBinding
import com.example.sophistnerd.service.DownloadImageImpl
import com.example.sophistnerd.util.saveBitmap
import com.example.sophistnerd.util.showSnackbarMessage
import kotlinx.coroutines.*

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 */
class ItemRecyclerViewAdapter(
    private val values: List<UnsplashPhotoWithStatus>
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
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
        holder.itemImageView.setOnLongClickListener {
            it as ImageView
            AlertDialog.Builder(it.context)
                .setMessage("是否保存图片？")
                .setPositiveButton("下载") { p0, p1 ->
                    coroutineScope.launch {
                        saveBitmap(it)
                        showSnackbarMessage(it, "下载完成")
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

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemImageView: ImageView = binding.itemImageView
        val authorName: TextView = binding.authorName
        val createAt: TextView = binding.createAt
        val favorCounts: TextView = binding.favorCounts
    }

    //由于RecyclerView的onBindViewHolder()方法，只有在getItemViewType()返回类型不同时才会调用，
    // 这点是跟ListView的getView()方法不同的地方，所以如果想要每次都调用onBindViewHolder()刷新item数据，
    // 就要重写getItemViewType()，让其返回position，否则很容易产生数据错乱的现象。
    override fun getItemViewType(position: Int): Int {
        return position
    }

    suspend fun downloadImage(url: String): Bitmap? {
        val bitmap = DownloadImageImpl().download(url)
        return bitmap
    }

}