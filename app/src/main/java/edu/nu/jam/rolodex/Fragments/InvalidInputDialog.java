package edu.nu.jam.rolodex.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import edu.nu.jam.rolodexRecord.R;

public class InvalidInputDialog extends DialogFragment
{

	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.invalid_input_dialog_fragment, null);
		builder.setTitle(getString(R.string.invalid_title));
		builder.setMessage(getString(R.string.invalid_message));
		builder.setView(dialogView);

		registerHandlers(builder);

		return builder.create();
	}

	private void registerHandlers(AlertDialog.Builder builder)
	{
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				// Do nothing.
			}
		});
	}
}
