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

package com.simranjit.android.bakingapp.features.recipelist;

//Created By Simranjit Singh 2018

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.simranjit.android.bakingapp.BakingApp;
import com.simranjit.android.bakingapp.R;
import com.simranjit.android.bakingapp.data.idlingresource.RecipesIdlingResource;
import com.simranjit.android.bakingapp.utils.FragmentUtils;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

public class RecipeListActivity extends AppCompatActivity {

  @Inject
  RecipeListPresenter recipeListPresenter;

  @Nullable
  private RecipesIdlingResource idlingResource;
  private AtomicBoolean isRunningTest;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // For waking up the screen for RecipeListActivityUITest
    // Un comment to use for testing purposes

//    if (BuildConfig.DEBUG) {
//      KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
//      KeyguardManager.KeyguardLock keyguardLock = km.newKeyguardLock("TAG");
//      keyguardLock.disableKeyguard();
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//    }
    // Normal Code
    setContentView(R.layout.activity_recipe_list);

    RecipeListFragment recipeListFragment =
        (RecipeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

    if (recipeListFragment == null) {
      recipeListFragment = RecipeListFragment.newInstance();
      FragmentUtils.addFragmentTo(getSupportFragmentManager(), recipeListFragment,
          R.id.fragmentContainer);
    }

    DaggerRecipeListComponent.builder()
        .recipeRepositoryComponent(((BakingApp) getApplication()).getRecipeRepositoryComponent())
        .recipeListPresenterModule(new RecipeListPresenterModule(recipeListFragment))
        .build()
        .inject(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    if(id == R.id.action_refresh) {
      recipeListPresenter.loadRecipesFromRepo(true, idlingResource);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @VisibleForTesting
  @NonNull
  public IdlingResource getIdlingResource() {
    if (idlingResource == null) {
      idlingResource = new RecipesIdlingResource();
    }
    return idlingResource;
  }
}
