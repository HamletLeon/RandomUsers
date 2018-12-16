package com.hamletleon.randomusers.repositories.remote

import com.hamletleon.randomusers.repositories.remote.core.RetrofitClient
import com.hamletleon.randomusers.repositories.remote.services.RandomUsersRemoteService

object RemoteServiceFactory {
    fun getRandomUsersService() = getRetrofit()?.create(RandomUsersRemoteService::class.java)
    private fun getRetrofit() = RetrofitClient.getClient("https://randomuser.me")
}