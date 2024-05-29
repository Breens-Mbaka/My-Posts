package com.example.retrofittutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofittutorial.model.Post
import com.example.retrofittutorial.repository.PostRepository
import com.example.retrofittutorial.service.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {
    private val postRepository = PostRepository()

    private val _postsUiState = MutableStateFlow(PostsUiState())
    val postsUiState: StateFlow<PostsUiState> = _postsUiState.asStateFlow()

    fun getPosts() {
        viewModelScope.launch {
            _postsUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            when (val result = postRepository.getPosts()) {
                is Resource.Success -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            posts = result.data ?: emptyList()
                        )
                    }
                }

                is Resource.Error -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                                ?: "Something went wrong, please try again"
                        )
                    }
                }

                else -> {
                    postsUiState
                }
            }
        }
    }

    fun getPostById() {
        viewModelScope.launch {
            _postsUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            when (val result =
                postRepository.getPostById(id = postsUiState.value.searchQuery.toInt())) {
                is Resource.Success -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            posts = if (result.data != null) listOf(result.data) else it.posts
                        )
                    }
                }

                is Resource.Error -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                                ?: "Something went wrong, please try again"
                        )
                    }
                }

                else -> {
                    postsUiState
                }
            }
        }
    }

    fun createPost() {
        viewModelScope.launch {
            _postsUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            when (val result =
                postRepository.createPost(
                    title = postsUiState.value.postTitle,
                    body = postsUiState.value.postBody
                )) {
                is Resource.Success -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            posts = if (result.data != null) listOf(result.data) else it.posts,
                            postTitle = "",
                            postBody = "",
                            showCreatePostDialog = false
                        )
                    }
                }

                is Resource.Error -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                                ?: "Something went wrong, please try again",
                            showCreatePostDialog = false
                        )
                    }
                }

                else -> {
                    postsUiState
                }
            }
        }
    }

    fun setPostTitle(title: String) {
        _postsUiState.update {
            it.copy(
                postTitle = title
            )
        }
    }

    fun setPostBody(body: String) {
        _postsUiState.update {
            it.copy(
                postBody = body
            )
        }
    }

    fun showCreatePostDialog(showCreatePostDialog: Boolean) {
        _postsUiState.update {
            it.copy(
                showCreatePostDialog = showCreatePostDialog
            )
        }
    }

    fun setSearchQuery(query: String) {
        _postsUiState.update {
            it.copy(
                searchQuery = query
            )
        }
    }

    fun refresh() {
        _postsUiState.value = PostsUiState(
            posts = emptyList(),
            errorMessage = "",
            searchQuery = ""
        )

        getPosts()
    }

    init {
        getPosts()
    }
}

data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val errorMessage: String = "",
    val searchQuery: String = "",
    val postTitle: String = "",
    val postBody: String = "",
    val showCreatePostDialog: Boolean = false
)