package edu.nu.jam.rolodex.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.nu.jam.rolodex.Data.RolodexRecord;
import edu.nu.jam.rolodex.Interfaces.IRolodexOperations;
import edu.nu.jam.rolodexRecord.R;


public class RolodexAdapter extends RecyclerView.Adapter<RolodexAdapter.ViewHolder>
{
	private IRolodexOperations rolodexOperationsContext;

	public RolodexAdapter(Context context)
	{
		if (!(context instanceof IRolodexOperations))
			throw new ClassCastException("IRolodexOperations cast exception.");

		rolodexOperationsContext = (IRolodexOperations) context;
	}

	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView fullNameTextView;
		private TextView phoneNumberTextView;

		ViewHolder(@NonNull View itemView)
		{
			super(itemView);

			bindControls();
			registerHandlers();
		}

		private void bindControls()
		{
			fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
			phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
		}

		private void registerHandlers()
		{
			itemView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					int index = (int) view.getTag();
					View fetchedViewOrigin = (View) phoneNumberTextView.getParent();
					View fetchedViewIntermediate = (View) fetchedViewOrigin.getParent();
					View fetchedViewTarget = (View) fetchedViewIntermediate.getParent();
					rolodexOperationsContext.onItemSelected(index, fetchedViewTarget);
				}
			});
		}

		private void displayRolodexData(RolodexRecord rolodexRecordInformation)
		{
			fullNameTextView.setText(rolodexRecordInformation.nameToString());
			phoneNumberTextView.setText(rolodexRecordInformation.getPhoneNumber());
		}
	}

	@NonNull
	@Override
	public RolodexAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parentViewGroup, int viewType)
	{
		View view = LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.rolodex_list_item, parentViewGroup, false);
		return new RolodexAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RolodexAdapter.ViewHolder holder, int position)
	{
		List<RolodexRecord> rolodexRecordList = rolodexOperationsContext.onGetContacts();
		RolodexRecord rolodexRecordInformation = rolodexRecordList.get(position);
		holder.displayRolodexData(rolodexRecordInformation);
		holder.itemView.setTag(position);
	}

	@Override
	public int getItemCount()
	{
		return rolodexOperationsContext.onGetContacts().size();
	}
}
