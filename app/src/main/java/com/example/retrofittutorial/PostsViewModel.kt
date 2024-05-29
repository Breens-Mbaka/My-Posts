package com.example.retrofittutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofittutorial.model.Post
import com.example.retrofittutorial.model.PostBody
import com.example.retrofittutorial.repository.PostRepository
import com.example.retrofittutorial.service.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {
    private val postRepository = PostRepository()

    private val _postsUiState = MutableStateFlow(PostsUiState())
    val postsUiState: StateFlow<PostsUiState> = _postsUiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow: SharedFlow<UiEvents> = _eventFlow.asSharedFlow()

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

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = result.message ?: "Something went wrong, please try again"
                        )
                    )
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

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = result.message ?: "Something went wrong, please try again"
                        )
                    )
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
                    title = postsUiState.value.newPostTitle,
                    body = postsUiState.value.newPostBody
                )) {
                is Resource.Success -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            posts = if (result.data != null) listOf(result.data) else it.posts,
                            newPostTitle = "",
                            newPostBody = "",
                            showCreatePostDialog = false
                        )
                    }

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = "Post created successfully"
                        )
                    )
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

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = result.message ?: "Something went wrong, please try again"
                        )
                    )
                }

                else -> {
                    postsUiState
                }
            }
        }
    }

    fun updatePostFully() {
        viewModelScope.launch {
            _postsUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            val selectedPost = postsUiState.value.selectedPost
            val postBody = PostBody(
                title = postsUiState.value.updatedPostFullyTitle,
                body = postsUiState.value.updatedPostFullyBody,
                userId = selectedPost?.userId ?: -1
            )

            when (val result =
                postRepository.updatePostFully(
                    id = selectedPost?.id ?: -1,
                    post = postBody
                )) {
                is Resource.Success -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            posts = if (result.data != null) listOf(result.data) else it.posts,
                            updatedPostFullyTitle = "",
                            updatedPostFullyBody = "",
                            showUpdatePostFullyDialog = false,
                            showActionsMenu = false
                        )
                    }

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = "Post fully updated successfully"
                        )
                    )
                }

                is Resource.Error -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                                ?: "Something went wrong, please try again",
                            showUpdatePostFullyDialog = false,
                            showActionsMenu = false
                        )
                    }

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = result.message ?: "Something went wrong, please try again"
                        )
                    )
                }

                else -> {
                    postsUiState
                }
            }
        }
    }

    fun updatePostPartially() {
        viewModelScope.launch {
            _postsUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            val selectedPost = postsUiState.value.selectedPost
            val postBody = PostBody(
                title = postsUiState.value.updatedPostPartiallyTitle,
                body = postsUiState.value.updatedPostPartiallyBody,
                userId = null
            )

            when (val result =
                postRepository.updatePostPartially(
                    id = selectedPost?.id ?: -1,
                    post = postBody
                )) {
                is Resource.Success -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            posts = if (result.data != null) listOf(result.data) else it.posts,
                            updatedPostPartiallyTitle = "",
                            updatedPostPartiallyBody = "",
                            showUpdatePostPartiallyDialog = false,
                            showActionsMenu = false
                        )
                    }

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = "Post partially updated successfully"
                        )
                    )
                }

                is Resource.Error -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                                ?: "Something went wrong, please try again",
                            showUpdatePostPartiallyDialog = false,
                            showActionsMenu = false
                        )
                    }

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = result.message ?: "Something went wrong, please try again"
                        )
                    )
                }

                else -> {
                    postsUiState
                }
            }
        }
    }

    fun deletePost() {
        viewModelScope.launch {
            _postsUiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            val selectedPost = postsUiState.value.selectedPost

            when (val result =
                postRepository.deletePost(
                    id = selectedPost?.id ?: -1,
                )) {
                is Resource.Success -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            showActionsMenu = false
                        )
                    }

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = "Post deleted successfully"
                        )
                    )

                    getPosts()
                }

                is Resource.Error -> {
                    _postsUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                                ?: "Something went wrong, please try again",
                            showActionsMenu = false
                        )
                    }

                    _eventFlow.emit(
                        UiEvents.SnackBarEvent(
                            message = result.message ?: "Something went wrong, please try again"
                        )
                    )
                }

                else -> {
                    postsUiState
                }
            }
        }
    }

    fun showActionsMenu(showActionsMenu: Boolean) {
        _postsUiState.update {
            it.copy(
                showActionsMenu = showActionsMenu
            )
        }
    }

    fun setSelectedPost(post: Post) {
        _postsUiState.update {
            it.copy(
                selectedPost = post,
                showActionsMenu = true
            )
        }
    }

    fun showUpdatePostPartiallyDialog(showUpdatePostPartiallyDialog: Boolean) {
        _postsUiState.update {
            it.copy(
                showUpdatePostPartiallyDialog = showUpdatePostPartiallyDialog
            )
        }
    }

    fun setUpdatePostPartiallyTitle(title: String) {
        _postsUiState.update {
            it.copy(
                updatedPostPartiallyTitle = title
            )
        }
    }

    fun setUpdatePostPartiallyBody(body: String) {
        _postsUiState.update {
            it.copy(
                updatedPostPartiallyBody = body
            )
        }
    }

    fun showUpdatePostFullyDialog(showUpdatePostFullyDialog: Boolean) {
        _postsUiState.update {
            it.copy(
                showUpdatePostFullyDialog = showUpdatePostFullyDialog
            )
        }
    }

    fun setUpdatePostFullyTitle(title: String) {
        _postsUiState.update {
            it.copy(
                updatedPostFullyTitle = title
            )
        }
    }

    fun setUpdatePostFullyBody(body: String) {
        _postsUiState.update {
            it.copy(
                updatedPostFullyBody = body
            )
        }
    }

    fun setNewPostTitle(title: String) {
        _postsUiState.update {
            it.copy(
                newPostTitle = title
            )
        }
    }

    fun setNewPostBody(body: String) {
        _postsUiState.update {
            it.copy(
                newPostBody = body
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