package com.hicham.wpapp.data.source

import com.hicham.wpapp.data.source.network.WordpressApi
import javax.inject.Inject

class PostsRepository @Inject constructor(private val api: WordpressApi) {
}