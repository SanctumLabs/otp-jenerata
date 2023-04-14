package com.sanctumlabs.otp.core.interactor

interface UseCase<in T, out R> {
    fun execute(request: T): R
}
