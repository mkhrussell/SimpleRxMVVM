package com.kamrul.simplerxmvvm

import android.content.Context
import com.kamrul.simplerxmvvm.persistence.UserDao
import com.kamrul.simplerxmvvm.persistence.UsersDatabase
import com.kamrul.simplerxmvvm.ui.UserViewModel

object Injection {

    fun provideUserDataSource(context: Context): UserDao {
        val database = UsersDatabase.getInstance(context)
        return database.userDao()
    }

    fun provideViewModelFactory(context: Context): UserViewModel.Factory {
        val dataSource = provideUserDataSource(context)
        return UserViewModel.Factory(dataSource)
    }
}