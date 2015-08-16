package com.google.util;

import java.io.File;


public class Constants {
	
	public static final String IMAGES_PATH = "assets" + File.separator + "images" + File.separator;
	public static final String DATABASE_NAME = "books.db";
	public static final int SCHEMA_VERSION = 3;

	public static final String TABLE_NAME = "books";
	public static final String ID_COLUMN_NAME = "_id";
	public static final String TITLE_COLUMN_NAME = "title";
	public static final String AUTHOR_COLUMN_NAME = "author";
	public static final String PICTURE_COLUMN_NAME = "picture";
	public static final String CATEGORY_COLUMN_NAME = "category";
	public static final String YEAR_COLUMN_NAME = "year";
	public static final String EDITION_COLUMN_NAME = "edition";
	public static final String BARCODE_COLUMN_NAME = "barcode";
	public static final String LENT_TO_COLUMN_NAME = "lendedTo";
	public static final String LENDING_DATE_COLUMN_NAME = "lendindDate";
	public static final String BORROWED_FROM_COLUMN_NAME = "borrowedFrom";
	public static final String BORROWING_DATE_COLUMN_NAME = "borrowindDate";
	public static final String NO_IMAGE_AVAILABLE = "noImageAvailable";
	
	public static final int CAMERA_WIDTH = 720;
	public static final int CAMERA_HEIGHT = 480;
	
	public static final String PICTURE_EXTENSION = ".png";
	
}
