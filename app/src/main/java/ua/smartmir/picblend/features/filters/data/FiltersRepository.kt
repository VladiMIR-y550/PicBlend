package ua.smartmir.picblend.features.filters.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ua.smartmir.picblend.features.filters.domain.model.Filter
import ua.smartmir.picblend.features.filters.domain.model.FilterType
import javax.inject.Inject

interface FiltersRepository {
    val filterStatesFlow: StateFlow<List<Filter>>
    fun updateSelectedFilter(filterType: FilterType)

    class Base @Inject constructor() : FiltersRepository {
        private val initialFilters = listOf(
            Filter(isSelected = true, filterType = FilterType.None),
            Filter(filterType = FilterType.INVERT),
            Filter(filterType = FilterType.GRAYSCALE),
            Filter(filterType = FilterType.SEPIA)
        )

        private val _filtersFlow = MutableStateFlow(initialFilters)
        override val filterStatesFlow: StateFlow<List<Filter>> = _filtersFlow

        override fun updateSelectedFilter(filterType: FilterType) {
            _filtersFlow.update { filters ->
                filters.map {
                    val isSelected = it.filterType == filterType
                    it.copy(isSelected = isSelected)
                }
            }
        }
    }
}