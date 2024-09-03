package com.nicco.willowthreedemo.domain

import com.nicco.willowthreedemo.data.model.UserResponse
import com.nicco.willowthreedemo.framework.util.ResultWillow

interface UserRepository {
    suspend fun getUser(): ResultWillow<List<UserResponse>?, String>
}