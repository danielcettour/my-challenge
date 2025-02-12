package com.cettourdev.challenge.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.focus.FocusManager
import com.cettourdev.challenge.model.ItemResponse
import java.util.Locale

object Utils {
    const val BASE_URL = "https://api.mercadolibre.com/"

    fun getPriceWithCurrency(item: ItemResponse, originalPrice: Boolean): String {
        val priceToFormat = if (originalPrice) item.original_price else item.price
        if (priceToFormat <= 0) return ""

        val formattedPrice =
            String.format(Locale.getDefault(), "%,.0f", priceToFormat).replace(',', '.')
        return when (item.currency_id) {
            "ARS" -> "$ $formattedPrice"
            "USD" -> "US$ $formattedPrice"
            else -> formattedPrice
        }
    }

    fun hideKeyboard(
        context: Context,
        focusManager: FocusManager,
    ) {
        focusManager.clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(null, 0)
    }
}
