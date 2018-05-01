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
import com.simranjit.android.bakingapp.data.idlingresource.RecipesIdlingResource;
import com.simranjit.android.bakingapp.data.source.RecipeRepository;
import com.simranjit.android.bakingapp.features.recipelist.RecipeListContract.View;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

class RecipeListPresenter implements RecipeListContract.Presenter {

  private final RecipeRepository recipeRepository;
  private final RecipeListContract.View recipesView;
  private final CompositeDisposable disposableList;

  @Inject
  RecipeListPresenter(RecipeRepository recipeRepository,
      View recipesView) {
    this.recipeRepository = recipeRepository;
    this.recipesView = recipesView;

    disposableList = new CompositeDisposable();
  }

  @Inject
  void setupListeners() {
    recipesView.setPresenter(this);
  }

  @Override
  public void subscribe() {
    loadRecipesFromRepo(false, null);
  }

  @Override
  public void unsubscribe() {
    disposableList.clear();
  }

  @Override
  public void loadRecipesFromRepo(boolean forcedSync, RecipesIdlingResource resource) {

    if (forcedSync) {
      recipeRepository.markRepoAsSynced(false);
    }

    disposableList.clear();

    Disposable subscription = recipeRepository
        .getRecipes()
        .doOnSubscribe(disposable -> {
          recipesView.showLoadingIndicator(true);
          if (resource != null) resource.setIdleState(false);
        })
        .subscribe(
            //OnNext
            recipeList -> {
              recipesView.showRecipeList(recipeList);
              recipeRepository.markRepoAsSynced(true);
              recipesView.showLoadingIndicator(false);
              if (resource != null) resource.setIdleState(true);
              if (forcedSync) recipesView.showCompletedMessage();
            },
            // OnError
            throwable -> {
              recipesView.showLoadingIndicator(false);
              recipesView.showErrorMessage();
              recipeRepository.markRepoAsSynced(false);
            });

    disposableList.add(subscription);
  }

  @Override
  public void openRecipeDetails(int recipeId) {
    recipesView.showRecipeDetails(recipeId);
  }
}
