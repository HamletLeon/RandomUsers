package com.hamletleon.randomusers.repositories

import android.content.Context
import com.hamletleon.randomusers.models.FavoriteUser
import com.hamletleon.randomusers.models.User
import com.hamletleon.randomusers.repositories.local.LocalServiceFactory
import com.hamletleon.randomusers.repositories.local.services.FavoriteUsersLocalService
import com.hamletleon.randomusers.repositories.local.services.RandomUsersLocalService
import com.hamletleon.randomusers.repositories.remote.RemoteServiceFactory
import com.hamletleon.randomusers.repositories.remote.services.RandomUsersRemoteService
import com.hamletleon.randomusers.utils.fromDate
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception
import java.util.*

class UsersRepository(ctx: Context) {
    private val localService: RandomUsersLocalService = LocalServiceFactory.getRandomUsersService(ctx)
    private val favoriteLocalService: FavoriteUsersLocalService = LocalServiceFactory.getFavoriteUsersService(ctx)
    private val remoteService: RandomUsersRemoteService? = RemoteServiceFactory.getRandomUsersService()
    private val sharedPreferences = ctx.getSharedPreferences(ctx.packageName, Context.MODE_PRIVATE)

    suspend fun getUsers(page: Int, limit: Int = 50): Deferred<List<User>> {
        val offset = (page - 1) * limit
        return getUsers(limit, offset, page)
    }

    suspend fun getUserById(id: Int): User {
        return withContext(Dispatchers.IO) {
            async { localService.getUserById(id) }
        }.await()
    }

    suspend fun getFavoriteUsers(): Deferred<List<User>> {
        return withContext(Dispatchers.IO) {
            async { favoriteLocalService.getAllFavoriteUsers() }
        }
    }

    suspend fun saveFavoriteUser(userId: Int): Boolean {
        val favoriteUser = FavoriteUser()
        favoriteUser.userId = userId
        favoriteUser.registeredAt = Calendar.getInstance().time.fromDate() ?: ""
        return withContext(Dispatchers.Default) { favoriteLocalService.upsert(favoriteUser) }
    }

    private suspend fun getUsers(limit: Int, offset: Int, page: Int, onError: (String) -> Unit = {}): Deferred<List<User>> {
        return withContext(Dispatchers.IO) {
            val localCount = localService.getUsersCount()

            if ((localCount - offset) >= limit) {
                async { localService.getUsers(limit, offset) }
            } else {
                val parameters = mutableMapOf<String, String>()
                parameters["page"] = page.toString()
                parameters["results"] = limit.toString()
                getSeed()?.let { parameters["seed"] = it }
                try {
                    val response = remoteService?.getRandomUsers(parameters)?.await()
                    if (response?.isSuccessful == true) {
                        val randomUserResponse = response.body()

                        val seed = randomUserResponse?.info?.seed ?: ""
                        saveSeed(seed)

                        val users = randomUserResponse?.results?.map { User(it) }?: emptyList()
                        withContext(Dispatchers.Default) { localService.upsert(users) }
                        async { localService.getUsers(limit, offset) }
                    } else {
                        onError("${response?.code()}: ${response?.errorBody().toString()}")
                        async { emptyList<User>() }
                    }
                } catch (e: Exception) {
                    if (e is HttpException){
                        onError("${e.code()}: ${e.message()}")
                        async { emptyList<User>() }
                    }
                    else{
                        onError(e.localizedMessage)
                        async { emptyList<User>() }
                    }
                }
            }
        }
    }

    private fun saveSeed(seed: String) = sharedPreferences?.edit()?.putString("SEED", seed)?.apply()
    private fun getSeed(): String? = sharedPreferences?.getString("SEED", null)
}