package com.cettourdev.challenge.utils

import com.cettourdev.challenge.model.ItemResponse
import java.util.Locale

object Formatter {
    fun getPriceWithCurrency(
        item: ItemResponse,
        originalPrice: Boolean,
    ): String {
        val priceToFormat = if (originalPrice) item.original_price else item.price
        if (priceToFormat <= 0) return ""

        val formattedPrice = String.format(Locale.getDefault(), "%,.0f", priceToFormat).replace(',', '.')
        return when (item.currency_id) {
            "ARS" -> "$ $formattedPrice"
            "USD" -> "US$ $formattedPrice"
            else -> formattedPrice
        }
    }
}
