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
import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.simranjit.android.bakingapp.BakingApp;
import com.simranjit.android.bakingapp.R;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.Set;
import javax.inject.Inject;
import timber.log.Timber;

public class WidgetConfigurationActivity extends AppCompatActivity {

  @Inject
  WidgetDataHelper widgetDataHelper;

  private CompositeDisposable disposableList;
  private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

  @BindView(R.id.radioGroup)
  RadioGroup namesRadioGroup;

  @BindString(R.string.widget_config_no_data)
  String noDataErrorMessage;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setResult(RESULT_CANCELED);
    setContentView(R.layout.widget_configuration_activity);
    ButterKnife.bind(this);

    disposableList = new CompositeDisposable();

    DaggerWidgetDataHelperComponent.builder()
        .recipeRepositoryComponent(
            ((BakingApp) getApplication()).getRecipeRepositoryComponent())
        .build()
        .inject(this);

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();

    if (extras != null) {
      appWidgetId = extras.getInt(
          AppWidgetManager.EXTRA_APPWIDGET_ID,
          AppWidgetManager.INVALID_APPWIDGET_ID);

      if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
        finish();
      }
    }

    Set<String> names = widgetDataHelper.getRecipeNamesFromPrefs();

    if (names.size() == 0) {
      Toast.makeText(this, noDataErrorMessage, Toast.LENGTH_SHORT).show();
      finish();
    }

    // Fill the radioGroup
    int currentIndex = 0;

    for (String name : names) {
      AppCompatRadioButton button = new AppCompatRadioButton(this);
      button.setText(name);
      button.setId(currentIndex++);
      setupRadioButtonColor(button);
      namesRadioGroup.addView(button);
    }

    // Check the first item when loaded
    if (namesRadioGroup.getChildCount() > 0) {
      ((AppCompatRadioButton) namesRadioGroup.getChildAt(0)).setChecked(true);
    }
  }

  @OnClick(R.id.button)
  public void onOkButtonClick() {

    int checkedItemId = namesRadioGroup.getCheckedRadioButtonId();
    String recipeName = ((AppCompatRadioButton) namesRadioGroup
        .getChildAt(checkedItemId)).getText().toString();

    widgetDataHelper.saveRecipeNameToPrefs(appWidgetId, recipeName);

    Context context = getApplicationContext();
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

    Disposable subscription = widgetDataHelper
        .getIngredientsList(recipeName)
        .subscribe(
            // OnNext
            ingredients ->
                WidgetProvider
                    .updateAppWidgetContent(context, appWidgetManager, appWidgetId, recipeName,
                        ingredients),
            // OnError
            throwable ->
                Timber.d("Error: unable to populate widget data."));

    disposableList.add(subscription);

    Intent resultValue = new Intent();
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    setResult(RESULT_OK, resultValue);
    finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposableList.clear();
  }

  @SuppressLint("RestrictedApi")
  private void setupRadioButtonColor(AppCompatRadioButton button) {

    int color = ContextCompat.getColor(this, R.color.colorPrimary);

    ColorStateList colorStateList = new ColorStateList(
        new int[][]{
            new int[]{-android.R.attr.state_checked},
            new int[]{android.R.attr.state_checked}
        },
        new int[]{
            color, color
        }
    );
    button.setSupportButtonTintList(colorStateList);
  }
}
