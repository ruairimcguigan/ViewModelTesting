package com.aquidigital.viewmodeltesting.api

import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {

    @GET("user/{user}/repos")
    fun getRepos(@Path("user") usernamer: String): Maybe<List<Repo>>
}