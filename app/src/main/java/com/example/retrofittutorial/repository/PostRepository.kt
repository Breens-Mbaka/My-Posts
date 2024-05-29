package com.example.retrofittutorial.repository

import com.example.retrofittutorial.model.Post
import com.example.retrofittutorial.model.PostBody
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

    suspend fun getPostById(id: Int): Resource<Post> {
        return withContext(Dispatchers.IO) {
            try {
                val response = postService.getPostById(id = id)

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

    suspend fun createPost(title: String, body: String): Resource<Post> {
        return withContext(Dispatchers.IO) {
            try {
                val post = PostBody(
                    title = title,
                    body = body,
                    userId = 7
                )
                val response = postService.createPost(post = post)

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