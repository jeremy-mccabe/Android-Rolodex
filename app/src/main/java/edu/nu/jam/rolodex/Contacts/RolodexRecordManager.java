package edu.nu.jam.rolodex.Contacts;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import edu.nu.jam.rolodex.Data.RolodexRecord;
import edu.nu.jam.rolodex.MainActivity;

public final class RolodexRecordManager
{
	private static final String TAG = RolodexRecordManager.class.getName();

	private static List<RolodexRecord> rolodexRecordList = new LinkedList<>();
	private static DatabaseManager dbManager = MainActivity.dbManager;

	//// SHARED PREFERENCES PORTION: \\\\
	public static List<RolodexRecord> getAllContactsFromSharedPreferences(Context appContext)
	{
		/*
		// This method retrieves all contacts from SharedPreferences.
		// The array is used in each iteration to store the converted
		// toString() RolodexRecord information to an array of either
		// first name & last name & phone number, or first name &
		// middle name & last name & phone number, then it is used with
		// the respective overload constructors.
		*/

		int size = SharedPreferencesManager.getInstance(appContext).getSize();

		for (int i = 0; i < size; i++)
		{
			String contact = SharedPreferencesManager.getInstance(appContext).getRolodex(Integer.toString(i));
			RolodexRecord rolodexRecord;
			String[] arr = contact.split("\\s+");

			// Use case: No middle name present:
			if (arr.length == 3)
			{
				rolodexRecord = new RolodexRecord(arr[0], arr[1], arr[2]);
				rolodexRecordList.add(rolodexRecord);
			}
			// Use case: Middle name is present:
			else if (arr.length == 4)
			{
				rolodexRecord = new RolodexRecord(arr[0], arr[1], arr[2], arr[3]);
				rolodexRecordList.add(rolodexRecord);
			}
		}

		return rolodexRecordList;
	}

	public static boolean hasDuplicateInSharedPreferences(Context context, RolodexRecord rolodexRecord)
	{
		int size = SharedPreferencesManager.getInstance(context).getSize();

		for (int i = 0; i < size; i++)
		{
			if (SharedPreferencesManager.getInstance(context).getRolodex(Integer.toString(i)).equals(rolodexRecord.toString()))
				return true;
		}

		return false;
	}

	//// DATABASE PORTION: \\\\
	public static List<RolodexRecord> getAllContactsFromDatabase()
	{
		/*
		// This method functions similarly to the corresponding method
		// in SharedPreferences defined above.
		*/

		Log.d(TAG, "getAllContactsFromDatabase:  Displaying data in the RecyclerView.");

		Cursor cursor = dbManager.getData();
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			String contact = cursor.getString(1);
			RolodexRecord rolodexRecord;

			String[] arr = contact.split("\\s+");

			if (arr.length == 3)
			{
				rolodexRecord = new RolodexRecord(arr[0], arr[1], arr[2]);
				rolodexRecordList.add(rolodexRecord);
			}
			else if (arr.length == 4)
			{
				rolodexRecord = new RolodexRecord(arr[0], arr[1], arr[2], arr[3]);
				rolodexRecordList.add(rolodexRecord);
			}

			cursor.moveToNext();
		}

		cursor.close();
		return rolodexRecordList;

	}

	public static boolean hasDuplicateInDatabase(RolodexRecord rolodexRecord)
	{
		Cursor data = dbManager.getData();

		while (data.moveToNext())
		{
			String contact = data.getString(1);
			if (contact.equals(rolodexRecord.toString()))
				return true;
		}

		return false;
	}
}
