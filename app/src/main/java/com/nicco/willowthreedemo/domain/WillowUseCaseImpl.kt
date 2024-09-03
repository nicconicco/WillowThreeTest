package com.nicco.willowthreedemo.domain

import com.nicco.willowthreedemo.framework.util.ResultWillow
import com.nicco.willowthreedemo.framework.util.flow
import com.nicco.willowthreedemo.presentation.model.UserWillowUi

internal class WillowUseCaseImpl(
    private val userRepository: UserRepository
) : WillowUseCase {
    override suspend fun getUser(): ResultWillow<List<UserWillowUi>, String> {
        val result = userRepository.getUser()

        return result.flow(
            { listResponse ->
                val list = listResponse?.map { user ->
                    UserWillowUi(
                        user.firstName ?: "",
                        user.headshot.url
                    )
                }.orEmpty()

                ResultWillow.Success(list)
            }, {
                ResultWillow.Failure("Error!")
            })
    }
}

/**
 *
 * Activity -> Viewmodel -> Usecase -> Repository -> Service
 * Service -> Repository -> UseCase -> ViewModel -> Activity
 *
 */