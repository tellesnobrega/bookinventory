package com.google.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.beans.Book;
import com.google.beans.BookRequester;
import com.google.db.DataBaseAdapter;

public class AddNewBookInfo extends Activity {

	private ImageView bookImage;
	private Book book;
	
	private List<Book> books;

	private EditText titleET;
	private EditText authorET;
	private EditText yearET;
	private EditText barcodeET;

	private Button save;
	private Button cancel;
	private Dialog dialog;
	
	private String searchedBarcode;

	final String PICTURE_EXTENSION = ".png";

	private Spinner categoriesSpinner;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnewbookinfo);
		
		bookImage = (ImageView) findViewById(R.id.bookCover);

		titleET = (EditText) findViewById(R.id.addNewTitleInfoRecovered);
		authorET = (EditText) findViewById(R.id.addNewAuthorInfoRecovered);
		yearET = (EditText) findViewById(R.id.addNewYearInfoRecovered);
		barcodeET = (EditText) findViewById(R.id.addNewBarcodeInfoRecovered);

		save = (Button) findViewById(R.id.addNewsaveBook);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				book = new Book(new Long(0));
				book.setTitle(titleET.getText().toString());
				book.setAuthor(authorET.getText().toString());
				book.setBarcode(barcodeET.getText().toString());
				book.setYear(yearET.getText().toString());
				saveBookOnDB(book);
				closeActivity();
			}
		});
		
		cancel = (Button) findViewById(R.id.addNewcancelBook);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeActivity();
				
			}
		});

		categoriesSpinner = (Spinner) findViewById(R.id.addNewCategorySpinner);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.categories, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categoriesSpinner.setAdapter(adapter);
		
		Intent sendIntent = getIntent();
		this.searchedBarcode = (String) sendIntent.getExtras().getString("barcode");
		this.barcodeET.setText(searchedBarcode);
	}

	private void closeActivity(){
		Intent menu = new Intent(AddNewBookInfo.this, Menu.class);
		this.finish();
		startActivity(menu);
	}
	
	private void saveBookOnDB(Book book) {
		long imageName;
		if (book != null) {
			imageName = DataBaseAdapter.getInstance(this).addBook(book);

			createSaveFile(book,imageName);
		}
	}

	private void createSaveFile(Book book, long imageName) {
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

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(AddNewBookInfo.this, Menu.class);
		this.finish();
		startActivity(intent);
	}
}
