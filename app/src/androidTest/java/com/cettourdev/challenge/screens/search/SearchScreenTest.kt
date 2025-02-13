package com.cettourdev.challenge.screens.search

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.cettourdev.challenge.AlertDialogAyuda
import com.cettourdev.challenge.MyTopBar
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun comprobarValidacionCampoBusqueda() {
        with(composeTestRule) {
            this.setContent {
                val query = remember { mutableStateOf("") }
                val snackbarHostState = remember { SnackbarHostState() }

                BusquedaOutlinedTextField(
                    searchQuery = query.value,
                    onQueryChanged = { query.value = it },
                    onSearch = {},
                    snackbarHostState = snackbarHostState
                )
            }
            composeTestRule.waitForIdle()

            //FINDER
            val queryTextField = composeTestRule.onNodeWithTag("campoBusquedaTag", useUnmergedTree = true)
            val supportingText = composeTestRule.onNodeWithTag("supportingTextTag", useUnmergedTree = true)
            val searchAction = composeTestRule.onNodeWithTag("performSearchIcon", useUnmergedTree = true)

            //ACTIONS
            supportingText.assertIsNotDisplayed()
            queryTextField.performTextClearance()
            queryTextField.performTextInput("Motorola")
            queryTextField.assertTextContains("moto", substring = true, ignoreCase = true)
            supportingText.assertIsNotDisplayed()
            queryTextField.performTextClearance()
            searchAction.performClick()
            supportingText.assertIsDisplayed()
            queryTextField.performTextInput("Samsung")
            queryTextField.assertTextContains("samsung", substring = false, ignoreCase = true)
            queryTextField.assertTextEquals("Samsung")
            searchAction.performClick()
            supportingText.assertIsNotDisplayed()
        }
    }

    @Test
    fun comprobarDialogAyuda() {

        with(composeTestRule) {
            this.setContent {
                val displayed = remember { mutableStateOf(false) }

                MyTopBar(null, null, onVisibilityChanged = { displayed.value = it })
                AlertDialogAyuda(displayed.value, onVisibilityChanged = { displayed.value = it })
            }
            composeTestRule.waitForIdle()

            //FINDER
            val helpIcon = composeTestRule.onNodeWithTag("iconoAyudaTag", useUnmergedTree = true)
            val dialog = composeTestRule.onNodeWithTag("dialogAyudaTag", useUnmergedTree = true)
            val buttonAceptar = composeTestRule.onNodeWithTag("aceptarButtonTag", useUnmergedTree = true)

            //ACTIONS
            helpIcon.assertIsDisplayed()
            dialog.assertIsNotDisplayed()
            helpIcon.performClick()
            dialog.assertIsDisplayed()
            buttonAceptar.performClick()
            dialog.assertIsNotDisplayed()
        }
    }
}