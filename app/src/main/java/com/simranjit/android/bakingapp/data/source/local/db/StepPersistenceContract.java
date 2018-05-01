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
import android.provider.BaseColumns;

public class StepPersistenceContract {

  private StepPersistenceContract() {
  }

  public static abstract class StepEntry implements BaseColumns {

    public static final String TABLE_NAME = "steps";

    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String COLUMN_STEP_ID = "step_id";
    public static final String COLUMN_SHORT_DESC = "short_desc";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_VIDEO_URL = "video";
    public static final String COLUMN_THUMB_URL = "thumb";
  }

  static final String SQL_QUERY_CREATE =
      "CREATE TABLE " + StepEntry.TABLE_NAME + " ("
          + StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
          + StepEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL,"
          + StepEntry.COLUMN_STEP_ID + " INTEGER NOT NULL,"
          + StepEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL,"
          + StepEntry.COLUMN_DESC + " TEXT NOT NULL,"
          + StepEntry.COLUMN_VIDEO_URL + " TEXT NOT NULL,"
          + StepEntry.COLUMN_THUMB_URL + " TEXT NOT NULL"
          + ")";
}
