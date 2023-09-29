package com.ma.rovers.usecases

interface IUseCaseResult {
    val successful: Boolean
    val errorMessage: String
}