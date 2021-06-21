package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.Const.Addresses.Companion.API_HOST
import com.crabgore.moviesDB.data.favorites.services.FavoritesService
import com.crabgore.moviesDB.data.movies.services.MovieDetailsService
import com.crabgore.moviesDB.data.movies.services.MovieService
import com.crabgore.moviesDB.data.people.services.PeopleDetailsService
import com.crabgore.moviesDB.data.people.services.PeopleService
import com.crabgore.moviesDB.data.search.services.SearchService
import com.crabgore.moviesDB.data.tv.services.TVDetailsServices
import com.crabgore.moviesDB.data.tv.services.TVService
import com.crabgore.moviesDB.data.user.services.UserDetailsService
import com.crabgore.moviesDB.data.user.services.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class RemoteModule {

    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_HOST)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun getMoviesApi(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    fun getTVsApi(retrofit: Retrofit): TVService =
        retrofit.create(TVService::class.java)

    @Provides
    fun getPeopleApi(retrofit: Retrofit): PeopleService =
        retrofit.create(PeopleService::class.java)

    @Provides
    fun getMovieDetailsApi(retrofit: Retrofit): MovieDetailsService =
        retrofit.create(MovieDetailsService::class.java)

    @Provides
    fun getTVDetailsApi(retrofit: Retrofit): TVDetailsServices =
        retrofit.create(TVDetailsServices::class.java)

    @Provides
    fun getPeopleDetailsApi(retrofit: Retrofit): PeopleDetailsService =
        retrofit.create(PeopleDetailsService::class.java)

    @Provides
    fun getFavoritesApi(retrofit: Retrofit): FavoritesService =
        retrofit.create(FavoritesService::class.java)

    @Provides
    fun getSearchApi(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)

    @Provides
    fun getUserApi(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    fun getUserDetailsApi(retrofit: Retrofit): UserDetailsService =
        retrofit.create(UserDetailsService::class.java)
}