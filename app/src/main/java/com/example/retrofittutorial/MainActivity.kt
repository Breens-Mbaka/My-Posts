package com.example.retrofittutorial

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.retrofittutorial.model.Post
import com.example.retrofittutorial.ui.theme.RetrofitTutorialTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PostsViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val postsUiState by viewModel.postsUiState.collectAsState()

            RetrofitTutorialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                viewModel.showCreatePostDialog(showCreatePostDialog = true)
                            },
                            shape = CircleShape
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                        }
                    },
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "My Posts", style = MaterialTheme.typography.titleLarge)
                            },
                            actions = {
                                IconButton(onClick = { viewModel.refresh() }) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    if (postsUiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(30.dp))
                        }
                    }

                    if (!postsUiState.isLoading && postsUiState.errorMessage.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = postsUiState.errorMessage,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { viewModel.getPosts() }) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = null,
                                    )
                                    Text(
                                        text = stringResource(id = R.string.retry),
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    if (postsUiState.showCreatePostDialog) {
                        CreatePostDialog(
                            postsUiState = postsUiState,
                            setPostTitle = { viewModel.setPostTitle(it) },
                            setPostBody = { viewModel.setPostBody(it) },
                            createPost = {
                                viewModel.createPost()
                            },
                            showCreatePostDialog = {
                                viewModel.showCreatePostDialog(it)
                            }
                        )
                    }

                    if (!postsUiState.isLoading && postsUiState.posts.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.padding(innerPadding),
                            contentPadding = PaddingValues(14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = postsUiState.searchQuery,
                                    onValueChange = {
                                        viewModel.setSearchQuery(it)
                                    },
                                    placeholder = {
                                        Text(text = stringResource(id = R.string.search_by_id))
                                    },
                                    shape = MaterialTheme.shapes.medium,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Search
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            if (postsUiState.searchQuery.isNotEmpty()) {
                                                return@KeyboardActions viewModel.getPostById()
                                            }
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Post ID is required",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    ),
                                    trailingIcon = {
                                        if (postsUiState.searchQuery.isNotEmpty()) {
                                            IconButton(onClick = { viewModel.refresh() }) {
                                                Icon(
                                                    imageVector = Icons.Filled.Clear,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                            items(postsUiState.posts) { post ->
                                PostComponent(
                                    post = post
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PostComponent(
    modifier: Modifier = Modifier,
    post: Post = Post("Testing Description", 1, "Testing Title", 3)
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = post.id.toString(),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostDialog(
    modifier: Modifier = Modifier,
    postsUiState: PostsUiState,
    setPostTitle: (String) -> Unit,
    setPostBody: (String) -> Unit,
    showCreatePostDialog: (Boolean) -> Unit,
    createPost: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { showCreatePostDialog(false) },
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(12.dp)
            ) {
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    value = postsUiState.postTitle,
                    onValueChange = {
                        setPostTitle(it)
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.post_title))
                    },
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                )

                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    value = postsUiState.postBody,
                    onValueChange = {
                        setPostBody(it)
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.post_body))
                    },
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showCreatePostDialog(false)
                        },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = stringResource(id = R.string.dismiss),
                        )
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { createPost() },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = stringResource(id = R.string.submit),
                        )
                    }
                }
            }
        }
    }
}