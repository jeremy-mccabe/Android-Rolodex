package edu.nu.jam.rolodex.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.regex.Matcher;

import edu.nu.jam.rolodex.Data.RolodexRecord;
import edu.nu.jam.rolodex.MainActivity;
import edu.nu.jam.rolodex.MainActivity.Mode;

import java.util.regex.Pattern;

import edu.nu.jam.rolodex.Contacts.SharedPreferencesManager;
import edu.nu.jam.rolodex.Contacts.RolodexRecordManager;
import edu.nu.jam.rolodexRecord.R;


public class RolodexRecordDialog extends DialogFragment
{
	private Context context;
	private RolodexRecord rolodexRecordInformation;
	private List<RolodexRecord> rolodexRecordList;
	private RecyclerView view;
	private EditText firstNameEditText;
	private EditText lastNameEditText;
	private EditText middleNameEditText;
	private EditText phoneNumberEditText;
	private int index;
	private boolean isUpdatingRecord;
	private Mode mode;

	public RolodexRecordDialog(Context context, RolodexRecord rolodexRecordInformation, List<RolodexRecord> rolodexRecordList, RecyclerView view, int currentIndex, Mode mode)
	{
		this.context = context;
		this.rolodexRecordInformation = rolodexRecordInformation;
		this.rolodexRecordList = rolodexRecordList;
		this.view = view;
		this.index = currentIndex;
		this.mode = mode;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.rolodex_information_dialog_fragment, null);
		builder.setView(dialogView);

		// Sets dialog title based on new or editable passed RolodexRecord object:
		if (rolodexRecordInformation.getFirstName().equals(""))
		{
			builder.setTitle(getString(R.string.rolodex_add));
			isUpdatingRecord = false;
		}
		else
		{
			builder.setTitle(getString(R.string.rolodex_edit));
			isUpdatingRecord = true;
		}

		bindControls(dialogView);
		populateData();
		registerHandlers(builder);

