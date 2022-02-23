package com.example.githubsearch.data.model

import android.view.View
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName

@Keep
data class SearchDataResponse(
    @SerializedName("total_count")
        val total_count: Int,
    @SerializedName("incomplete_results")
        val incomplete_results: Boolean,
    @SerializedName("items")
        val items: List<Items>,
) {
    @Keep
    data class Items(
        @SerializedName("id")
        val id: Int,
        @SerializedName("avatar_url")
        val avatar_url: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("score")
        val score: Int,
        @SerializedName("login")
        val login: String,
        @SerializedName("repos_url")
        val repos_url: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("company")
        val company: String,
        @SerializedName("location")
        val location: String,
        @SerializedName("twitter_username")
        val twitter_username: String,
        @SerializedName("public_repos")
        val public_repos: Int,
        @SerializedName("followers")
        val followers: Int,
        @SerializedName("following")
        val following: Int
    )
}

@BindingAdapter("imageUrl")
fun loadImage(view: View,
              imageUrl: String?) {
    val image: ImageView = view as ImageView
    Glide.with(image.context)
        .load(imageUrl)
        .into(image)
}
