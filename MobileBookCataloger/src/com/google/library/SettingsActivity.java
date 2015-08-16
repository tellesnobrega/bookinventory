package com.google.library;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SettingsActivity extends ListActivity{

	private Dialog confirmCleanDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String lista[] = new String[]{"Clean all the saved information."};

		setListAdapter(new ArrayAdapter(this, R.layout.settings, lista));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				createDialog();
			}
		});

	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(SettingsActivity.this, Menu.class);
		this.finish();
		startActivity(intent);
	}
	
	private void createDialog() {
		confirmCleanDialog = new Dialog(SettingsActivity.this);
		confirmCleanDialog.setContentView(R.layout.bookselectiongroup);
		confirmCleanDialog.setTitle("Confirm the clean of all saved data:");
		confirmCleanDialog.show();
		Button confirmButton = (Button) confirmCleanDialog.findViewById(R.id.yesaddbookdialog);
		Button cancelButton = (Button) confirmCleanDialog.findViewById(R.id.noaddbookdialog);
		
		confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO Chamada limpeza do db
				confirmCleanDialog.dismiss();
			}
		});
		
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				confirmCleanDialog.dismiss();
			}
		});
	}
}