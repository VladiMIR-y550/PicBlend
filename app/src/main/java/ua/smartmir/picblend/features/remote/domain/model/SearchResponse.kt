package ua.smartmir.picblend.features.remote.domain.model

data class SearchResponse(
    val total: Int,
    val results: List<UnsplashPhoto>
)

