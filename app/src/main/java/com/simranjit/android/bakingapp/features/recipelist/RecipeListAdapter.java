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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simranjit.android.bakingapp.R;
import com.simranjit.android.bakingapp.data.model.Recipe;

import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

  private List<Recipe> recipeList;
  final OnRecipeClickListener recipeClickListener;

  RecipeListAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
    setRecipes(recipes);
    recipeClickListener = listener;
  }

  @Override
  public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_recipe_list_item, parent, false);

    return new RecipeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RecipeViewHolder holder, int position) {
    holder.bindTo(recipeList.get(position));
  }

  @Override
  public int getItemCount() {
    return recipeList.size();
  }

  @Override
  public long getItemId(int position) {
    return recipeList.get(position).id();
  }

  void refreshRecipeList(List<Recipe> recipes) {
    setRecipes(recipes);
    notifyDataSetChanged();
  }

  private void setRecipes(@NonNull List<Recipe> recipes) {
    recipeList = recipes;
  }

  class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.list_recipe_name)
    TextView recipeName;

    @BindView(R.id.list_recipe_servings)
    TextView recipeServings;

    @BindString(R.string.recipe_list_servings_text)
    String servingsText;

    private int currentId;

    RecipeViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(this);
    }

    void bindTo(@NonNull Recipe recipe) {

      currentId = recipe.id();

      String name = recipe.name();
      recipeName.setText(name);
      int servings = recipe.servings();
      recipeServings.setText(String.format(Locale.US, servingsText, servings));
    }

    @Override
    public void onClick(View v) {
      recipeClickListener.recipeClicked(currentId);
    }
  }

  interface OnRecipeClickListener {

    void recipeClicked(int recipeId);
  }
}
