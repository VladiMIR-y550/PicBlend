package ua.smartmir.picblend.features.remote.presentation.model

import ua.smartmir.picblend.features.remote.domain.model.SearchResponse

data class SearchUi(
    val total: Int,
    val results: List<PhotoUi>
)

fun SearchResponse.mapTo(): SearchUi = SearchUi(
    total = total,
    results = results.map { it.mapTo() }
)