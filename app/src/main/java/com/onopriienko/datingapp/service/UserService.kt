package com.onopriienko.datingapp.service

import com.onopriienko.datingapp.model.LoginDto
import com.onopriienko.datingapp.model.ReactToUserDto
import com.onopriienko.datingapp.model.RegisterUserDto
import com.onopriienko.datingapp.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("/users/cities")
    suspend fun getAvailableCities(): Response<List<String>>

    @POST("/users/register")
    suspend fun registerUser(@Body user: RegisterUserDto): Response<UserDto>

    @POST("/users/react")
    suspend fun react(@Body reaction: ReactToUserDto): Response<Unit>

    @GET("/users/{id}/next")
    suspend fun findNextToReactTo(@Path("id") userId: String): Response<UserDto>

    @GET("/users/{id}/mutualLikes")
    suspend fun findMutual(@Path("id") userId: String): Response<List<UserDto>>

    @POST("/users/login")
    suspend fun login(@Body loginRequest: LoginDto): Response<UserDto>
}
