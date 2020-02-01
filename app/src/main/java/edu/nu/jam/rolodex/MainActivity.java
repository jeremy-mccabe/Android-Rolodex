package edu.nu.jam.rolodex;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.nu.jam.rolodex.Adapters.RolodexAdapter;
import edu.nu.jam.rolodex.Contacts.DatabaseManager;
import edu.nu.jam.rolodex.Contacts.RolodexRecordManager;
import edu.nu.jam.rolodex.Data.RolodexRecord;
import edu.nu.jam.rolodex.Fragments.DeleteConfirmationDialog;
import edu.nu.jam.rolodex.Fragments.PersistenceSelectionDialog;
import edu.nu.jam.rolodex.Fragments.RolodexRecordDialog;
import edu.nu.jam.rolodex.Interfaces.IRolodexOperations;
import edu.nu.jam.rolodexRecord.R;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IRolodexOperations
{
	private List<RolodexRecord> rolodexRecordList;
	private RecyclerView rolodexRecyclerView;
	private int currentIndex;
	private CardView archiveView;
	public static DatabaseManager dbManager;
	public static Mode mode;

	public enum Mode
	{
		SHARED_PREFERENCES, DATABASE
	}

	// ACTIVITY:
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dbManager = new DatabaseManager(this);
		dbManager.clearDatabase(false);

		createMenuBar();
		configurePersistenceMode();
		bindCtrlData();
		configureRecyclerView();
	}

	private void createMenuBar()
	{
		ActionBar actionBar = getSupportActionBar();
		if (null != actionBar)
		{
			actionBar.setTitle(R.string.app_name);
			actionBar.setDisplayUseLogoEnabled(true);
			actionBar.setDisplayShowHomeEnabled(true);
		}
	}

	public void configurePersistenceMode()
	{
		// Retrieve mode from SharedPreferences:
		SharedPreferences modeSaver = getApplicationContext().getSharedPreferences("MODE_SAVER", MODE_PRIVATE);
		SharedPreferences.Editor editor = modeSaver.edit();

		// If no mode exists in SharedPreferences, define it:
		if (modeSaver.getString("mode", null) == null)
		{
			editor.putString("mode", "shared_preferences");
			editor.commit();
		}

		String retrievedMode = modeSaver.getString("mode", null);
		switch (retrievedMode)
		{
			case "shared_preferences":
				mode = Mode.SHARED_PREFERENCES;
				break;
			case "database":
				mode = Mode.DATABASE;
				break;
		}
	}

	private void bindCtrlData()
	{
		if (mode == Mode.SHARED_PREFERENCES)
		{
			rolodexRecordList = RolodexRecordManager.getAllContactsFromSharedPreferences(this);
		}
		else if (mode == Mode.DATABASE)
		{
			rolodexRecordList = RolodexRecordManager.getAllContactsFromDatabase();
		}

		rolodexRecyclerView = findViewById(R.id.rolodexInfoRecyclerView);
	}

	private void configureRecyclerView()
	{
		rolodexRecyclerView.setHasFixedSize(true);
		rolodexRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		rolodexRecyclerView.setAdapter(new RolodexAdapter(this));
	}

	// MENU OPTIONS:
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.addMenuItem:
				showAddDialog();
				break;

			case R.id.editMenuItem:
				showEditDialog();
				break;

			case R.id.deleteMenuItem:
				showDeleteConfirmationDialog();
				break;

			case R.id.settingsMenuItem:
				showDataPersistenceDialog();
				break;
		}

		rolodexRecyclerView.getAdapter().notifyDataSetChanged();
		return super.onOptionsItemSelected(item);
	}

	// DIALOG FRAGMENTS:
	private void showAddDialog()
	{
		RolodexRecord rolodexRecordInformation = new RolodexRecord();
		RolodexRecordDialog dlg = new RolodexRecordDialog(this, rolodexRecordInformation, rolodexRecordList, rolodexRecyclerView, currentIndex, mode);
		dlg.show(getSupportFragmentManager(), "rolodex_information_dialog");
	}

	private void showEditDialog()
	{
		if ((currentIndex >= 0) && (currentIndex < rolodexRecordList.size()))
		{
			RolodexRecord rolodexRecordInformation = rolodexRecordList.get(currentIndex);
			RolodexRecordDialog dlg = new RolodexRecordDialog(this, rolodexRecordInformation, rolodexRecordList, rolodexRecyclerView, currentIndex, mode);
			dlg.show(getSupportFragmentManager(), "rolodex_information_dialog");
		}
	}

	private void showDeleteConfirmationDialog()
	{
		if ((currentIndex >= 0) && (currentIndex < rolodexRecordList.size()))
		{
			DeleteConfirmationDialog deleteConfirmationDialog = new DeleteConfirmationDialog(this, rolodexRecordList, rolodexRecyclerView, currentIndex);
			deleteConfirmationDialog.show(getSupportFragmentManager(), "rolodex_delete_dialog");
		}
	}

	private void showDataPersistenceDialog()
	{
		PersistenceSelectionDialog persistenceSelectionDialog = new PersistenceSelectionDialog(this, mode);
		persistenceSelectionDialog.show(getSupportFragmentManager(), "data_persistence_dialog");
	}

	public void refreshRolodexRecyclerView()
	{
		rolodexRecordList.clear();
		this.bindCtrlData();
		rolodexRecyclerView.getAdapter().notifyDataSetChanged();
	}

	@Override
	public List<RolodexRecord> onGetContacts()
	{
		return rolodexRecordList;
	}

	// CARD-COLORING:
	@Override
	public void onItemSelected(int position, View view)
	{
		CardView activeView;
		currentIndex = position;

		if ((position >= 0) && (position < rolodexRecordList.size()))
		{
			activeView = (CardView) view;

			if (archiveView == null && activeView != archiveView)
			{
				activeView.setCardBackgroundColor(Color.argb(100, 0, 133, 119));
				archiveView = activeView;
			}
			else if (archiveView != null && activeView != archiveView)
			{
				activeView.setCardBackgroundColor(Color.argb(100, 0, 133, 119));
				archiveView.setCardBackgroundColor(Color.rgb(255, 255, 255));
				archiveView = activeView;
			}
			else if (archiveView == null)
			{
				archiveView = activeView;
			}
		}
	}
}
