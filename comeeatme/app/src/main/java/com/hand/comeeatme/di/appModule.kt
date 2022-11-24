package com.hand.comeeatme.di

import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.data.repository.bookmark.DefaultBookmarkRepository
import com.hand.comeeatme.data.repository.favorite.DefaultFavoriteRepository
import com.hand.comeeatme.data.repository.favorite.FavoriteRepository
import com.hand.comeeatme.data.repository.home.DefaultPostRepository
import com.hand.comeeatme.data.repository.home.PostRepository
import com.hand.comeeatme.data.repository.image.DefaultImageRepository
import com.hand.comeeatme.data.repository.image.ImageRepository
import com.hand.comeeatme.data.repository.like.DefaultLikeRepository
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.data.repository.logIn.DefaultLogInRepository
import com.hand.comeeatme.data.repository.logIn.LogInRepository
import com.hand.comeeatme.data.repository.member.DefaultMemberRepository
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.repository.restaurant.DefaultRestaurantRepository
import com.hand.comeeatme.data.repository.restaurant.RestaurantRepository
import com.hand.comeeatme.util.event.MenuChangeEventBus
import com.hand.comeeatme.view.login.LogInViewModel
import com.hand.comeeatme.view.main.bookmark.BookmarkViewModel
import com.hand.comeeatme.view.main.bookmark.favorite.FavoritePostViewModel
import com.hand.comeeatme.view.main.bookmark.post.BookmarkPostViewModel
import com.hand.comeeatme.view.main.home.HomeViewModel
import com.hand.comeeatme.view.main.home.newpost.NewPostViewModel
import com.hand.comeeatme.view.main.home.newpost.album.AlbumViewModel
import com.hand.comeeatme.view.main.home.newpost.crop.CropViewModel
import com.hand.comeeatme.view.main.home.post.DetailPostViewModel
import com.hand.comeeatme.view.main.home.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // viewModel
    viewModel { LogInViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { NewPostViewModel(get(), get(), get(), get()) }
    viewModel { AlbumViewModel() }
    viewModel { CropViewModel() }
    viewModel { DetailPostViewModel(get(), get(), get(), get(), get()) }
    viewModel { BookmarkViewModel(get()) }
    viewModel { BookmarkPostViewModel(get(), get())}
    viewModel { FavoritePostViewModel(get(), get())}

    single<PostRepository> { DefaultPostRepository(get(), get()) }
    single<LogInRepository> { DefaultLogInRepository(get(), get()) }
    single<MemberRepository> { DefaultMemberRepository(get(), get()) }
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get()) }
    single<ImageRepository> { DefaultImageRepository(get(), get()) }
    single<LikeRepository> { DefaultLikeRepository(get(), get()) }
    single<BookmarkRepository> { DefaultBookmarkRepository(get(), get()) }
    single<FavoriteRepository> { DefaultFavoriteRepository(get(), get())}

    // provider
    single { provideApiRetrofit(get(), get(), get()) }
    single { provideOAuthApiService(get()) }
    single { providePostApiService(get()) }
    single { provideRestaurantService(get()) }
    single { provideMemberService(get()) }
    single { provideImageService(get()) }
    single { provideLikeService(get()) }
    single { provideBookmarkService(get()) }
    single { provideFavoriteService(get()) }

    single { provideGson() }
    single { provideGsonConverterFactory(get()) }
    single { provideScalarsConverterFactory() }
    single { buildOkHttpClient() }

    // eventBus
    single { MenuChangeEventBus() }

    single { AppPreferenceManager(androidApplication()) }

    single { Dispatchers.IO }
    single { Dispatchers.Main }

}
