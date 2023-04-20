package com.sanctumlabs.otp.core.interactor

interface CompletableUseCase<in T> : UseCase<T, Unit> {
    override suspend fun execute(request: T)
}
