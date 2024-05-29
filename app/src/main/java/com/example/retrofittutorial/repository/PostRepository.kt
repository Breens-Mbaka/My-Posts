package com.example.retrofittutorial.repository

import com.example.retrofittutorial.model.Post
import com.example.retrofittutorial.retrofit.RetrofitClient.postService
import com.example.retrofittutorial.service.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository {
    suspend fun getPosts(): Resource<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = postService.getPosts()

                if (response.isSuccessful) {
                    Resource.Success(response.body())
                } else {
                    Resource.Error(response.message())
                }
            } catch (exception: Exception) {
                Resource.Error(exception.message ?: "An error occurred")
            }
        }
    }
}