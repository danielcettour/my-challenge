package com.cettourdev.challenge.screens.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.cettourdev.challenge.domain.SearchUseCase
import com.cettourdev.challenge.model.ItemAttribute
import com.cettourdev.challenge.model.ItemResponse
import com.cettourdev.challenge.model.SearchResponse
import com.cettourdev.challenge.utils.Resource
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit test de SearchViewModel, mockeando el caso de uso utilizando la librería mockk
 */

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SearchViewModel
    private val searchUseCase: SearchUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(searchUseCase)
    }

    @Test
    fun `verificar que se guarden los datos cuando la búsqueda retorna éxito y una lista no vacía`() = runTest {
        // given
        val query = "motorola"
        val mockResponse = Resource.success(data = SearchResponse(site_id = "MLA", query = query, results = fakeItemsResponse))
        coEvery { searchUseCase(query) } returns mockResponse

        // when
        viewModel.setearQuery(query)
        viewModel.onSearch()
        advanceUntilIdle() // se avanza cuando las corrutinas pendientes hayan terminado

        // then
        coVerify { searchUseCase(query) }
        assertThat(viewModel.items.value).isEqualTo(fakeItemsResponse)
        assertThat(viewModel.isLoading.value).isFalse()
        assertThat(viewModel.resultsNotmpty.value).isTrue()
        assertThat(viewModel.resultsError.value).isFalse()
    }

    @Test
    fun `verificar que no haya error y la lista de items sea vacía cuando la búsqueda retorna éxito pero una lista vacía`() = runTest {
        // given
        val query = "Motorola"
        val mockResponse = Resource.success(SearchResponse(site_id = "MLA", query = query, results = emptyList()))
        coEvery { searchUseCase(query) } returns mockResponse

        // when
        viewModel.setearQuery(query)
        viewModel.onSearch()
        advanceUntilIdle()

        // then
        coVerify { searchUseCase(query) }
        assertThat(viewModel.items.value).isEmpty()
        assertThat(viewModel.isLoading.value).isFalse()
        assertThat(viewModel.resultsNotmpty.value).isFalse()
        assertThat(viewModel.resultsError.value).isFalse()
    }

    @Test
    fun `verificar el manejo de error cuando la búsqueda retorna un error inesperado`() = runTest {
        // given
        val query = "Motorola"
        val mockResponse = Resource.error<SearchResponse>(msg = "Error", data = null)
        coEvery { searchUseCase(query) } returns mockResponse

        // when
        viewModel.setearQuery(query)
        viewModel.onSearch()
        advanceUntilIdle()

        // then
        coVerify { searchUseCase(query) }
        assertThat(viewModel.items.value).isEmpty()
        assertThat(viewModel.isLoading.value).isFalse()
        assertThat(viewModel.resultsNotmpty.value).isNull() //esta variable no cambia en caso de error
        assertThat(viewModel.resultsError.value).isTrue()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    val fakeItemsResponse = listOf(
        ItemResponse(
            id = "1234",
            title = "Motorola Moto G04s",
            condition = "new",
            permalink = "https://permalink.com/item/1",
            thumbnail = "https://thumbnail.com/image1.jpg",
            price = 189999.0,
            currency_id = "ARS",
            original_price = 279999.0,
            attributes = listOf(
                ItemAttribute(name = "Marca", value_name = "Motorola"),
                ItemAttribute(name = "Color", value_name = "Azul"),
                ItemAttribute(name = "Modelo", value_name = "G04s")
            )
        ),
        ItemResponse(
            id = "2345",
            title = "Motorola Moto E14 64 Gb",
            condition = "new",
            permalink = "https://permalink.com/item/2",
            thumbnail = "https://thumbnail.com/image2.jpg",
            price = 149999.0,
            currency_id = "ARS",
            original_price = 199999.0,
            attributes = listOf(
                ItemAttribute(name = "Marca", value_name = "Motorola"),
                ItemAttribute(name = "Color", value_name = "Verde"),
                ItemAttribute(name = "Modelo", value_name = "E14")
            )
        ),
        ItemResponse(
            id = "3456",
            title = "Motorola Edge 50 Fusion",
            condition = "new",
            permalink = "https://permalink.com/item/3",
            thumbnail = "https://thumbnail.com/image3.jpg",
            price = 799999.99,
            currency_id = "ARS",
            original_price = 899999.99,
            attributes = listOf(
                ItemAttribute(name = "Marca", value_name = "Motorola"),
                ItemAttribute(name = "Color", value_name = "Gris"),
                ItemAttribute(name = "Modelo", value_name = "Edge 50")
            )
        )
    )
}
