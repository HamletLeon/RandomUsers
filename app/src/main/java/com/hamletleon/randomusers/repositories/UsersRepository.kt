package com.hamletleon.randomusers.repositories

import android.content.Context
import com.hamletleon.randomusers.repositories.local.LocalServiceFactory
import com.hamletleon.randomusers.repositories.local.services.RandomUsersLocalService
import com.hamletleon.randomusers.repositories.remote.RemoteServiceFactory
import com.hamletleon.randomusers.repositories.remote.services.RandomUsersRemoteService

class UsersRepository(ctx: Context) {
    private val localService: RandomUsersLocalService = LocalServiceFactory.getRandomUsersService(ctx)
    private val remoteService: RandomUsersRemoteService? = RemoteServiceFactory.getRandomUsersService()

    fun getAllUsers(page: Int = 1, limit: Int = 50) {
        val offset = (page - 1) * limit
        val localCount = localService.getUsersCount(limit, offset)

    }
}