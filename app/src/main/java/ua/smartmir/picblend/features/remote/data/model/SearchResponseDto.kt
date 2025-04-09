package ua.smartmir.picblend.features.remote.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ua.smartmir.picblend.features.remote.domain.model.SearchResponse

data class SearchResponseDto(
    @Expose
    @SerializedName("total")
    val total: Int,
    @Expose
    @SerializedName("results")
    val results: List<UnsplashPhotoDto>
)

fun SearchResponseDto.mapTo(): SearchResponse = SearchResponse(
    total = total,
    results = results.map { it.mapTo() }
)