		return builder.create();
	}

	private void bindControls(View dialogView)
	{
		firstNameEditText = dialogView.findViewById(R.id.firstNameEditText);
		lastNameEditText = dialogView.findViewById(R.id.lastNameEditText);
		middleNameEditText = dialogView.findViewById(R.id.middleNameEditText);
		phoneNumberEditText = dialogView.findViewById(R.id.phoneNumberEditText);
	}

	private void registerHandlers(AlertDialog.Builder builder)
	{
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (isValidInput())
				{
					// Get input data:
					String firstField = firstNameEditText.getText().toString();
					String lastField = lastNameEditText.getText().toString();
					String middleField = middleNameEditText.getText().toString();
					String digitField = phoneNumberEditText.getText().toString();

					rolodexRecordInformation.setFirstName(firstField);
					rolodexRecordInformation.setLastName(lastField);
					rolodexRecordInformation.setMiddleName(middleField);
					rolodexRecordInformation.setPhoneNumber(digitField);

					if (mode == Mode.SHARED_PREFERENCES)
					{
						// Check for duplicates:
						if (!RolodexRecordManager.hasDuplicateInSharedPreferences(context, rolodexRecordInformation))
						{
							// Add or Edit RolodexRecord in SharedPreferences:
							if (!isUpdatingRecord)
								addContactToSharedPreferences();
							else
								updateContactInSharedPreferences();
						}
						// Duplicate found:
						else
						{
							if (!isUpdatingRecord)
								Toast.makeText(context, "Cannot persist duplicated records.", Toast.LENGTH_LONG).show();
						}
					}
					else if (mode == Mode.DATABASE)
					{
						// Check for duplicates:
						if (!RolodexRecordManager.hasDuplicateInDatabase(rolodexRecordInformation))
						{
							// Add or Edit RolodexRecord in Database:
							if (!isUpdatingRecord)
								addContactToDatabase();
							else
								updateContactInDatabase();
						}
						// Duplicate found:
						else
						{
							if (!isUpdatingRecord)
								Toast.makeText(context, "Cannot persist duplicated records.", Toast.LENGTH_LONG).show();
						}
					}
				}
				// Invalid input:
				else
				{
					InvalidInputDialog notification = new InvalidInputDialog();
					notification.show(getFragmentManager(), "invalid_input_dialog_fragment");
				}
			}
		});

		builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// Do nothing.
			}
		});
	}

	private void populateData()
	{
		firstNameEditText.setText(rolodexRecordInformation.getFirstName());
		lastNameEditText.setText(rolodexRecordInformation.getLastName());
		middleNameEditText.setText(rolodexRecordInformation.getMiddleName());
		phoneNumberEditText.setText(rolodexRecordInformation.getPhoneNumber());
	}

	private boolean isValidInput()
	{
		// Get input data:
		String firstField = firstNameEditText.getText().toString();
		String lastField = lastNameEditText.getText().toString();
		String middleField = middleNameEditText.getText().toString();
		String digitField = phoneNumberEditText.getText().toString();

		// Regex values:
		String alphaRegex = "^[a-zA-Z]+$";
		Pattern alphaPattern = Pattern.compile(alphaRegex);
		String digitRegex = "^[0-9]+$";
		//String digitRegex = "^[2-9]\\d{2}-\\d{3}-\\d{4}$";
		Pattern digitPattern = Pattern.compile(digitRegex);

		// Matching:
		Matcher firstMatcher = alphaPattern.matcher(firstField);
		Matcher lastMatcher = alphaPattern.matcher(lastField);
		Matcher middleMatcher = alphaPattern.matcher(middleField);
		Matcher phoneMatcher = digitPattern.matcher(digitField);

		// Validation & return:
		if (!firstField.equals("") && firstMatcher.matches()
				&& !lastField.equals("") && lastMatcher.matches()
				&& (middleField.equals("") || middleMatcher.matches())
				&& !digitField.equals("") && digitField.length() == 10 && phoneMatcher.matches()
		) {
			rolodexRecordInformation.setFirstName(firstField);
			rolodexRecordInformation.setLastName(lastField);
			rolodexRecordInformation.setMiddleName(middleField);
			rolodexRecordInformation.setPhoneNumber(digitField);
			return true;
		}
		else
			return false;
	}

	private void addContactToSharedPreferences()
	{
		String targetIndex = Integer.toString(SharedPreferencesManager.getInstance(context).getSize());

		// Sets new Rolodex entry to the same index value as the total size of the SharedPreferences (ie: size = 5, new record is placed at key# 5):
		SharedPreferencesManager.getInstance(context).addRolodex(targetIndex, rolodexRecordInformation.toString());

		// Passes in the targetIndex to increment the total size of the SharedPreferences:
		SharedPreferencesManager.getInstance(context).setSize(Integer.parseInt(targetIndex) + 1);

		rolodexRecordList.add(rolodexRecordInformation);
		view.getAdapter().notifyDataSetChanged();
	}

	private void updateContactInSharedPreferences()
	{
		String indexStr = Integer.toString(index);
		SharedPreferencesManager.getInstance(context).editRolodex(indexStr, rolodexRecordInformation.toString());

		rolodexRecordList.set(index, rolodexRecordInformation);
		view.getAdapter().notifyDataSetChanged();
	}

	private void addContactToDatabase()
	{
		boolean insertData = MainActivity.dbManager.addData(rolodexRecordInformation.toString());

		if (insertData)
			Toast.makeText(context, "RolodexRecord entry successfully inserted!", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(context, "Problem inserting the RolodexRecord entry...", Toast.LENGTH_SHORT).show();

		rolodexRecordList.add(rolodexRecordInformation);
		view.getAdapter().notifyDataSetChanged();
	}

	private void updateContactInDatabase()
	{
		Cursor data = MainActivity.dbManager.getData();

		while (data.moveToNext())
		{
			String contact = data.getString(1);

			if (contact.equals(rolodexRecordInformation.toString()))
			{
				MainActivity.dbManager.updateData(contact, rolodexRecordInformation.toString());
			}
		}

		rolodexRecordList.set(index, rolodexRecordInformation);
		view.getAdapter().notifyDataSetChanged();
	}
}
