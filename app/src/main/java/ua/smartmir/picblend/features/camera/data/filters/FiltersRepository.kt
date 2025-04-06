package ua.smartmir.picblend.features.camera.data.filters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ua.smartmir.picblend.features.camera.domain.FilterDomainEntity
import ua.smartmir.picblend.features.camera.domain.FilterType
import javax.inject.Inject

interface FiltersRepository {
    val filterStatesFlow: StateFlow<List<FilterDomainEntity>>
    fun updateSelectedFilter(filterType: FilterType)

    class Base @Inject constructor() : FiltersRepository {

        private val initialFilters = listOf(
            FilterDomainEntity(isSelected = true, filterType = FilterType.None),
            FilterDomainEntity(filterType = FilterType.INVERT),
            FilterDomainEntity(filterType = FilterType.GRAYSCALE),
            FilterDomainEntity(filterType = FilterType.SEPIA)
        )

        private val _filtersFlow = MutableStateFlow(initialFilters)
        override val filterStatesFlow: StateFlow<List<FilterDomainEntity>> = _filtersFlow

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