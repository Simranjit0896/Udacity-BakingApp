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

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.simranjit.android.bakingapp.R;
import com.simranjit.android.bakingapp.data.model.Ingredient;
import com.simranjit.android.bakingapp.data.model.Step;
import com.simranjit.android.bakingapp.features.recipestep.RecipeStepActivity;
import com.simranjit.android.bakingapp.features.recipestep.RecipeStepSinglePageFragment;
import com.simranjit.android.bakingapp.utils.FragmentUtils;
import com.simranjit.android.bakingapp.utils.StringUtils;
import com.simranjit.android.bakingapp.utils.TextViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailsFragment extends Fragment implements RecipeDetailsContract.View {

    @BindView(R.id.recipe_details_ingredients)
    TextView recipeDetailsIngredients;
    @BindView(R.id.recipe_details_steps)
    RecyclerView recipeDetailsRecyclerView;

    @BindBool(R.bool.two_pane_mode)
    boolean twoPaneMode;
    @BindString(R.string.loading_data_error)
    String errorMessage;
    @BindString(R.string.recipe_details_ingredients_header)
    String ingredientsListHeader;

    Unbinder unbinder;

    private RecipeDetailsContract.Presenter recipeDetailsPresenter;
    private RecipeDetailsAdapter recipeDetailsAdapter;
    private int recipeId;
    private LinearLayoutManager layoutManager;
    public static int index = -1;
    public static int top = -1;
    Parcelable state;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();


    public RecipeDetailsFragment() {
    }

    public static RecipeDetailsFragment newInstance(int recipeId) {
        Bundle arguments = new Bundle();
        arguments.putInt(RecipeDetailsActivity.EXTRA_RECIPE_ID, recipeId);
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeId = getArguments().getInt(RecipeDetailsActivity.EXTRA_RECIPE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        recipeDetailsAdapter = new RecipeDetailsAdapter(new ArrayList<>(0),
                stepId -> recipeDetailsPresenter.openStepDetails(stepId));

        layoutManager = new LinearLayoutManager(getContext());
        recipeDetailsRecyclerView.setNestedScrollingEnabled(true);  // For NestedScrollView
        recipeDetailsRecyclerView.setLayoutManager(layoutManager);
        recipeDetailsRecyclerView.setHasFixedSize(true);
        recipeDetailsRecyclerView.setAdapter(recipeDetailsAdapter);

        recipeDetailsRecyclerView
                .addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        layoutManager.scrollToPosition(0);

        if (twoPaneMode) {
            recipeDetailsPresenter.fetchStepData(0);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recipeDetailsPresenter.subscribe();
        //set recyclerview position
        if (index != -1) {
            layoutManager.scrollToPositionWithOffset(index, top);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        recipeDetailsPresenter.unsubscribe();
        index = layoutManager.findFirstVisibleItemPosition();
        View v = recipeDetailsRecyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - recipeDetailsRecyclerView.getPaddingTop());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setPresenter(RecipeDetailsContract.Presenter presenter) {
        this.recipeDetailsPresenter = presenter;
    }

    @Override
    public void showIngredientsList(List<Ingredient> ingredients) {

        StringBuilder sb = new StringBuilder();
        sb.append(ingredientsListHeader);

        for (Ingredient ingredient : ingredients) {

            String name = ingredient.ingredient();
            float quantity = ingredient.quantity();
            String measure = ingredient.measure();

            sb.append("\n");
            sb.append(StringUtils.formatIngdedient(getContext(), name, quantity, measure));
        }

        TextViewUtils.setTextWithSpan(recipeDetailsIngredients, sb.toString(), ingredientsListHeader,
                new StyleSpan(Typeface.BOLD));
    }

    @Override
    public void showStepsList(List<Step> steps) {
        recipeDetailsAdapter.refreshStepsList(steps);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRecipeNameInActivityTitle(String recipeName) {
        getActivity().setTitle(recipeName);
    }

    @Override
    public void showStepDetails(int stepId) {

        if (twoPaneMode) {
            recipeDetailsPresenter.fetchStepData(stepId);
        } else {
            startActivity(RecipeStepActivity.prepareIntent(getContext(), recipeId, stepId));
        }
    }

    @Override
    public void refreshStepContainerFragment(String desc, String videoUrl, String imageUrl) {

        RecipeStepSinglePageFragment fragment =
                RecipeStepSinglePageFragment.newInstance(desc, videoUrl, imageUrl);

        FragmentUtils.replaceFragmentIn(
                getChildFragmentManager(),
                fragment,
                R.id.recipe_step_container);
    }
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        int scrollIndex = layoutManager.findFirstVisibleItemPosition();
//        View v = recipeDetailsRecyclerView.getChildAt(0);
//        int scrollTop = (v == null) ? 0 : (v.getTop() - recipeDetailsRecyclerView.getPaddingTop());
//        outState.putInt("scrollIndex", scrollIndex);
//        outState.putInt("scrollTop", scrollTop);
//    }

    /**
     * This is a method for Fragment.
     * You can do the same in onCreate or onRestoreInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            recipeDetailsPresenter.subscribe();
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recipeDetailsRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                recipeDetailsRecyclerView.getLayoutManager().onSaveInstanceState());
    }
}