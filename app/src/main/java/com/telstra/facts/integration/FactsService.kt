package com.telstra.facts.integration

import com.telstra.facts.model.Facts
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface FactsService {

    @GET
    fun getFacts(@Url url: String): Observable<Facts>
}