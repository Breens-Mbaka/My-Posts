package com.example.retrofittutorial

import com.example.retrofittutorial.model.Post

data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val errorMessage: String = "",
    val searchQuery: String = "",
    val newPostTitle: String = "",
    val newPostBody: String = "",
    val updatedPostFullyTitle: String = "",
    val updatedPostFullyBody: String = "",
    val updatedPostPartiallyTitle: String = "",
    val updatedPostPartiallyBody: String = "",
    val showCreatePostDialog: Boolean = false,
    val showUpdatePostFullyDialog: Boolean = false,
    val showUpdatePostPartiallyDialog: Boolean = false,
    val selectedPost: Post? = null,
    val showActionsMenu: Boolean = false
)