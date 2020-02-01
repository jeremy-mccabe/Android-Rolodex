package edu.nu.jam.rolodex.Interfaces;

import android.view.View;
import java.util.List;
import edu.nu.jam.rolodex.Data.RolodexRecord;

public interface IRolodexOperations
{
	List<RolodexRecord> onGetContacts();
	void onItemSelected(int position, View view);
}
