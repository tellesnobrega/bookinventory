package com.google.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.beans.Book;
import com.google.beans.BookRequester;
import com.google.db.DataBaseAdapter;
import com.google.util.Constants;

public class Menu extends Activity {

	private ImageButton scanButton;
	private ImageButton bookShelfButton;
	private String barcode;
	private ArrayList<Book> books;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		bookShelfButton = (ImageButton) findViewById(R.id.imageButtonBooks);
		bookShelfButton.setOnClickListener(bookShelfListener);
		scanButton = (ImageButton) findViewById(R.id.imageButtonScanner);
		scanButton.setOnClickListener(mScan);

		// Generates the BookInventory folders, if they do not exist
		createDirs();
	}

	private void createDirs() {
		File bookInventoryFolder = new File(
				Environment.getExternalStorageDirectory() + File.separator
				+ "BookInventory");
		bookInventoryFolder.mkdirs();

		final String rootFolder = Environment.getExternalStorageDirectory() + File.separator
						+ "BookInventory" + File.separator;
		
		File myBooksFolder = new File(rootFolder + "My Books");
		myBooksFolder.mkdirs();

		File wishlistFolder = new File(
				rootFolder + "Wishlist");
		wishlistFolder.mkdirs();

		File lentBooksFolder = new File(
				rootFolder + "Lent");
		lentBooksFolder.mkdirs();

		File borrowedBooksFolder = new File(
				rootFolder + "Borrowed");
		borrowedBooksFolder.mkdirs();

		File thumbnailsFolder = new File(
				rootFolder + "Thumbnails");
		thumbnailsFolder.mkdirs();
		
		File tmpFolder = new File(
				rootFolder + "tmp");
		tmpFolder.mkdirs();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				barcode = intent.getStringExtra("SCAN_RESULT");
				new RetrieveBooksFromWeb().execute(barcode);
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.settingmenu:
	        	Intent intent = new Intent(Menu.this, SettingsActivity.class);
	    		startActivity(intent);
	    		Menu.this.finish();
	        	break;
	    }
	    return true;
	}

	private List<Book> retrieveBooks(String barCode) {

		books = (ArrayList<Book>) DataBaseAdapter.getInstance(
				this).getBookByISBN(barCode);

		Book book = null;

		if (books.isEmpty()) {
			books = (ArrayList<Book>) BookRequester.requestBook(barCode);
		} else {
			book = books.get(0);
		}
		return books;
	}

	private void displayBookInfo(ArrayList<Book> book, String barcode) {
		Intent intent = new Intent(Menu.this, BookInfo.class);
		intent.putExtra("book", book);
		intent.putExtra("barcode", barcode);
		startActivity(intent);
		Menu.this.finish();
	}


	public Button.OnClickListener mScan = new Button.OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			startActivityForResult(intent, 0);
		}
	};

	public Button.OnClickListener bookShelfListener = new Button.OnClickListener() {
		public void onClick(View v) {
			bookShelfButtonAction();
		}
	};

	private void bookShelfButtonAction() {
		Intent bookInventory = new Intent(Menu.this, BookInventory.class);
		startActivity(bookInventory);
		Menu.this.finish();
	}

	private class RetrieveBooksFromWeb extends AsyncTask<String, Void, String> {

		private final ProgressDialog dialog = new ProgressDialog(Menu.this);

		protected void onPreExecute() {
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage(getResources().getString(R.string.searching));
			this.dialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
			books = (ArrayList<Book>) retrieveBooks(args[0]);
			if (!books.isEmpty()) {
				for (Book book : books) {
					if (book.getThumbnailLink() != null) {
						saveTmpThumbnail(book);
					}
				}
			}
			return null;
		}

		protected void onPostExecute(String result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			displayBookInfo(books, barcode);
		}
	}
	
	private void saveTmpThumbnail(Book book) {
		if (!book.getThumbnailLink().equals(Constants.NO_IMAGE_AVAILABLE)) {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {

				File bookInventoryFolder = new File(
						Environment.getExternalStorageDirectory()
								+ File.separator + "BookInventory"
								+ File.separator + "tmp");

				File file = new File(bookInventoryFolder, book.getPicture());
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(file);
					BookRequester.saveBookThumbnail(book.getThumbnailLink(),
							fos);
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {
					// handle exception
				} catch (IOException e) {
					// handle exception
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		File file = new File(Environment.getExternalStorageDirectory() + File.separator + "BookInventory" + File.separator + "tmp");
		for (File images : file.listFiles()) {
			images.delete();
			
		}
		super.onBackPressed();

	}
}
