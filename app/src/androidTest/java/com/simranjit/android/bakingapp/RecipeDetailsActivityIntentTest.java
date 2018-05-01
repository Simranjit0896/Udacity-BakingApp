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
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.simranjit.android.bakingapp.features.recipedetails.RecipeDetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailsActivityIntentTest {

  private static final String EXTRA_RECIPE_ID_KEY = "RECIPE_ID";
  private static final int EXTRA_RECIPE_ID_VALUE = 1;

  private static final String EXTRA_STEP_ID_KEY = "STEP_ID";
  private static final int EXTRA_STEP_ID_VALUE = 1;

  @Rule
  public IntentsTestRule<RecipeDetailsActivity> intentsTestRule
      = new IntentsTestRule<>(RecipeDetailsActivity.class);

  @Test
  public void clickOnRecyclerViewItem_runsRecipeStepActivityIntent() {

    onView(ViewMatchers.withId(R.id.recipe_details_steps))
        .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

    intended(allOf(
        hasExtra(EXTRA_RECIPE_ID_KEY, EXTRA_RECIPE_ID_VALUE),
        hasExtra(EXTRA_STEP_ID_KEY, EXTRA_STEP_ID_VALUE)
    ));
  }
}
