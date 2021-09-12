package com.hicham.wcstoreapp.data.source

import com.hicham.wcstoreapp.data.source.network.WordpressApi
import javax.inject.Inject

class PostsRepository @Inject constructor(private val api: WordpressApi) {
}