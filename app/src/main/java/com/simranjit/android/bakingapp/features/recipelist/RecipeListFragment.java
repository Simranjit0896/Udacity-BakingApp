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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.simranjit.android.bakingapp.R;
import com.simranjit.android.bakingapp.data.model.Recipe;
import com.simranjit.android.bakingapp.features.recipedetails.RecipeDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipeListFragment extends Fragment implements RecipeListContract.View {

  private static final String SAVED_SCROLL_POSITION = "SAVED_SCROLL_POSITION";

  @BindView(R.id.recipe_list_recycler_view)
  RecyclerView recipeListRecyclerView;
  @BindView(R.id.recipe_list_progress_bar)
  ProgressBar recipeListProgressBar;

  @BindInt(R.integer.grid_column_count)
  int gridColumnCount;
  @BindString(R.string.recipe_list_sync_completed)
  String syncCompletedMessage;
  @BindString(R.string.recipe_list_connection_error)
  String connectionErrorMessage;

  Unbinder unbinder;

  private RecipeListContract.Presenter recipeListPresenter;
  private RecipeListAdapter recipeListAdapter;
  private GridLayoutManager layoutManager;

  public RecipeListFragment() {
  }

  public static RecipeListFragment newInstance() {
    return new RecipeListFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
    unbinder = ButterKnife.bind(this, view);

    recipeListAdapter = new RecipeListAdapter(
        new ArrayList<>(0),
        recipeId -> recipeListPresenter.openRecipeDetails(recipeId)
    );

    recipeListAdapter.setHasStableIds(true);

    layoutManager = new GridLayoutManager(getContext(), gridColumnCount);
    recipeListRecyclerView.setLayoutManager(layoutManager);
    recipeListRecyclerView.setHasFixedSize(true);
    recipeListRecyclerView.setAdapter(recipeListAdapter);

    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_SCROLL_POSITION)) {
      int position = savedInstanceState.getInt(SAVED_SCROLL_POSITION);
      new Handler().postDelayed(() -> layoutManager.scrollToPositionWithOffset(position, 0), 200);
    }
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    int position = layoutManager.findLastVisibleItemPosition();
    outState.putInt(SAVED_SCROLL_POSITION, position);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onResume() {
    super.onResume();
    recipeListPresenter.subscribe();
  }

  @Override
  public void onPause() {
    super.onPause();
    recipeListPresenter.unsubscribe();
  }

  @Override
  public void setPresenter(@NonNull RecipeListContract.Presenter presenter) {
    this.recipeListPresenter = presenter;
  }

  @Override
  public void showRecipeList(List<Recipe> recipeList) {
    recipeListAdapter.refreshRecipeList(recipeList);
  }

  @Override
  public void showLoadingIndicator(boolean show) {
    setViewVisibility(recipeListRecyclerView, !show);
    setViewVisibility(recipeListProgressBar, show);
  }

  @Override
  public void showCompletedMessage() {
    Toast.makeText(getContext(), syncCompletedMessage, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showErrorMessage() {
    Toast.makeText(getContext(), connectionErrorMessage, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showRecipeDetails(int recipeId) {
    startActivity(RecipeDetailsActivity.prepareIntent(getContext(), recipeId));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void setViewVisibility(View view, boolean visible) {
    if (visible) {
      view.setVisibility(View.VISIBLE);
    } else {
      view.setVisibility(View.INVISIBLE);
    }
  }
}
