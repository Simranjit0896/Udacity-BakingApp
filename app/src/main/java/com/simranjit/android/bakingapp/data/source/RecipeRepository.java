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

package com.simranjit.android.bakingapp.data.source;

//Created By Simranjit Singh 2018
import com.simranjit.android.bakingapp.data.model.Ingredient;
import com.simranjit.android.bakingapp.data.model.Recipe;
import com.simranjit.android.bakingapp.data.model.Step;
import com.simranjit.android.bakingapp.data.source.local.Local;
import com.simranjit.android.bakingapp.data.source.local.prefs.PreferencesHelper;
import com.simranjit.android.bakingapp.data.source.remote.Remote;
import com.simranjit.android.bakingapp.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class RecipeRepository implements RecipeDataSource {

  private final RecipeDataSource recipeRemoteDataSource;
  private final RecipeDataSource recipeLocalDataSource;
  private final PreferencesHelper preferencesHelper;

  @Inject
  public RecipeRepository(
      @Remote RecipeDataSource recipeRemoteDataSource,
      @Local RecipeDataSource recipeLocalDataSource,
      PreferencesHelper preferencesHelper) {
    this.recipeRemoteDataSource = recipeRemoteDataSource;
    this.recipeLocalDataSource = recipeLocalDataSource;
    this.preferencesHelper = preferencesHelper;
  }

  @Override
  public Observable<List<Recipe>> getRecipes() {

    if (!preferencesHelper.isRecipeListSynced()) {
      return recipeRemoteDataSource
          .getRecipes()
          .compose(RxUtils.applySchedulers())
          .doOnNext(recipeList -> {
            recipeLocalDataSource.saveRecipes(recipeList);
            preferencesHelper.saveRecipeNamesList(recipeList);
          });
    } else {
      return recipeLocalDataSource
          .getRecipes()
          .compose(RxUtils.applySchedulers());
    }
  }

  @Override
  public Observable<List<Ingredient>> getRecipeIngredients(int recipeId) {
    return recipeLocalDataSource
        .getRecipeIngredients(recipeId)
        .compose(RxUtils.applySchedulers());
  }

  @Override
  public Observable<List<Ingredient>> getRecipeIngredients(String recipeName) {
    return recipeLocalDataSource
        .getRecipeIngredients(recipeName)
        .compose(RxUtils.applySchedulers());
  }

  @Override
  public Observable<List<Step>> getRecipeSteps(int recipeId) {
    return recipeLocalDataSource
        .getRecipeSteps(recipeId)
        .compose(RxUtils.applySchedulers());
  }

  @Override
  public void saveRecipes(List<Recipe> recipes) {
    recipeLocalDataSource.saveRecipes(recipes);
  }

  public void markRepoAsSynced(boolean synced) {
    preferencesHelper.setRecipeListSynced(synced);
  }

  public PreferencesHelper getPreferencesHelper() {
    return preferencesHelper;
  }
}
