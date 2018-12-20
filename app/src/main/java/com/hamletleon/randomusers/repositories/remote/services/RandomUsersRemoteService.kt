package com.hamletleon.randomusers.repositories.remote.services

import com.hamletleon.randomusers.dtos.RandomUserDto
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RandomUsersRemoteService {
    @GET("api?nat=us,es,fr") // US, ES, FR nationalities to avoid unknown characters
    fun getRandomUsers(@QueryMap parameters: Map<String, String>) : Deferred<Response<RandomUserDto>>
}