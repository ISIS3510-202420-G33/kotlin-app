package com.artlens.data.models

data class UserResponse (
    val pk: Int,
    val fields: UserFields
)

data class UserFields(
    val name: String,
    val userName: String,
    val email: String,
    val password: String
)

data class UserAuth(
    val userName: String,
    val password: String
)

sealed class CreateUserResponse {
    data class Success(val user: UserResponse) : CreateUserResponse()
    data class Failure(val error: String) : CreateUserResponse()
}