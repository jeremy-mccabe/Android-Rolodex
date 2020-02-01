package edu.nu.jam.rolodex.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import edu.nu.jam.rolodex.MainActivity;
import edu.nu.jam.rolodex.MainActivity.Mode;
import edu.nu.jam.rolodexRecord.R;


public class PersistenceSelectionDialog extends DialogFragment
{
	private Context context;
	private RadioButton sharedPreferencesRadioButton;
	private RadioButton databaseRadioButton;
	private Mode mode;

	public PersistenceSelectionDialog(Context context, Mode mode)
	{
		this.context = context;
		this.mode = mode;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.data_persistence_dialog_fragment, null);
		builder.setView(dialogView);
		builder.setTitle(R.string.persist_title);

		bindControls(dialogView);
		registerHandlers(builder);
		setPersistenceMode();

		return builder.create();
	}

	private View.OnClickListener sharedPreferenceRadioListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			databaseRadioButton.setChecked(false);
		}
	};

	private View.OnClickListener databaseRadioListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			sharedPreferencesRadioButton.setChecked(false);
		}
	};

	private void bindControls(View dialogView)
	{
		sharedPreferencesRadioButton = dialogView.findViewById(R.id.sharedPreferencesRadioButton);
		databaseRadioButton = dialogView.findViewById(R.id.databaseRadioButton);
	}

	private void registerHandlers(AlertDialog.Builder builder)
	{
		// Retrieve persistence mode from shared preferences:
		SharedPreferences modeSaver = context.getSharedPreferences("MODE_SAVER", Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = modeSaver.edit();

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (sharedPreferencesRadioButton.isChecked())
				{
					MainActivity.mode = Mode.SHARED_PREFERENCES;

					editor.putString("mode", "shared_preferences");
					editor.commit();

					// refresh the recycler view
					((MainActivity)getActivity()).refreshRolodexRecyclerView();

					Toast.makeText(context, "Loading from SharedPreferences...", Toast.LENGTH_LONG).show();

				}
				else if (databaseRadioButton.isChecked())
				{
					MainActivity.mode = Mode.DATABASE;

					editor.putString("mode", "database");
					editor.commit();

					// refresh the recycler view
					((MainActivity)getActivity()).refreshRolodexRecyclerView();

					Toast.makeText(context, "Loading from SQLite database...", Toast.LENGTH_LONG).show();
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

	private void setPersistenceMode()
	{
		if (mode == Mode.SHARED_PREFERENCES)
		{
			sharedPreferencesRadioButton.setChecked(true);
			databaseRadioButton.setChecked(false);
		}
		else if (mode == Mode.DATABASE)
		{
			databaseRadioButton.setChecked(true);
			sharedPreferencesRadioButton.setChecked(false);
		}

		sharedPreferencesRadioButton.setOnClickListener(sharedPreferenceRadioListener);
		databaseRadioButton.setOnClickListener(databaseRadioListener);
	}
}
