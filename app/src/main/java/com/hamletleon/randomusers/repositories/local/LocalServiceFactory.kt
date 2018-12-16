package com.hamletleon.randomusers.repositories.local

import android.content.Context
import com.hamletleon.randomusers.repositories.local.core.DatabaseContext
import com.hamletleon.randomusers.repositories.local.services.RandomUsersLocalService

object LocalServiceFactory {
    fun getRandomUsersService(ctx: Context) : RandomUsersLocalService {
        return DatabaseContext.getInstance(ctx).getRandomUserLocalService()
    }
}