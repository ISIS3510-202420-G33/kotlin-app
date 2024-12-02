package com.artlens.data.models

data class CommentRequest(
    val content: String,
    val date: String,
    val artwork: Int,
    val user: Int
)

data class CommentResponse(
    val pk: Int,
    val fields: CommentFields
)

data class CommentFields(
    val content: String,
    val date: String,
    val artwork: Int,
    val user: Int
)
