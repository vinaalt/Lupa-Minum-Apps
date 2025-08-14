package edu.unikom.lupaminum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.unikom.lupaminum.network.ApiService
import edu.unikom.lupaminum.repository.UserRepository
import edu.unikom.lupaminum.repository.WeatherRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https:///api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserRepository()

    @Provides
    @Singleton
    fun provideWeatherRepository(apiService: ApiService): WeatherRepository =
        WeatherRepository(apiService)

}