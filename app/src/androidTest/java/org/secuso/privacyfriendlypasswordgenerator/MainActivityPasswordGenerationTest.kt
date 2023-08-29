package org.secuso.privacyfriendlypasswordgenerator

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.secuso.privacyfriendlypasswordgenerator.activities.MainActivity


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityPasswordGenerationTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun addEntry() {
        //Remove entry if already present
        removeEntry()
        //Add new entry
        onView(withId(R.id.add_fab)).perform(click())
        onView(withId(R.id.editTextDomain))
            .perform(typeText(DOMAIN), closeSoftKeyboard())
        onView(withId(R.id.editTextUsername))
            .perform(typeText(USERNAME), closeSoftKeyboard())
        //Close dialog
        onView(withId(android.R.id.button1)).perform(click())
    }

    @After
    fun removeEntry() {
        //Delete entry
        try {
            onView(withId(R.id.recycler_view))
                .perform(
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(DOMAIN)),
                        swipeLeft()
                    )
                )
        } catch (e: Exception) {

        }
    }

    /**
     *
     */
    private fun checkEntry(domain: String, masterPassword: String, expectedPassword: String) {
        //Select entry
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText(domain)), click()))

        //Click skip in the master password tutorial appears
        try {
            onView(withId(R.id.btn_skip)).perform(click())
        } catch (e: NoMatchingViewException) {

        }

        //Enter master password and generate password
        onView(withId(R.id.editTextMasterpassword))
            .perform(typeText(masterPassword), closeSoftKeyboard())
        onView(withId(R.id.generatorButton)).perform(click())

        //Compare generated password to expected value
        onView(withId(R.id.textViewPassword)).check(matches(withText(expectedPassword)))

        //Close dialog
        onView(withId(android.R.id.button1)).perform(click())
    }

    @Test
    fun generatePassword() {
        checkEntry(DOMAIN, MASTER_PASSWORD, PASSWORD_EXPECTED)
    }

    @Test
    fun updateEntry() {
        //Open update dialog
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText(DOMAIN)), longClick()))
        //Enter new username
        onView(withId(R.id.editTextUsernameUpdate))
            .perform(replaceText(USERNAME_AFTER_UPDATE), closeSoftKeyboard())
        //Uncheck special characters
        onView(withId(R.id.checkBoxSpecialCharacterUpdate)).perform(click())
        //Confirm update
        onView(withId(android.R.id.button1)).perform(click())
        //Enter master password
        onView(withId(R.id.editTextUpdateMasterpassword))
            .perform(typeText(MASTER_PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.displayButton)).perform(click())
        //Compare generated passwords to expected values
        onView(withId(R.id.textViewOldPassword)).check(matches(withText(PASSWORD_EXPECTED)))
        onView(withId(R.id.textViewNewPassword)).check(matches(withText(PASSWORD_EXPECTED_AFTER_UPDATE)))
        //Close dialog
        onView(withId(android.R.id.button1)).perform(click())


        checkEntry(DOMAIN, MASTER_PASSWORD, PASSWORD_EXPECTED_AFTER_UPDATE)
    }

    companion object {
        const val DOMAIN = "automated-test.test.com"
        const val USERNAME = "hugo"
        const val USERNAME_AFTER_UPDATE = "hugo2"
        const val MASTER_PASSWORD = "12345678"
        const val PASSWORD_EXPECTED = "zU5)}h(FNf"
        const val PASSWORD_EXPECTED_AFTER_UPDATE = "fYt8wHnhsP"
    }
}