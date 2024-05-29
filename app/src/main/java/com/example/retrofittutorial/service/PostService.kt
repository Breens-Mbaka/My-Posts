package com.example.retrofittutorial.service

import com.example.retrofittutorial.model.Post
import com.example.retrofittutorial.model.PostBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostService {
    @GET("/posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("/posts/{id}")
    suspend fun getPostById(
        @Path("id") id: Int
    ): Response<Post>

    @POST("/posts")
    suspend fun createPost(
        @Body post: PostBody
    ): Response<Post>
}