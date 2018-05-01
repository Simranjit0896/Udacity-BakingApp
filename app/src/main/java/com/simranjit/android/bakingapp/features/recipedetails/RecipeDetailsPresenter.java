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
import com.simranjit.android.bakingapp.data.source.RecipeRepository;
import com.simranjit.android.bakingapp.features.recipedetails.RecipeDetailsContract.View;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

class RecipeDetailsPresenter implements RecipeDetailsContract.Presenter {

  private final RecipeRepository recipeRepository;
  private final RecipeDetailsContract.View detailsView;
  private final int recipeId;
  private final CompositeDisposable disposableList;

  @Inject
  RecipeDetailsPresenter(
      RecipeRepository recipeRepository,
      View detailsView, int recipeId) {
    this.recipeRepository = recipeRepository;
    this.detailsView = detailsView;
    this.recipeId = recipeId;

    disposableList = new CompositeDisposable();
  }

  @Inject
  void setupListeners() {
    detailsView.setPresenter(this);
  }

  @Override
  public void subscribe() {
    loadRecipeNameFromRepo();
    loadIngredientsFromRepo();
    loadStepsFromRepo();
  }

  @Override
  public void unsubscribe() {
    disposableList.clear();
  }

  @Override
  public void loadRecipeNameFromRepo() {
    Disposable subscription = recipeRepository
        .getRecipes()
        .flatMap(Observable::fromIterable)
        .filter(recipe -> recipe.id() == recipeId)
        .subscribe(
            // OnNext
            recipe -> detailsView.showRecipeNameInActivityTitle(recipe.name()),
            // OnError
            throwable -> detailsView.showErrorMessage());

    disposableList.add(subscription);
  }

  @Override
  public void loadIngredientsFromRepo() {

    Disposable subscription = recipeRepository
        .getRecipeIngredients(recipeId)
        .subscribe(
            // OnNext
            detailsView::showIngredientsList,
            // OnError
            throwable -> detailsView.showErrorMessage());

    disposableList.add(subscription);
  }

  @Override
  public void loadStepsFromRepo() {

    Disposable subscription = recipeRepository
        .getRecipeSteps(recipeId)
        .subscribe(
            // OnNext
            detailsView::showStepsList,
            // OnError
            throwable -> detailsView.showErrorMessage());

    disposableList.add(subscription);
  }

  @Override
  public void openStepDetails(int stepId) {
    detailsView.showStepDetails(stepId);
  }

  @Override
  public void fetchStepData(int stepId) {

    Disposable subscription = recipeRepository
        .getRecipeSteps(recipeId)
        .flatMap(Observable::fromIterable)
        .filter(step -> step.id() == stepId)
        .subscribe(
            // OnNext
            step ->
                detailsView.refreshStepContainerFragment(
                    step.description(),
                    step.videoURL(),
                    step.thumbnailURL()),
            // OnError
            throwable -> detailsView.showErrorMessage());

    disposableList.add(subscription);
  }
}
