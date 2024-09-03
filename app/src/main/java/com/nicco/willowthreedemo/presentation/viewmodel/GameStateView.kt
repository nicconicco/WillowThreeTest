package com.nicco.willowthreedemo.presentation.viewmodel

import com.nicco.willowthreedemo.presentation.model.UserWillowUi

sealed interface GameStateView {
    data class SuccessList(val list: List<UserWillowUi>) : GameStateView
    data class ShowDialogGameEnded(val correctAnswers: Int) : GameStateView
    object Error : GameStateView
    object Idle : GameStateView
}