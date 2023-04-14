package com.sanctumlabs.otp.core.interactor

interface CompletableUseCase<in T> : UseCase<T, Unit> {
    override fun execute(params: T)
}
