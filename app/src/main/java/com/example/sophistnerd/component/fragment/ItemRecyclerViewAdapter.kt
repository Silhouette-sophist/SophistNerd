package com.example.sophistnerd.component.fragment

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sophistnerd.databinding.FragmentItemBinding
import com.example.sophistnerd.service.DownloadImageImpl
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlinx.coroutines.*

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 */
class ItemRecyclerViewAdapter(
    private val values: List<UnsplashPhoto>
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
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                item.urls.regular?.let { downloadImage(it) }
            }
            holder.itemImageView.setImageBitmap(bitmap)
        }
        holder.authorName.text = item.user.name
        holder.createAt.text = item.created_at
        holder.favorCounts.text = "${item.likes}"
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