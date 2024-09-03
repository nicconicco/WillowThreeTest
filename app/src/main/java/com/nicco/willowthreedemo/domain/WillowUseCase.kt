package com.nicco.willowthreedemo.domain

import com.nicco.willowthreedemo.framework.util.ResultWillow
import com.nicco.willowthreedemo.presentation.model.UserWillowUi

interface WillowUseCase {
    suspend fun getUser(): ResultWillow<List<UserWillowUi>?, String>
}