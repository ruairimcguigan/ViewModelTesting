package com.aquidigital.viewmodeltesting.api

class ApiResult<T>(val status: Status,
                   val data: T? = null,
                   val err: Throwable? = null) {

    companion object {
        fun <T> success(data: T?) = ApiResult(Status.SUCCESS, data)
        fun <T> loading() = ApiResult<T>(Status.LOADING)
        fun <T> error(err: Throwable?) = ApiResult<T>(Status.ERROR, null, err)
    }

    enum class Status {
        SUCCESS, ERROR, LOADING
    }
}
