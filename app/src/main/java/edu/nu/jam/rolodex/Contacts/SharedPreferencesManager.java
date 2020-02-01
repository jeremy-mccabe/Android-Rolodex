package edu.nu.jam.rolodex.Contacts;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager
{

	private static final String TAG = SharedPreferencesManager.class.getName();

	private static SharedPreferencesManager instance;
	private static SharedPreferences pref;
	private static final String PREF_NAME = "Rolodex_Shared_Preferences";
	private static final String SIZE = "Size";

	private SharedPreferencesManager() { }

	public static SharedPreferencesManager getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new SharedPreferencesManager();
			pref = context.getSharedPreferences(PREF_NAME, 0);
			SharedPreferences.Editor editor = pref.edit();
			if (pref.getInt(SIZE, -1) == -1)
			{
				editor.putInt(SIZE, 0);
				editor.commit();
			}
		}
		return instance;
	}

	public int getSize()
	{
		return pref.getInt(SIZE, -1);
	}

	public void setSize(int size)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(SIZE, size);
		editor.commit();
	}

	public String getRolodex(String key)
	{
		return pref.getString(key, null);
	}

	public void addRolodex(String index, String data)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(index, data);
		editor.commit();
	}

	public void editRolodex(String index, String data)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(index, data);
		editor.commit();
	}

	public void deleteRolodex(int targetIndex)
	{
		/*
		// This method finds a targeted index to overwrite, and functions
		// by shifting all other values "downward" in SharedPreferences,
		// until the target is overwritten. Then it simply removes the last
		// "element" in SharedPreferences, successfully eliminating the undesired
		// entry while reorganizing the entry's indexed order in SharedPreferences.
		*/

		int size = pref.getInt(SIZE, -1);
		SharedPreferences.Editor editor = pref.edit();

		String tempA;
		String tempB = null;

		// Use case: Target for deletion is the last "element":
		if (targetIndex == size - 1)
		{
			// Reset size of SharedPreferences and remove the last element:
			size--;
			editor.remove(Integer.toString(size));
			editor.putInt(SIZE, size);
			editor.commit();

			return;
		}

		// Use case: Target for deletion is the second-to-last "element":
		if (targetIndex == size - 2)
		{
			// Change the second-to-last "element" to the last "element":
			editor.putString(Integer.toString(size - 2), pref.getString(Integer.toString(size - 1), null));
			// Reset size of SharedPreferences and remove the last element:
			size--;
			editor.remove(Integer.toString(size));
			editor.putInt(SIZE, size);
			editor.commit();

			return;
		}

		// Use case: Normal situation:
		for (int i = (size - 2); i >= targetIndex; i--)
		{
			// First iteration only:
			if (i == (size - 2))
			{
				tempA = pref.getString(Integer.toString(i), null);
				tempB = pref.getString(Integer.toString(i + 1), null);
				editor.putString(Integer.toString(i), tempB);
				tempB = tempA;

				editor.commit();
				continue;
			}

			// Following iterations:
			tempA = pref.getString(Integer.toString(i), null);
			editor.putString(Integer.toString(i), tempB);
			tempB = tempA;

			editor.commit();
		}

		// Reset size of SharedPreferences and remove the last "element":
		size--;
		editor.remove(Integer.toString(size));
		editor.putInt(SIZE, size);
		editor.commit();
	}
}