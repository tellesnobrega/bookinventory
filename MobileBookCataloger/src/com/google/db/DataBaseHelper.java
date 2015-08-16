package com.google.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.beans.Book;
import com.google.util.Constants;

public class DataBaseHelper extends SQLiteOpenHelper{

	public DataBaseHelper(Context context) {
		super(context, Constants.DATABASE_NAME, null, Constants.SCHEMA_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase dataBase) {
		dataBase.execSQL("CREATE TABLE " + Constants.TABLE_NAME 
				+ " (" + Constants.ID_COLUMN_NAME +" INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Constants.TITLE_COLUMN_NAME + " TEXT NOT NULL, "
				+ Constants.AUTHOR_COLUMN_NAME + " TEXT NOT NULL,"
				+ Constants.PICTURE_COLUMN_NAME + " TEXT, "
				+ Constants.CATEGORY_COLUMN_NAME + " TEXT NOT NULL, "
				+ Constants.YEAR_COLUMN_NAME + " TEXT, "
				+ Constants.EDITION_COLUMN_NAME + " TEXT, "
				+ Constants.BARCODE_COLUMN_NAME + " TEXT NOT NULL, "
				+ Constants.LENT_TO_COLUMN_NAME + " TEXT, "
				+ Constants.LENDING_DATE_COLUMN_NAME + " INTEGER,"
				+ Constants.BORROWED_FROM_COLUMN_NAME + " TEXT, "
				+ Constants.BORROWING_DATE_COLUMN_NAME + " INTEGER, " 
				+ " CONSTRAINT book_unique UNIQUE ( " 
				+ Constants.TITLE_COLUMN_NAME + ", " 
				+ Constants.AUTHOR_COLUMN_NAME + ", "
				+ Constants.YEAR_COLUMN_NAME + ", "
				+ Constants.BARCODE_COLUMN_NAME
				+		"));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
	}
	
	public long insert(Book book) {
			return insertBookInTable(book);
	}

	private long getIdOfLastBook() {
		Cursor ranksCursor = getReadableDatabase().
				query(Constants.TABLE_NAME, 
				new String[]{Constants.ID_COLUMN_NAME}, null, null, null, null, 
				Constants.ID_COLUMN_NAME + " DESC", "1");
		
		ranksCursor.moveToFirst();
		long id;
		if(ranksCursor.getCount() != 0) {
			id = ranksCursor.getLong(0);
		}else{
			id = 0;
		}
		
		ranksCursor.close();
		
		return id + 1;
	}

	private long insertBookInTable(Book book) {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(Constants.TITLE_COLUMN_NAME, 
				book.getTitle());
		contentValues.put(Constants.AUTHOR_COLUMN_NAME, 
				book.getAuthor());
		contentValues.put(Constants.PICTURE_COLUMN_NAME, 
				book.getPicture());
		contentValues.put(Constants.CATEGORY_COLUMN_NAME, 
				book.getCategory());
		contentValues.put(Constants.YEAR_COLUMN_NAME, 
				book.getYear());
		contentValues.put(Constants.EDITION_COLUMN_NAME, 
				book.getEdition());
		contentValues.put(Constants.BARCODE_COLUMN_NAME, 
				book.getBarcode());
		contentValues.put(Constants.LENT_TO_COLUMN_NAME, 
				book.getLendedTo());
		contentValues.put(Constants.LENDING_DATE_COLUMN_NAME, 
				book.getLendingDate().getTime());
		contentValues.put(Constants.BORROWED_FROM_COLUMN_NAME, 
				book.getBorrowedFrom());
		contentValues.put(Constants.BORROWING_DATE_COLUMN_NAME, 
				book.getBorrowingDate().getTime());
		
		return getWritableDatabase().
			insert(Constants.TABLE_NAME, null, contentValues);
	}
	
	public long updateBook(Book book) {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(Constants.CATEGORY_COLUMN_NAME, 
				book.getCategory());
		contentValues.put(Constants.LENT_TO_COLUMN_NAME, 
				book.getLendedTo());
		contentValues.put(Constants.LENDING_DATE_COLUMN_NAME, 
				book.getLendingDate().getTime());
		contentValues.put(Constants.BORROWED_FROM_COLUMN_NAME, 
				book.getBorrowedFrom());
		contentValues.put(Constants.BORROWING_DATE_COLUMN_NAME, 
				book.getBorrowingDate().getTime());

		getWritableDatabase().update(Constants.TABLE_NAME, contentValues, Constants.ID_COLUMN_NAME + " = "+ book.getId(), null);
		return book.getId();
	}
		
}
