package com.example.retrofittutorial.model

data class Post(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)

val posts = listOf(
    Post(
        "Testing post description",
        2,
        "Testing post description",
        4
    ),
    Post(
        "Testing post description",
        2,
        "Testing post description",
        4
    ),
    Post(
        "Testing post description",
        2,
        "Testing post description",
        4
    ),
    Post(
        "Testing post description",
        2,
        "Testing post description",
        4
    ),
    Post(
        "Testing post description",
        2,
        "Testing post description",
        4
    )
)