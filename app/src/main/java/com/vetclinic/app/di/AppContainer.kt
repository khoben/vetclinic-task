package com.vetclinic.app.di

import android.content.Context
import com.vetclinic.app.R
import com.vetclinic.app.common.fetchimage.FetchImage
import com.vetclinic.app.common.fetchimage.cache.ImageCache
import com.vetclinic.app.common.fetchimage.cache.KeyHash
import com.vetclinic.app.common.fetchimage.decode.ComputeScale
import com.vetclinic.app.common.fetchimage.decode.ImageDecoder
import com.vetclinic.app.common.fetchimage.decode.TempFile
import com.vetclinic.app.common.fetchimage.strategy.ImageLoadStrategy
import com.vetclinic.app.common.fetchimage.target.GetTargetSize
import com.vetclinic.app.common.network.HttpService
import com.vetclinic.app.common.ui.UseCase
import com.vetclinic.app.data.cloud.*
import com.vetclinic.app.domain.ConfigDomain
import com.vetclinic.app.domain.PetDomain
import com.vetclinic.app.domain.usecase.FetchConfigUseCase
import com.vetclinic.app.domain.usecase.FetchPetsUseCase
import com.vetclinic.app.domain.workinghours.CheckWorkingHours
import com.vetclinic.app.domain.workinghours.CurrentHour
import com.vetclinic.app.domain.workinghours.ParseWorkingHours
import com.vetclinic.app.navigation.Navigation
import com.vetclinic.app.ui.list.ConfigMapper
import com.vetclinic.app.ui.list.PetListMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AppContainer(appContext: Context) : DiContainer {

    private val executorService: ExecutorService by lazy {
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    }

    private fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

    private val configHttpService: HttpService<ConfigCloud> by lazy {
        ConfigHttpService(
            CONFIG_URL,
            UnmarshallConfigResponse(),
            provideOkHttpClient()
        )
    }

    private val petHttpService: HttpService<List<PetCloud>> by lazy {
        PetHttpService(
            PETS_URL,
            UnmarshallPetListResponse(),
            provideOkHttpClient()
        )
    }

    override val navigation: Navigation.Component by lazy { Navigation.Base() }

    override val fetchConfigUseCase: UseCase<ConfigDomain> by lazy {
        FetchConfigUseCase(configHttpService, ConfigMapper(ParseWorkingHours.Base()))
    }

    override val fetchPetsUseCase: UseCase<List<PetDomain>> by lazy {
        FetchPetsUseCase(petHttpService, PetListMapper())
    }

    override val checkWorkingHours: CheckWorkingHours by lazy {
        CheckWorkingHours.Base(CurrentHour.Base())
    }

    override val fetchImage: FetchImage by lazy {
        FetchImage.Base(
            placeholder = R.drawable.ic_launcher_foreground,
            loadStrategy = ImageLoadStrategy.Memory(
                memoryCache = ImageCache.MemoryImageCache(),
                next = ImageLoadStrategy.Persistence(
                    persistenceCache = ImageCache.FileImageCache(
                        context = appContext,
                        keyHash = KeyHash.MD5(),
                        imageDecoder = ImageDecoder.FileDecoder()
                    ),
                    executorService = executorService,
                    next = ImageLoadStrategy.Remote(
                        okHttpClient = provideOkHttpClient(),
                        streamImageDecoder = ImageDecoder.StreamDecoder(
                            tempFile = TempFile.Cache(appContext),
                            computeScale = ComputeScale.Factor2()
                        )
                    )
                )
            ),
            getTargetSize = GetTargetSize.ViewTarget()
        )
    }

    companion object {
        private const val CONFIG_URL = "https://jsonkeeper.com/b/PN84"
        private const val PETS_URL = "https://jsonkeeper.com/b/7GTA"
    }
}