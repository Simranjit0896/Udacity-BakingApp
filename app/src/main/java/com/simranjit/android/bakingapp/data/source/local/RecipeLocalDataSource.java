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

package com.simranjit.android.bakingapp.data.source.local;

//Created By Simranjit Singh 2018
import com.simranjit.android.bakingapp.data.model.Ingredient;
import com.simranjit.android.bakingapp.data.model.Recipe;
import com.simranjit.android.bakingapp.data.model.Step;
import com.simranjit.android.bakingapp.data.source.RecipeDataSource;
import com.simranjit.android.bakingapp.data.source.local.db.IngredientPersistenceContract.IngredientEntry;
import com.simranjit.android.bakingapp.data.source.local.db.RecipePersistenceContract.RecipeEntry;
import com.simranjit.android.bakingapp.data.source.local.db.StepPersistenceContract.StepEntry;
import com.simranjit.android.bakingapp.utils.DbUtils;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;

@Singleton
public class RecipeLocalDataSource implements RecipeDataSource {

  private final BriteDatabase databaseHelper;

  @Inject
  public RecipeLocalDataSource(BriteDatabase briteDatabase) {
    this.databaseHelper = briteDatabase;
  }

  @Override
  public Observable<List<Recipe>> getRecipes() {

    rx.Observable<List<Recipe>> listObservable = databaseHelper
        .createQuery(RecipeEntry.TABLE_NAME, DbUtils.getSelectAllQuery(RecipeEntry.TABLE_NAME))
        .mapToOne(DbUtils::recipesFromCursor);

    return RxJavaInterop.toV2Observable(listObservable);
  }

  @Override
  public Observable<List<Ingredient>> getRecipeIngredients(int recipeId) {

    rx.Observable<List<Ingredient>> listObservable = databaseHelper
        .createQuery(IngredientEntry.TABLE_NAME,
            DbUtils.getSelectByIdQuery(IngredientEntry.TABLE_NAME,
                IngredientEntry.COLUMN_RECIPE_ID),
            String.valueOf(recipeId))
        .mapToOne(DbUtils::ingredientsFromCursor);

    return RxJavaInterop.toV2Observable(listObservable);
  }

  @Override
  public Observable<List<Ingredient>> getRecipeIngredients(String recipeName) {
    return getRecipes()
        .flatMap(Observable::fromIterable)
        .filter(recipe -> Objects.equals(recipe.name(), recipeName))
        .map(Recipe::id)
        .flatMap(this::getRecipeIngredients);
  }

  @Override
  public Observable<List<Step>> getRecipeSteps(int recipeId) {

    rx.Observable<List<Step>> listObservable = databaseHelper
        .createQuery(StepEntry.TABLE_NAME,
            DbUtils.getSelectByIdQuery(StepEntry.TABLE_NAME,
                StepEntry.COLUMN_RECIPE_ID),
            String.valueOf(recipeId))
        .mapToOne(DbUtils::stepsFromCursor);

    return RxJavaInterop.toV2Observable(listObservable);
  }

  @Override
  public void saveRecipes(List<Recipe> recipes) {

    BriteDatabase.Transaction transaction = databaseHelper.newTransaction();

    try {
      deleteAllRecipes();

      for (Recipe recipe : recipes) {

        int id = recipe.id();

        for (Ingredient ingredient : recipe.ingredients()) {
          databaseHelper.insert(IngredientEntry.TABLE_NAME,
              DbUtils.ingredientToContentValues(ingredient, id));
        }

        for (Step step : recipe.steps()) {
          databaseHelper.insert(StepEntry.TABLE_NAME,
              DbUtils.stepToContentValues(step, id));
        }

        databaseHelper.insert(RecipeEntry.TABLE_NAME,
            DbUtils.recipeToContentValues(recipe));
      }

      transaction.markSuccessful();

    } finally {
      transaction.end();
    }
  }

  private void deleteAllRecipes() {
    databaseHelper.delete(RecipeEntry.TABLE_NAME, null);
    databaseHelper.delete(StepEntry.TABLE_NAME, null);
    databaseHelper.delete(IngredientEntry.TABLE_NAME, null);
  }
}
