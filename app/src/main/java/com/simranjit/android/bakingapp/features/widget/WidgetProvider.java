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

package com.simranjit.android.bakingapp.features.widget;

//Created By Simranjit Singh 2018
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import com.simranjit.android.bakingapp.BakingApp;
import com.simranjit.android.bakingapp.R;
import com.simranjit.android.bakingapp.data.model.Ingredient;
import com.simranjit.android.bakingapp.utils.StringUtils;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class WidgetProvider extends AppWidgetProvider {

  @Inject
  WidgetDataHelper widgetDataHelper;

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);

    Timber.d("onUpdate");

    DaggerWidgetDataHelperComponent.builder()
        .recipeRepositoryComponent(
            ((BakingApp) context.getApplicationContext()).getRecipeRepositoryComponent())
        .build()
        .inject(this);

    for (int appWidgetId : appWidgetIds) {
      String recipeName = widgetDataHelper.getRecipeNameFromPrefs(appWidgetId);

      widgetDataHelper
          .getIngredientsList(recipeName)
          .take(1)
          .subscribe(
              // OnNext
              ingredients ->
                  WidgetProvider
                      .updateAppWidgetContent(context, appWidgetManager, appWidgetId, recipeName,
                          ingredients),
              // OnError
              throwable ->
                  Timber.d("Error: unable to populate widget data."));
    }
  }

  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    super.onDeleted(context, appWidgetIds);

    DaggerWidgetDataHelperComponent.builder()
        .recipeRepositoryComponent(
            ((BakingApp) context.getApplicationContext()).getRecipeRepositoryComponent())
        .build()
        .inject(this);

    for (int appWidgetId : appWidgetIds) {
      widgetDataHelper.deleteRecipeFromPrefs(appWidgetId);
    }
  }

  public static void updateAppWidgetContent(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId, String recipeName, List<Ingredient> ingredients) {

    Timber.d("updateAppWidgetContent call...");
    Timber.d("id: " + appWidgetId + ", name: " + recipeName + "ingredients: " + ingredients.size());

    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_list);
    views.setTextViewText(R.id.widget_recipe_name, recipeName);
    views.removeAllViews(R.id.widget_ingredients_container);

    for (Ingredient ingredient : ingredients) {
      RemoteViews ingredientView = new RemoteViews(context.getPackageName(),
          R.layout.widget_ingredients_list_item);

      String line = StringUtils.formatIngdedient(
          context, ingredient.ingredient(), ingredient.quantity(), ingredient.measure());

      ingredientView.setTextViewText(R.id.widget_ingredient_name, line);
      views.addView(R.id.widget_ingredients_container, ingredientView);
    }

    appWidgetManager.updateAppWidget(appWidgetId, views);
  }
}