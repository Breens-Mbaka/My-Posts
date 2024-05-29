package com.example.retrofittutorial.repository

import com.example.retrofittutorial.model.Post
import com.example.retrofittutorial.model.PostBody
import com.example.retrofittutorial.service.Resource

class PostRepository {
    fun getPosts(): Resource<List<Post>> {
        return Resource.Success(emptyList())
    }

    fun getPostById(id: Int): Resource<Post> {
        return Resource.Success(null)
    }

    suspend fun createPost(title: String, body: String): Resource<Post> {
        return Resource.Success(null)
    }

    suspend fun updatePostFully(id: Int, post: PostBody): Resource<Post> {
        return Resource.Success(null)
    }

    suspend fun updatePostPartially(id: Int, post: PostBody): Resource<Post> {
        return Resource.Success(null)
    }

    suspend fun deletePost(id: Int): Resource<Unit> {
        return Resource.Success(null)
    }
}