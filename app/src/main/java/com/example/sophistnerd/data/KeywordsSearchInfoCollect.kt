package com.example.sophistnerd.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import kotlinx.android.parcel.Parcelize

//本地数据库对象
@Entity(tableName = "keywords_search_info_table")
@Parcelize
data class LocalKeywordsSearchInfo(
    @ColumnInfo(name = "keywords") val keywords: String,
    //@Ignore val searchResults: List<UnsplashPhoto>,
    @ColumnInfo(name = "search_time") val searchTime: Long,
    //注意这里有个误区，这里的自增是针对存储到数据库中的数据，当你构造对象时，这里肯定为0, 但是存储到数据库中会自增！！！
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "search_id") val searchId: Int = 0
) : Parcelable

/*
@Entity(tableName = "unsplash_photo_info_table")
@Parcelize
data class LocalUnsplashPhoto(
    val id: String,
    val created_at: String,
    val width: Int,
    val height: Int,
    val color: String? = "#000000",
    val likes: Int,
    val description: String?,
    @Ignore val urls: LocalUnsplashUrls,
    @Ignore val links: LocalUnsplashLinks,
    @Ignore val user: LocalUnsplashUser
) : Parcelable

@Entity(tableName = "unsplash_user_info_table")
@Parcelize
data class LocalUnsplashUser(
    val id: String,
    val username: String,
    val name: String,
    val portfolio_url: String?,
    val bio: String?,
    val location: String?,
    val total_likes: Int,
    val total_photos: Int,
    val total_collection: Int,
    @Ignore val profile_image: LocalUnsplashUrls,
    @Ignore val links: LocalUnsplashLinks
) : Parcelable

@Entity(tableName = "unsplash_links_info_table")
@Parcelize
data class LocalUnsplashLinks(
    val self: String,
    val html: String,
    val photos: String?,
    val likes: String?,
    val portfolio: String?,
    val download: String?,
    val download_location: String?
) : Parcelable

@Entity(tableName = "unsplash_urls_info_table")
@Parcelize
data class LocalUnsplashUrls(
    val thumb: String?,
    val small: String,
    val medium: String?,
    val regular: String?,
    val large: String?,
    val full: String?,
    val raw: String?
) : Parcelable
*/
