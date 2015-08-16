package com.google.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.google.beans.Book;
import com.google.enums.Category;
import com.google.util.Constants;

public class DataBaseAdapter {

	private static DataBaseHelper dataBaseHelper;

	private static DataBaseAdapter instance;

	private DataBaseAdapter(Context context) {
		this.dataBaseHelper = new DataBaseHelper(context);
	}

	public static DataBaseAdapter getInstance(Context context) {
		if (instance == null) {
			instance = new DataBaseAdapter(context);
		}

		return instance;
	}

	public static void destroy() {
		if (instance != null) {
			instance.close();
		}
		instance = null;
	}

	private void close() {
		dataBaseHelper.close();
	}

	public long addBook(Book book) {
		return dataBaseHelper.insert(book);
	}

	public List<Book> getBooksByCategory(Category category) {
		Cursor booksCursor = dataBaseHelper.getReadableDatabase().query(
				Constants.TABLE_NAME, null, "category = " + category.name(),
				null, null, null, Constants.TITLE_COLUMN_NAME);

		List<Book> books = getBooksFromCursor(booksCursor);

		booksCursor.close();

		return books;
	}

	private static Long getIDFromCursor(Cursor booksCursor) {
		int id = booksCursor.getColumnIndex(Constants.ID_COLUMN_NAME);
		return booksCursor.getLong(id);
	}

	private List<Book> getBooksFromCursor(Cursor booksCursor) {
		List<Book> books = new ArrayList<Book>();

		int idColumnIndex = booksCursor
				.getColumnIndex(Constants.ID_COLUMN_NAME);
		int titleColumnIndex = booksCursor
				.getColumnIndex(Constants.TITLE_COLUMN_NAME);
		int authorColumnIndex = booksCursor
				.getColumnIndex(Constants.AUTHOR_COLUMN_NAME);
		int pictureColumnIndex = booksCursor
				.getColumnIndex(Constants.PICTURE_COLUMN_NAME);
		int categoryColumnIndex = booksCursor
				.getColumnIndex(Constants.CATEGORY_COLUMN_NAME);
		int yearColumnIndex = booksCursor
				.getColumnIndex(Constants.YEAR_COLUMN_NAME);
		int editionColumnIndex = booksCursor
				.getColumnIndex(Constants.EDITION_COLUMN_NAME);
		int barcodeColumnIndex = booksCursor
				.getColumnIndex(Constants.BARCODE_COLUMN_NAME);
		int lendedToColumnIndex = booksCursor
				.getColumnIndex(Constants.LENT_TO_COLUMN_NAME);
		int lendingDateColumnIndex = booksCursor
				.getColumnIndex(Constants.LENDING_DATE_COLUMN_NAME);
		int borrowedColumnIndex = booksCursor
				.getColumnIndex(Constants.BORROWED_FROM_COLUMN_NAME);
		int borrowingDateColumnIndex = booksCursor
				.getColumnIndex(Constants.BORROWING_DATE_COLUMN_NAME);

		while (booksCursor.moveToNext()) {
			Book book = new Book(booksCursor.getLong(idColumnIndex));
			book.setTitle(booksCursor.getString(titleColumnIndex));
			book.setAuthor(booksCursor.getString(authorColumnIndex));
			book.setPicture(booksCursor.getString(pictureColumnIndex));
			book.setCategory(booksCursor.getString(categoryColumnIndex));
			book.setYear(booksCursor.getString(yearColumnIndex));
			book.setEdition(booksCursor.getString(editionColumnIndex));
			book.setBarcode(booksCursor.getString(barcodeColumnIndex));
			book.setLendedTo(booksCursor.getString(lendedToColumnIndex));
			book.setLendingDate(new Date(booksCursor
					.getLong(lendingDateColumnIndex)));
			book.setBorrowedFrom(booksCursor.getString(borrowedColumnIndex));
			book.setBorrowingDate(new Date(booksCursor
					.getLong(borrowingDateColumnIndex)));
			books.add(book);
		}

		return books;
	}

	public static DataBaseAdapter getInstance() {
		return instance;
	}

	public List<Book> getBookByISBN(String isbn) {
		Cursor booksCursor = dataBaseHelper.getReadableDatabase().query(
				Constants.TABLE_NAME, null,
				Constants.BARCODE_COLUMN_NAME + " = " + isbn, null, null, null,
				Constants.TITLE_COLUMN_NAME);

		List<Book> books = getBooksFromCursor(booksCursor);

		booksCursor.close();

		return books;
	}

	public List<Book> getBooksByID(Long id) {
		Cursor booksCursor = dataBaseHelper.getReadableDatabase().query(
				Constants.TABLE_NAME, null,
				Constants.ID_COLUMN_NAME + " = " + id, null, null, null, null);

		List<Book> books = getBooksFromCursor(booksCursor);

		booksCursor.close();

		return books;
	}

	public int countBooks() {
		String query = "SELECT count(*) FROM " + Constants.TABLE_NAME;

		Cursor countRows = dataBaseHelper.getReadableDatabase().rawQuery(query,
				null);
		countRows.moveToFirst();
		int count = countRows.getInt(0);
		countRows.close();

		return count;
	}

	public void cleanDB() {
		dataBaseHelper.getWritableDatabase().delete(Constants.TABLE_NAME, null,
				null);
	}

	public List<Book> getBookByPictureName(String pictureName) {
		Cursor booksCursor = dataBaseHelper.getReadableDatabase().query(
				Constants.TABLE_NAME, null,
				Constants.PICTURE_COLUMN_NAME + " = " + pictureName, null,
				null, null, Constants.TITLE_COLUMN_NAME);

		List<Book> books = getBooksFromCursor(booksCursor);

		booksCursor.close();

		return books;
	}

	public void deleteBook(Book book) {
		dataBaseHelper.getWritableDatabase().delete(Constants.TABLE_NAME,
				Constants.ID_COLUMN_NAME + " = " + book.getId(), null);
	}

	public long updateBook(Book book) {
		return dataBaseHelper.updateBook(book);
	}

	public static Book getNewEmptyBook() {

		Long nextID = getNextID();
		return new Book(nextID);
	}

	private static Cursor getIDCursor() {
		String[] columns = { Constants.ID_COLUMN_NAME };
		Cursor IDCursor = dataBaseHelper.getReadableDatabase().query(
				Constants.TABLE_NAME, columns, null, null, null, null, null);
		return IDCursor;
	}

	public static Long getNextID() {
		Cursor IDCursor = getIDCursor();
		Long max = Long.MIN_VALUE;
		if (IDCursor.getCount() != 0) {
			int idColumnIndex = IDCursor
					.getColumnIndex(Constants.ID_COLUMN_NAME);
			while (IDCursor.moveToNext()) {
				max = max > IDCursor.getLong(idColumnIndex) ? max : IDCursor
						.getLong(idColumnIndex);
			}
		}
		else {
			max = new Long(0);
		}
		return max + 1;
	}
}
