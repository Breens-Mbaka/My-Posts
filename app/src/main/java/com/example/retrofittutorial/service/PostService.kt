package com.example.retrofittutorial.service

import com.example.retrofittutorial.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface PostService {
    @GET("/posts")
    suspend fun getPosts(): Response<List<Post>>
}