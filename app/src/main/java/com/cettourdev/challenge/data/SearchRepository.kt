package com.cettourdev.challenge.data

import com.cettourdev.challenge.data.network.SearchService
import com.cettourdev.challenge.model.SearchResponse
import com.cettourdev.challenge.utils.Resource
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: SearchService) {

    suspend fun doSearch(query: String): Resource<SearchResponse> {
        return try {
            val response = api.doSearch(query)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    Resource.success(response.body())
                } else {
                    logErrorToCrashlytics("API response null")
                    Resource.error("Error", null)
                }
            } else {
                logErrorToCrashlytics("API response not successful")
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            logErrorToCrashlytics(e.message ?: "Exception")
            Resource.error("Error", null)
        }
    }

    private fun logErrorToCrashlytics(error: String) {
        FirebaseCrashlytics.getInstance().log(error)
        FirebaseCrashlytics.getInstance().recordException(RuntimeException(error))
    }
}
