package com.hand.comeeatme.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hand.comeeatme.BuildConfig
import com.hand.comeeatme.data.network.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

fun provideOAuthApiService(retrofit: Retrofit): OAuthService {
    return retrofit.create(OAuthService::class.java)
}

fun providePostApiService(retrofit: Retrofit): PostService {
    return retrofit.create(PostService::class.java)
}

fun provideRestaurantService(retrofit: Retrofit): RestaurantService {
    return retrofit.create(RestaurantService::class.java)
}

fun provideMemberService(retrofit: Retrofit): MemberService {
    return retrofit.create(MemberService::class.java)
}

fun provideImageService(retrofit: Retrofit): ImageService {
    return retrofit.create(ImageService::class.java)
}

fun provideLikeService(retrofit: Retrofit) : LikeService {
    return retrofit.create(LikeService::class.java)
}

fun provideBookmarkService(retrofit: Retrofit) : BookmarkService {
    return retrofit.create(BookmarkService::class.java)
}

fun provideApiRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
    scalarsConverterFactory: ScalarsConverterFactory,
): Retrofit {

    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(scalarsConverterFactory)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()
}

fun provideGson() : Gson {
    return GsonBuilder().setLenient().create()
}

fun provideGsonConverterFactory(
    gson: Gson
): GsonConverterFactory{
    return GsonConverterFactory.create(gson)
}

fun provideScalarsConverterFactory(): ScalarsConverterFactory {
    return ScalarsConverterFactory.create()
}

fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}