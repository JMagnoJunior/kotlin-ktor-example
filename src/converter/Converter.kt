package com.magnojr.converter

import com.magnojr.domain.Restaurant
import com.magnojr.dto.RestaurantReceiverDTO


object ConverterService {
    fun fromDTOToDomain(dto: RestaurantReceiverDTO): Restaurant {
        return Restaurant(
            id = null, name = dto.name,
            local = dto.local, rate = dto.rate
        )
    }
}