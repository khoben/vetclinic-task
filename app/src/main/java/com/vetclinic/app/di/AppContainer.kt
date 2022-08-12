package com.vetclinic.app.di

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.vetclinic.app.App
import com.vetclinic.app.R
import com.vetclinic.app.common.fetchimage.*
import com.vetclinic.app.common.network.HttpService
import com.vetclinic.app.data.cloud.*
import com.vetclinic.app.navigation.Navigation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val Activity.di: AppContainer get() = (application as App).appContainer
val Fragment.di: AppContainer get() = requireActivity().di

class AppContainer(appContext: Context) {

    private val executorService: ExecutorService by lazy {
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    }

    private fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

    val fetchImage: FetchImage by lazy {
        FetchImage.Base(
            placeholder = R.drawable.ic_launcher_foreground,
            okHttpClient = provideOkHttpClient(),
            memoryCache = ImageCache.MemoryImageCache(),
            persistenceCache = ImageCache.FileImageCache(
                appContext,
                KeyHash.MD5(),
                ImageDecoder.FileDecoder()
            ),
            downSampleImage = DownSampleStreamImage.Base(CreateTempFile.Cache(appContext)),
            executorService = executorService
        )
    }

    val configHttpService: HttpService<ConfigCloud> by lazy {
        ConfigHttpService(
            CONFIG_URL,
            UnmarshallConfigResponse(),
            provideOkHttpClient()
        )
    }

    val petHttpService: HttpService<List<PetCloud>> by lazy {
        PetHttpService(
            PETS_URL,
            UnmarshallPetListResponse(),
            provideOkHttpClient()
        )
    }

    val navigation: Navigation.Component by lazy { Navigation.Base() }

    companion object {
        private const val CONFIG_URL = "https://jsonkeeper.com/b/PN84"
        private const val PETS_URL = "https://jsonkeeper.com/b/7GTA"
    }
}