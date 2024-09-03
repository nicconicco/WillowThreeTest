package com.nicco.willowthreedemo.framework

import com.nicco.willowthreedemo.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("profiles")
    suspend fun getUsers(): Response<List<UserResponse>>
}

/**
 * {
 *  maxListUSer [6]
 *  actualPage: 0
 *  nextPage: 1
 * }
 *
 *  *  maxListUSer [6]
 *  *  actualPage: 1
 *  *  nextPage: 2
 *
 *  Response -> check if user has image if not -> put a default image
 *
 *  ModelUI <- Controller -> ModelResponse
 */