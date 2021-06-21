package com.crabgore.moviesDB.domain.repositories.interfaces

import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.movies.models.MovieDetailsResponse
import com.crabgore.moviesDB.ui.items.CreditsItem

interface MovieDetailsRepository {

    var castListStore: Resource<List<CreditsItem>>
    var crewListStore: Resource<List<CreditsItem>>
    suspend fun getMovieDetails(id: Int): Resource<MovieDetailsResponse>
}