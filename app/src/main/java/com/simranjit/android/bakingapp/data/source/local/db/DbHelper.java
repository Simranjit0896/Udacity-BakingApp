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

package com.simranjit.android.bakingapp.data.source.local.db;

//Created By Simranjit Singh 2018
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.simranjit.android.bakingapp.data.source.local.db.IngredientPersistenceContract.IngredientEntry;
import com.simranjit.android.bakingapp.data.source.local.db.RecipePersistenceContract.RecipeEntry;
import com.simranjit.android.bakingapp.data.source.local.db.StepPersistenceContract.StepEntry;

public class DbHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "recipes.db";
  private static final int DATABASE_VERSION = 1;

  DbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(IngredientPersistenceContract.SQL_QUERY_CREATE);
    db.execSQL(StepPersistenceContract.SQL_QUERY_CREATE);
    db.execSQL(RecipePersistenceContract.SQL_QUERY_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
    onCreate(db);
  }
}
