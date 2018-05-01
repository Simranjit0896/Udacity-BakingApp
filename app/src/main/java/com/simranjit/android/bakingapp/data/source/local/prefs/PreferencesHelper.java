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

package com.simranjit.android.bakingapp.data.source.local.prefs;

//Created By Simranjit Singh 2018
import android.content.Context;
import android.content.SharedPreferences;
import com.simranjit.android.bakingapp.ApplicationContext;
import com.simranjit.android.bakingapp.data.model.Recipe;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

  private static final String PREFS_FILE_NAME = "baking_app_prefs";
  private static final String PREFERENCE_SYNCED = "baking_app_synced";
  private static final String PREFERENCE_RECIPES = "baking_app_recipes";
  private static final String PREFERENCE_CHOSEN_RECIPE = "baking_app_widget_chosen_recipe_";

  private final SharedPreferences sharedPreferences;

  @Inject
  public PreferencesHelper(@ApplicationContext Context context) {
    sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
  }

  public void setRecipeListSynced(boolean flag) {
    sharedPreferences.edit().putBoolean(PREFERENCE_SYNCED, flag).apply();
  }

  public boolean isRecipeListSynced() {
    return sharedPreferences.getBoolean(PREFERENCE_SYNCED, false);
  }

  @SuppressWarnings("Convert2streamapi")
  public void saveRecipeNamesList(List<Recipe> recipes) {

    Set<String> values = new HashSet<>();

    for (Recipe recipe : recipes) {
      values.add(recipe.name());
    }
    sharedPreferences.edit().putStringSet(PREFERENCE_RECIPES, values).apply();
  }

  public Set<String> getRecipeNamesList() {
    return sharedPreferences.getStringSet(PREFERENCE_RECIPES, new HashSet<>(0));
  }

  public String getChosenRecipeName(int keySuffix) {
    return sharedPreferences.getString(PREFERENCE_CHOSEN_RECIPE + keySuffix, "");
  }

  public void saveChosenRecipeName(int keySuffix, String name) {
    sharedPreferences.edit().putString(PREFERENCE_CHOSEN_RECIPE + keySuffix, name).apply();
  }

  public void deleteRecipeName(int keySuffix) {
    sharedPreferences.edit().remove(PREFERENCE_CHOSEN_RECIPE + keySuffix).apply();
  }
}
