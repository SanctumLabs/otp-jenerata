package com.garihub.otp.core.interactor

@Suppress("UnnecessaryAbstractClass")
abstract class UseCase<in T, out R> {

    abstract fun execute(params: T? = null): R
}
