package edu.nu.jam.rolodex.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.nu.jam.rolodex.Contacts.SharedPreferencesManager;
import edu.nu.jam.rolodex.Data.RolodexRecord;
import edu.nu.jam.rolodex.MainActivity;
import edu.nu.jam.rolodexRecord.R;

public class DeleteConfirmationDialog extends DialogFragment
{
	private Context context;
	private List<RolodexRecord> rolodexRecordList;
	private RecyclerView rolodexRecyclerView;
	private int currentSelectedListItem;

	public DeleteConfirmationDialog(Context context, List<RolodexRecord> rolodexRecordList, RecyclerView rolodexRecyclerView, int currentSelectedListItem)
	{
		this.context = context;
		this.rolodexRecordList = rolodexRecordList;
		this.rolodexRecyclerView = rolodexRecyclerView;
		this.currentSelectedListItem = currentSelectedListItem;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.rolodex_deletion_dialog_fragment, null);
		builder.setTitle(getString(R.string.deletion_confirmation_title));
		builder.setMessage(getString(R.string.deletion_confirmation_message));
		builder.setView(dialogView);
		registerHandlers(builder);

		return builder.create();

	}

	private void registerHandlers(AlertDialog.Builder builder)
	{
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				if (MainActivity.mode == MainActivity.Mode.SHARED_PREFERENCES)
					SharedPreferencesManager.getInstance(context).deleteRolodex(currentSelectedListItem);
				else if (MainActivity.mode == MainActivity.Mode.DATABASE)
					MainActivity.dbManager.deleteData(rolodexRecordList.get(currentSelectedListItem).toString());

					rolodexRecordList.remove(currentSelectedListItem);
					rolodexRecyclerView.getAdapter().notifyDataSetChanged();
			}
		});

		builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialogInterface, int i)
			{
				// Do nothing.
			}
		});
	}

}
