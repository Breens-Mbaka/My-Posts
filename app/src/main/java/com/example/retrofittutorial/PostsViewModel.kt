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

    init {
        getPosts()
    }
}

data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val errorMessage: String = ""
)