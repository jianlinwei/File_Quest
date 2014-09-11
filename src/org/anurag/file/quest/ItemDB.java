/**
 * Copyright(c) 2014 DRAWNZER.ORG PROJECTS -> ANURAG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *                             
 *                             anurag.dev1512@gmail.com
 *
 */

package org.anurag.file.quest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * This class holds the record of files folders that are locked or added to favorite....
 * 
 * @author Anurag
 *
 */

public class ItemDB extends SQLiteOpenHelper{
	
	static String DBNAME = "ItemDB.db";
	

	public ItemDB(Context ctx) {
		// TODO Auto-generated constructor stub
		super(ctx,DBNAME,null,1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE ITEMS "+
				   "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
				   "FILEPATH TEXT,"+
				   "LOCKED INTEGER);");
		
		db.execSQL("CREATE TABLE FAVITEMS "+
				   "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
				   "FILEPATH TEXT,"+
				   "FAV INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase DB, int arg1, int arg2) {
		// TODO Auto-generated method stub
		DB.execSQL("DROP TABLE IF EXISTS ITEMS");
		DB.execSQL("DROP TABLE IF EXISTS FAVITEMS");
		onCreate(DB);
	}
	
	/**
	 * 
	 * @param PATH
	 * @param locked
	 * @param fav
	 */
	public void insertNodeToLock(String PATH,int locked){
		/**
		 * performing raw query to ensure that we are not making any 
		 * duplicate record of the items.. 
		 */		
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM ITEMS WHERE FILEPATH = ?",
				new String[]{PATH});
		if(cursor.getCount()==0){
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("FILEPATH", PATH);
			values.put("LOCKED", locked);
			//values.put("FAV", fav);
			db.insert("ITEMS", null , values);
		}		
	}
	
	/**
	 * 
	 * @param Path
	 * @return
	 */
	public boolean deleteLockedNode(String Path){
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("ITEMS", "FILEPATH = ?", new String[]{Path})>0;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean deleteAllLockedNodes(){
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("ITEMS", null , null) > 0;
	}
	
	/**
	 * 
	 * @param PATH
	 * @return
	 */
	public boolean isLocked(String PATH){
		Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM ITEMS WHERE FILEPATH = ?",
				new String[]{PATH});
		if(cursor.getCount()==0)
			return false;
		return true;
	}
}
