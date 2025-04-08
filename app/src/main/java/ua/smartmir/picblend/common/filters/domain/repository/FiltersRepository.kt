package ua.smartmir.picblend.common.filters.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ua.smartmir.picblend.common.filters.domain.model.FilterDomainEntity
import ua.smartmir.picblend.common.filters.domain.model.FilterType

interface FiltersRepository {
    val filterStatesFlow: StateFlow<List<FilterDomainEntity>>
    fun updateSelectedFilter(filterType: FilterType)
}