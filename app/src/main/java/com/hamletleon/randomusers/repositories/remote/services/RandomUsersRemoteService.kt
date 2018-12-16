package com.hamletleon.randomusers.repositories.remote.services

import com.hamletleon.randomusers.dtos.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUsersRemoteService {
    @GET("api?nat=us,es,fr") // US, ES, FR nationalities to avoid unknown characters
    fun getRandomUsers(@Query("page") page: Int, @Query("seed") seed: String,
                       @Query("results") limit: Int = 50) : Response<UserDto>
}