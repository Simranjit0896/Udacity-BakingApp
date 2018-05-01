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
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.simranjit.android.bakingapp.features.recipedetails.RecipeDetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailsActivityUITest {

  private static final int STEP_WITH_VIDEO = 0;
  private static final int STEP_WITHOUT_VIDEO = 1;

  @Rule
  public ActivityTestRule<RecipeDetailsActivity> activityActivityTestRule =
      new ActivityTestRule<>(RecipeDetailsActivity.class);

  @Test
  public void clickOnRecyclerViewItem_opensRecipeStepActivity() {

    onView(withId(R.id.recipe_details_steps))
        .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

    onView(withId(R.id.recipe_step_viewpager))
        .check(matches(isDisplayed()));
  }

  @Test
  public void clickOnStepWithVideo_showsVideoPlayerView() {

    onView(withId(R.id.recipe_details_steps))
        .perform(RecyclerViewActions.actionOnItemAtPosition(STEP_WITH_VIDEO, click()));

    onView(
        allOf(
            withId(R.id.recipe_step_video),
            withParent(withParent(withId(R.id.recipe_step_viewpager))),
            isDisplayed()))
        .check(matches(isDisplayed()));
  }

  @Test
  public void clickOnStepWithoutVideo_hidesVideoPlayerView() {

    onView(withId(R.id.recipe_details_steps))
        .perform(RecyclerViewActions.actionOnItemAtPosition(STEP_WITHOUT_VIDEO, click()));

    onView(
        allOf(
            withId(R.id.recipe_step_video),
            withParent(withParent(withId(R.id.recipe_step_viewpager))),
            isDisplayed()))
        .check(doesNotExist());
  }
}
