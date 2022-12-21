package com.hand.comeeatme.di

import com.hand.comeeatme.data.preference.AppPreferenceManager
import com.hand.comeeatme.data.repository.bookmark.BookmarkRepository
import com.hand.comeeatme.data.repository.bookmark.DefaultBookmarkRepository
import com.hand.comeeatme.data.repository.code.CodeRepository
import com.hand.comeeatme.data.repository.code.DefaultCodeRepository
import com.hand.comeeatme.data.repository.comment.CommentRepository
import com.hand.comeeatme.data.repository.comment.DefaultCommentRepository
import com.hand.comeeatme.data.repository.favorite.DefaultFavoriteRepository
import com.hand.comeeatme.data.repository.favorite.FavoriteRepository
import com.hand.comeeatme.data.repository.image.DefaultImageRepository
import com.hand.comeeatme.data.repository.image.ImageRepository
import com.hand.comeeatme.data.repository.kakao.DefaultKakaoRepository
import com.hand.comeeatme.data.repository.kakao.KakaoRepository
import com.hand.comeeatme.data.repository.like.DefaultLikeRepository
import com.hand.comeeatme.data.repository.like.LikeRepository
import com.hand.comeeatme.data.repository.member.DefaultMemberRepository
import com.hand.comeeatme.data.repository.member.MemberRepository
import com.hand.comeeatme.data.repository.oauth.DefaultOAuthRepository
import com.hand.comeeatme.data.repository.oauth.OAuthRepository
import com.hand.comeeatme.data.repository.post.DefaultPostRepository
import com.hand.comeeatme.data.repository.post.PostRepository
import com.hand.comeeatme.data.repository.report.DefaultReportRepository
import com.hand.comeeatme.data.repository.report.ReportRepository
import com.hand.comeeatme.data.repository.restaurant.DefaultRestaurantRepository
import com.hand.comeeatme.data.repository.restaurant.RestaurantRepository
import com.hand.comeeatme.util.event.MenuChangeEventBus
import com.hand.comeeatme.view.login.LogInViewModel
import com.hand.comeeatme.view.login.term.TermViewModel
import com.hand.comeeatme.view.main.bookmark.BookmarkViewModel
import com.hand.comeeatme.view.main.bookmark.favorite.FavoriteRestaurantViewModel
import com.hand.comeeatme.view.main.bookmark.post.BookmarkPostViewModel
import com.hand.comeeatme.view.main.home.HomeViewModel
import com.hand.comeeatme.view.main.home.newpost.NewPostViewModel
import com.hand.comeeatme.view.main.home.newpost.album.AlbumViewModel
import com.hand.comeeatme.view.main.home.newpost.crop.CropViewModel
import com.hand.comeeatme.view.main.home.post.DetailPostViewModel
import com.hand.comeeatme.view.main.home.post.report.ReportViewModel
import com.hand.comeeatme.view.main.home.search.SearchViewModel
import com.hand.comeeatme.view.main.rank.RankViewModel
import com.hand.comeeatme.view.main.rank.region.RegionViewModel
import com.hand.comeeatme.view.main.rank.restaurant.DetailRestaurantViewModel
import com.hand.comeeatme.view.main.user.UserViewModel
import com.hand.comeeatme.view.main.user.edit.UserEditViewModel
import com.hand.comeeatme.view.main.user.menu.likepost.LikedPostViewModel
import com.hand.comeeatme.view.main.user.menu.mycomment.MyCommentViewModel
import com.hand.comeeatme.view.main.user.menu.myreview.MyReviewViewModel
import com.hand.comeeatme.view.main.user.menu.recentreview.RecentReviewViewModel
import com.hand.comeeatme.view.main.user.other.OtherPageViewModel
import com.hand.comeeatme.view.main.user.setting.SettingViewModel
import com.hand.comeeatme.view.main.user.setting.unlink.UnlinkViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    // viewModel
    viewModel { LogInViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { NewPostViewModel(get(), get(), get(), get()) }
    viewModel { AlbumViewModel() }
    viewModel { CropViewModel() }
    viewModel { DetailPostViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { BookmarkViewModel() }
    viewModel { BookmarkPostViewModel(get(), get()) }
    viewModel { FavoriteRestaurantViewModel(get(), get()) }
    viewModel { UserViewModel(get(), get(), get(), get(), get()) }
    viewModel { UserEditViewModel(get(), get(), get()) }
    viewModel { MyReviewViewModel(get(), get()) }
    viewModel { MyCommentViewModel(get(), get()) }
    viewModel { LikedPostViewModel(get(), get()) }
    viewModel { RecentReviewViewModel(get()) }
    viewModel { OtherPageViewModel(get(), get(), get(), get(), get()) }
    viewModel { DetailRestaurantViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { TermViewModel(get(), get()) }
    viewModel { SettingViewModel(get(), get(), get()) }
    viewModel { ReportViewModel(get(), get()) }
    viewModel { RankViewModel(get(), get(), get(), get(), get()) }
    viewModel { RegionViewModel(get(), get()) }
    viewModel { UnlinkViewModel(get(), get())}

    // repository
    single<PostRepository> { DefaultPostRepository(get(), get()) }
    single<OAuthRepository> { DefaultOAuthRepository(get(), get()) }
    single<MemberRepository> { DefaultMemberRepository(get(), get()) }
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get()) }
    single<ImageRepository> { DefaultImageRepository(get(), get()) }
    single<LikeRepository> { DefaultLikeRepository(get(), get()) }
    single<BookmarkRepository> { DefaultBookmarkRepository(get(), get()) }
    single<FavoriteRepository> { DefaultFavoriteRepository(get(), get()) }
    single<CommentRepository> { DefaultCommentRepository(get(), get()) }
    single<ReportRepository> { DefaultReportRepository(get(), get()) }
    single<KakaoRepository> { DefaultKakaoRepository(get(), get()) }
    single<CodeRepository> { DefaultCodeRepository(get(), get()) }

    // provider
    single(named("comeeatme")) { provideApiRetrofit(get(), get(), get()) }
    single(named("kakao")) { provideKakaoApiRetrofit(get(), get(), get()) }

    // ComeEatMe
    single { provideOAuthApiService(get(qualifier = named("comeeatme"))) }
    single { providePostApiService(get(qualifier = named("comeeatme"))) }
    single { provideRestaurantService(get(qualifier = named("comeeatme"))) }
    single { provideMemberService(get(qualifier = named("comeeatme"))) }
    single { provideImageService(get(qualifier = named("comeeatme"))) }
    single { provideLikeService(get(qualifier = named("comeeatme"))) }
    single { provideBookmarkService(get(qualifier = named("comeeatme"))) }
    single { provideFavoriteService(get(qualifier = named("comeeatme"))) }
    single { provideCommentService(get(qualifier = named("comeeatme"))) }
    single { provideReportService(get(qualifier = named("comeeatme"))) }
    single { provideCodeService(get(qualifier = named("comeeatme"))) }

    // Kakao
    single { provideKakaoService(get(qualifier = named("kakao"))) }

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
