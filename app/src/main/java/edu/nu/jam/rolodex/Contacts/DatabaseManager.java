package edu.nu.jam.rolodex.Contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper
{
	private static final String TAG = DatabaseManager.class.getName();

	public static final String TABLE_NAME = "rolodex";
	public static final String COL_ID = "ID";
	public static final String COL_CONTACT = "contact";

	public DatabaseManager(Context context)
	{
		super(context, TABLE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_CONTACT + " TEXT NOT NULL)";
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i1)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public boolean addData(String item)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_CONTACT, item);

		Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

		long result = db.insert(TABLE_NAME, null, contentValues);

		// Successfully added to database?:
		return (result != -1);

	}

	public boolean deleteData(String item)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		Log.d(TAG, "deleteData: Removing " + item + " from " + TABLE_NAME);

		long result = db.delete(TABLE_NAME, COL_CONTACT + " = \"" + item + "\"", null);

		// Successfully deleted from database?:
		return (result != -1);

	}

	public boolean updateData(String oldItem, String newItem)
	{
		Log.d(TAG, "updateData: Updating " + oldItem + " to " + newItem + " in " + TABLE_NAME);

		boolean isPreviousDataRemoved = deleteData(oldItem);
		boolean isNewDataAdded = addData(newItem);

		// Successfully added to and deleted from database?:
		return isPreviousDataRemoved && isNewDataAdded;

	}

	public Cursor getData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_NAME;

		return db.rawQuery(query, null);
	}

	// Used to zero out database for debugging:
	public void clearDatabase(boolean enabled)
	{
		if (enabled)
		{
			SQLiteDatabase db = this.getWritableDatabase();
			String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + " > -1";
			db.execSQL(query);
		}
	}
}
