/*
 * MIT License
 *
 * Copyright (c) 2018 Simranjit Singh
 *
 * This project was submitted by Simranjit Singh as  part of the Android Developer Nanodegree at Udacity.
 *
 * Besides the above notice, the following license applies and this license notice
 * must be included in all works derived from this project.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.simranjit.android.bakingapp;

//Created By Simranjit Singh 2018
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.simranjit.android.bakingapp.features.recipelist.RecipeListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListActivityUITest {

  @Rule
  public ActivityTestRule<RecipeListActivity> activityTestRule =
      new ActivityTestRule<>(RecipeListActivity.class);

  private IdlingResource idlingResource;

  @Before
  public void registerIdlingResource() {
    idlingResource = activityTestRule.getActivity().getIdlingResource();
    Espresso.registerIdlingResources(idlingResource);
  }

  // The device screen needs to be on during this test or the test will fail
  // To do so without having to wake up screen by hand, some code is used in the onCreate method.
  // You need to uncomment that code in RecipeListActivity if testing on a physical device with screen off
  // No need to do that if the device's screen is on during the test or if you're using an emulator.
  @Test
  public void clickOnRefreshMenuItem_showsSuccessfulSyncMessage() {

    // Click on Refresh menu item
    onView(withId(R.id.action_refresh))
        .perform(click());

    // Check if successful sync message is displayed
    onView(withText(R.string.recipe_list_sync_completed)).
        inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).
            check(matches(isDisplayed()));
  }

  @Test
  public void clickOnRecyclerViewItem_opensRecipeDetailsActivity() {

    onView(withId(R.id.recipe_list_recycler_view))
        .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

    onView(withId(R.id.recipe_details_ingredients))
        .check(matches(isDisplayed()));
  }

  @After
  public void unregisterIdlingResource() {
    if (idlingResource != null) {
      Espresso.unregisterIdlingResources(idlingResource);
    }
  }
}
