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

package com.simranjit.android.bakingapp.features.recipestep;

//Created By Simranjit Singh 2018
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.simranjit.android.bakingapp.R;
import com.simranjit.android.bakingapp.data.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepFragment extends Fragment implements RecipeStepContract.View {

  @BindView(R.id.recipe_step_viewpager)
  ViewPager recipeStepViewPager;
  @BindView(R.id.recipe_step_tablayout)
  TabLayout recipeStepTabLayout;

  @BindString(R.string.loading_data_error)
  String errorMessage;
  @BindBool(R.bool.two_pane_mode)
  boolean isTwoPane;

  Unbinder unbinder;

  private RecipeStepContract.Presenter recipeStepPresenter;
  private RecipeStepPageAdapter viewPagerAdapter;

  int stepId;

  public RecipeStepFragment() {
  }

  public static RecipeStepFragment newInstance(int stepId) {
    Bundle arguments = new Bundle();
    arguments.putInt(RecipeStepActivity.EXTRA_STEP_ID, stepId);
    RecipeStepFragment fragment = new RecipeStepFragment();
    fragment.setArguments(arguments);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      stepId = getArguments().getInt(RecipeStepActivity.EXTRA_STEP_ID);
    } else {
      stepId = savedInstanceState.getInt(RecipeStepActivity.EXTRA_STEP_ID);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
    unbinder = ButterKnife.bind(this, view);

    viewPagerAdapter = new RecipeStepPageAdapter(getFragmentManager(), new ArrayList<>(0), getContext());
    recipeStepViewPager.setAdapter(viewPagerAdapter);
    setUpViewPagerListener();
    recipeStepTabLayout.setupWithViewPager(recipeStepViewPager);

    // Hide tabs on landscape not-twoPane mode
    int orientation = getResources().getConfiguration().orientation;

    if (orientation == Configuration.ORIENTATION_LANDSCAPE && !isTwoPane) {
      recipeStepTabLayout.setVisibility(View.GONE);
    }

    return view;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putInt(RecipeStepActivity.EXTRA_STEP_ID, stepId);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onResume() {
    super.onResume();
    recipeStepPresenter.subscribe();
  }

  @Override
  public void onPause() {
    super.onPause();
    recipeStepPresenter.unsubscribe();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void setPresenter(RecipeStepContract.Presenter presenter) {
    this.recipeStepPresenter = presenter;
  }

  @Override
  public void showStepsInViewpager(List<Step> steps) {
    viewPagerAdapter.setSteps(steps);
  }

  @Override
  public void showErrorMessage() {
    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void moveToCurrentStepPage() {
    recipeStepViewPager.setCurrentItem(stepId);
  }

  private void setUpViewPagerListener() {
    recipeStepViewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

      @Override
      public void onPageSelected(int position) {
        stepId = position;
      }

      @Override
      public void onPageScrollStateChanged(int state) {}
    });
  }
}
