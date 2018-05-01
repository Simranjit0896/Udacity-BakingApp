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

package com.simranjit.android.bakingapp.features.recipedetails;

//Created By Simranjit Singh 2018
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.simranjit.android.bakingapp.BakingApp;
import com.simranjit.android.bakingapp.R;
import com.simranjit.android.bakingapp.utils.FragmentUtils;

import javax.inject.Inject;

public class RecipeDetailsActivity extends AppCompatActivity {

  public static final String EXTRA_RECIPE_ID = "RECIPE_ID";
  private static final int DEFAULT_RECIPE_ID = 1;

  @Inject
  RecipeDetailsPresenter recipeDetailsPresenter;

  public static Intent prepareIntent(Context context, int recipeId) {
    Intent intent = new Intent(context, RecipeDetailsActivity.class);
    intent.putExtra(EXTRA_RECIPE_ID, recipeId);
    return intent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_recipe_details);

    setUpActionBar();

    int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, DEFAULT_RECIPE_ID);

    RecipeDetailsFragment recipeDetailsFragment =
        (RecipeDetailsFragment) getSupportFragmentManager()
            .findFragmentById(R.id.detailsFragmentContainer);

    if (recipeDetailsFragment == null) {
      recipeDetailsFragment = RecipeDetailsFragment.newInstance(recipeId);
      FragmentUtils.addFragmentTo(getSupportFragmentManager(), recipeDetailsFragment,
          R.id.detailsFragmentContainer);
    }

    DaggerRecipeDetailsComponent.builder()
        .recipeRepositoryComponent(((BakingApp) getApplication()).getRecipeRepositoryComponent())
        .recipeDetailsPresenterModule(new RecipeDetailsPresenterModule(recipeDetailsFragment, recipeId))
        .build()
        .inject(this);
  }

  private void setUpActionBar() {
    ActionBar supportActionBar = getSupportActionBar();

    if (supportActionBar != null) {
      supportActionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }
}
