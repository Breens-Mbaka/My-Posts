package com.example.retrofittutorial.service

interface PostService {
    suspend fun getPosts()

    suspend fun getPostById()

    suspend fun createPost()

    suspend fun updatePostFully()

    suspend fun updatePostPartially()

    suspend fun deletePost()
}