package com.google.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.beans.Book;
import com.google.beans.BookRequester;
import com.google.db.DataBaseAdapter;
import com.google.util.Constants;

public class BookInfo extends Activity {

	private ImageView bookImage;
	private Book book;

	private ArrayList<Book> books;

	private EditText titleET;
	private EditText authorET;
	private EditText yearET;
	private EditText barcodeET;
	private EditText name;
	private DatePicker date;

	private Button save;
	private Button cancel;
	private Button searchButton;
	private Dialog bookSelectDialog;
	
	private String searchedBarcode;

	private Spinner categoriesSpinner;
	
	final String PICTURE_EXTENSION = ".png";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookinfo);

		bookImage = (ImageView) findViewById(R.id.bookCover);

		titleET = (EditText) findViewById(R.id.titleInfoRecovered);
		authorET = (EditText) findViewById(R.id.authorInfoRecovered);
		yearET = (EditText) findViewById(R.id.yearInfoRecovered);
		barcodeET = (EditText) findViewById(R.id.barcodeInfoRecovered);

		save = (Button) findViewById(R.id.saveBook);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new SaveOnDBASyncCall().execute(null);
			}
		});

		cancel = (Button) findViewById(R.id.cancelBook);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				closeActivity();

			}
		});
		
		searchButton = (Button) findViewById(R.id.searchMore);
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new RetrieveBooksFromWeb().execute(searchedBarcode);
			}
		});

		categoriesSpinner = (Spinner) findViewById(R.id.categorySpinner);
		categoriesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Object item = arg0.getItemAtPosition(arg2);
				if (item.toString().equalsIgnoreCase("Lent") || item.toString().equalsIgnoreCase("Emprestei")) {
					createLentBookDialog();
				}else if(item.toString().equalsIgnoreCase("Borrowed") || item.toString().equalsIgnoreCase("Peguei emprestado")) {
					createBorrowedBookDialog();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.categories, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categoriesSpinner.setAdapter(adapter);

		Intent sendIntent = getIntent();
		this.books = (ArrayList<Book>) sendIntent.getExtras().getSerializable(
				"book");
		this.searchedBarcode = (String) sendIntent.getExtras().getString("barcode");
		displayBookInfo(books);
	}

	private void displayBookInfo(List<Book> books) {
		if (books.size() > 1) {
			createBookSelectionDialog();
		} else {
			if (!books.isEmpty()) {
				book = books.get(0);
				if (book != null) {
					if(book.getThumbnailLink() != null) {
						Bitmap image;
						image = BitmapFactory.decodeFile(Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "BookInventory"
								+ File.separator
								+ "tmp"
								+ File.separator + book.getPicture());

						bookImage.setImageBitmap(image);
					}
					titleET.setText(book.getTitle());
					authorET.setText(book.getAuthor());
					yearET.setText(book.getYear());
					barcodeET.setText(book.getBarcode());
				} else {
					createAddBookDialog();
				}
			} else {
				createAddBookDialog();
			}
		}

	}

	private void createAddBookDialog() {
		final Dialog dialog = new Dialog(BookInfo.this);
		dialog.setContentView(R.layout.addbookdialog);
		dialog.setTitle(getResources().getString(R.string.notFound));
		Button yesButton = (Button) dialog.findViewById(R.id.yesaddbookdialog);
		yesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BookInfo.this, AddNewBookInfo.class);
				intent.putExtra("barcode", searchedBarcode);
				startActivity(intent);
				BookInfo.this.finish();
				dialog.dismiss();
			}
		});

		Button noButton = (Button) dialog.findViewById(R.id.noaddbookdialog);
		noButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BookInfo.this, Menu.class);
				BookInfo.this.finish();
				startActivity(intent);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private void createLentBookDialog() {
		final Dialog dialog = new Dialog(BookInfo.this);
		dialog.setContentView(R.layout.booklentdialog);
		dialog.setTitle(getResources().getString(R.string.lendInformation));
		
		name = (EditText) dialog.findViewById(R.id.lentToEdit);
		date = (DatePicker) dialog.findViewById(R.id.datePicker1);
		
		Button saveButton = (Button) dialog.findViewById(R.id.saveLent);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String nome = name.getText().toString();
				Date newDate = new Date(date.getYear(),date.getMonth(),date.getDayOfMonth());
				book.setLendedTo(nome);
				book.setLendingDate(newDate);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private void createBorrowedBookDialog() {
		final Dialog dialog = new Dialog(BookInfo.this);
		dialog.setContentView(R.layout.bookborroweddialog);
		dialog.setTitle(getResources().getString(R.string.borrowedInformation));
		
		name = (EditText) dialog.findViewById(R.id.borrowedToEdit);
		date = (DatePicker) dialog.findViewById(R.id.datePickerborrowedFrom);
		
		Button saveButton = (Button) dialog.findViewById(R.id.saveBorrowed);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String nome = name.getText().toString();
				Date newDate = new Date(date.getYear(),date.getMonth(),date.getDayOfMonth());
				book.setBorrowedFrom(nome);
				book.setBorrowingDate(newDate);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private void closeActivity() {
		Intent menu = new Intent(BookInfo.this, Menu.class);
		startActivity(menu);
		BookInfo.this.finish();
	}

	private void saveBookOnDB(Book book) {
		long imageName;
		if (book != null) {
			imageName = DataBaseAdapter.getInstance(this).addBook(book);
			if(imageName == -1) {
				imageName = DataBaseAdapter.getInstance(this).updateBook(book);
			}
			if (book.getThumbnailLink() != null) {
				saveThumbnail(book, imageName);
			}

			createSaveFile(book,imageName);
		}
	}

	private void createSaveFile(Book book, long imageName) {
		deleteBookFromWrongSection(book);
		String category = toEnglish((String) categoriesSpinner.getSelectedItem());
		File savedItem = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "BookInventory" + File.separator + category
				+ File.separator + Long.toString(imageName) + ".png");

		try {
			savedItem.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String toEnglish(String selectedItem) {
		if (selectedItem.equals("Meus Livros")) return "My Books";
		else if (selectedItem.equals("Peguei emprestado")) return "Borrowed";
		else if (selectedItem.equals("Emprestei")) return "Lent";
		else if (selectedItem.equals("Desejados")) return "Wishlist";
		return selectedItem;
	}
	
	private void deleteBookFromWrongSection(Book book) {
		deleteFromFolder(book,"My Books");
		deleteFromFolder(book, "Wishlist");
		deleteFromFolder(book, "Lent");
		deleteFromFolder(book, "Borrowed");
	}

	private void deleteFromFolder(Book book, String folder) {
		File file;
		if(book.getPicture().endsWith(PICTURE_EXTENSION)) {
			file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "BookInventory" + File.separator + folder + File.separator + book.getPicture());
		}else {
			file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "BookInventory" + File.separator + folder + File.separator + book.getPicture() + PICTURE_EXTENSION);
		}
		file.delete();
	}
	
	

	private void saveThumbnail(Book book, long nome) {
		if (!book.getThumbnailLink().equals(Constants.NO_IMAGE_AVAILABLE)) {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {

				File bookInventoryFolder = new File(
						Environment.getExternalStorageDirectory()
								+ File.separator + "BookInventory"
								+ File.separator + "Thumbnails");

				File file = new File(bookInventoryFolder, Long.toString(nome) + ".png");
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

	private void createBookSelectionDialog() {
		bookSelectDialog = new Dialog(BookInfo.this);
		bookSelectDialog.setContentView(R.layout.bookselectiongroup);
		bookSelectDialog.setTitle(getResources().getString(R.string.selectBook));

		final RadioGroup radioGroup = (RadioGroup) bookSelectDialog
				.findViewById(R.id.radioGroup);
		for (Book book : books) {
			RadioButton radioButton = new RadioButton(this);
			radioButton.setText(book.getTitle());
			radioGroup.addView(radioButton);
		}
		Button searchButton = (Button) bookSelectDialog
				.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (Book bookLista : books) {
					Intent intent = new Intent(BookInfo.this, BookInfo.class);
					books = (ArrayList<Book>) BookRequester
							.requestBook(bookLista.getBarcode());
					BookInfo.this.finish();
					intent.putExtra("book", books);
					startActivity(intent);
				}
			}
		});

		Button confirmButton = (Button) bookSelectDialog
				.findViewById(R.id.selected);
		confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
				RadioButton radioButton = (RadioButton) bookSelectDialog
						.findViewById(checkedRadioButtonId);
				String title = (String) radioButton.getText();
				for (Book bookLista : books) {
					if (bookLista.getTitle().equalsIgnoreCase(title)) {
						book = bookLista;
						titleET.setText(book.getTitle());
						authorET.setText(book.getAuthor());
						yearET.setText(book.getYear());
						barcodeET.setText(book.getBarcode());
					}
				}
				bookSelectDialog.dismiss();

			}
		});
		bookSelectDialog.show();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(BookInfo.this, Menu.class);
		this.finish();
		startActivity(intent);
	}
	
	private class SaveOnDBASyncCall extends AsyncTask<String, Void, String> {

		private final ProgressDialog dialog = new ProgressDialog(BookInfo.this);

		protected void onPreExecute() {
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage(getResources().getString(R.string.saving));
			this.dialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
			saveBookOnDB(book);
			return null;
		}

		protected void onPostExecute(String result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			closeActivity();
		}
	}
	
	private class RetrieveBooksFromWeb extends AsyncTask<String, Void, String> {

		private final ProgressDialog dialog = new ProgressDialog(BookInfo.this);

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
			displayBookInfo(books, searchedBarcode);
		}
	}
	
	private List<Book> retrieveBooks(String barCode) {
		books = (ArrayList<Book>) BookRequester.requestBook(barCode);
		return books;
	}
	
	private void displayBookInfo(ArrayList<Book> book, String barcode) {
		Intent intent = new Intent(BookInfo.this, BookInfo.class);
		intent.putExtra("book", book);
		intent.putExtra("barcode", barcode);
		startActivity(intent);
		BookInfo.this.finish();
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
}
