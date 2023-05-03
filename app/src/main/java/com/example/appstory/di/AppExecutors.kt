package com.example.appstory.di

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors {
    val diskIO: Executor = Executors.newSingleThreadExecutor()
}