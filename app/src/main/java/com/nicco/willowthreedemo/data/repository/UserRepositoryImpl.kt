package com.nicco.willowthreedemo.data.repository

import com.nicco.willowthreedemo.data.model.UserResponse
import com.nicco.willowthreedemo.domain.UserRepository
import com.nicco.willowthreedemo.framework.ApiService
import com.nicco.willowthreedemo.framework.util.ResultWillow

class UserRepositoryImpl(
    private val apiService: ApiService
) : UserRepository {

    override suspend fun getUser(): ResultWillow<List<UserResponse>?, String> {

        /**
         * datasourceLocal, datasourceRemote
         *
         * if(datasourceLocal.getContent() == null)
         * val result = datasourceRemote.getContent
         * datasourceLocal.setRoom(result)
         *  else {
         *  datasourceLocal.getLocalRoom()
         *  }
         *
         *  GameApp.saveLocalInformation()
         *  GameApp.isLocalInformation()
         *
         */
        val result = apiService.getUsers()

        return if (
            result.isSuccessful && result.body()?.isNotEmpty() == true
        ) {
            ResultWillow.Success(result.body())
        } else {
            ResultWillow.Failure(result.message())
        }
    }
}