package com.crabgore.moviesDB.data.people.repositories

import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.data.people.models.PeopleResponse
import com.crabgore.moviesDB.data.people.services.PeopleService
import com.crabgore.moviesDB.domain.repositories.interfaces.PeopleRepository
import com.crabgore.moviesDB.ui.items.PeopleItem
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val peopleService: PeopleService
) : PeopleRepository {
    override var maxPages = Int.MAX_VALUE

    override suspend fun getPopularPeople(page: Int?): Resource<List<PeopleItem>> {
        val response = peopleService.popularPeople(API_KEY, getLanguage(), page)
        return parseResponse(response)
    }

    private fun parseResponse(response: Response<PeopleResponse>): Resource<List<PeopleItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got TV response ${response.body()}")
            val list: MutableList<PeopleItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(PeopleItem(it.id, it.name, it.profilePath))
            }
            maxPages = response.body()?.totalPages!!
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }
}