package com.google.library;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.beans.Book;
import com.google.db.DataBaseAdapter;

public class BookCoverDisplay extends Activity implements OnClickListener {
	private File pictureFolder;
	private List<String> imageNames = new ArrayList<String>();
	private String imageViewName;
	private EditText titleET;
	private EditText authorET;
	private EditText yearET;
	private EditText barcodeET;

	private Button edit;
	private Button delete;
	private Button moreInfo;
	private String section;

	private List<Book> books;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;

		Intent parent = getIntent();
		section = parent.getExtras().getString("section");
		pictureFolder = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "BookInventory" + File.separator + section);

		if (pictureFolder.exists()) {
			if (pictureFolder.listFiles().length > 1) {
				setContentView(R.layout.bookcoverlayout);
				Gallery galleriaImagens = (Gallery) findViewById(R.id.galleria);
				titleET = (EditText) findViewById(R.id.titleInfoRecoveredBookCover);
				authorET = (EditText) findViewById(R.id.authorInfoRecoveredBookCover);
				yearET = (EditText) findViewById(R.id.yearInfoRecoveredBookCover);
				barcodeET = (EditText) findViewById(R.id.barcodeInfoRecoveredBookCover);
				edit = (Button) findViewById(R.id.editBookCover);
				delete = (Button) findViewById(R.id.deleteBookCover);
				moreInfo = (Button) findViewById(R.id.moreInfoBookCover);
				if (section.equalsIgnoreCase("Lent")
						|| section.equalsIgnoreCase("Borrowed")) {
					moreInfo.setEnabled(true);
				}
				edit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						callBookInfo((ArrayList<Book>) books);
					}
				});

				delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						createDeleteBookDialog();
					}
				});

				moreInfo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						createMoreInfoDialog();
					}
				});

				galleriaImagens.setAdapter(new ImageAdapter(this));
				galleriaImagens.setSelection(1);
				galleriaImagens.setSpacing(1);
				galleriaImagens
						.setOnItemClickListener(new OnItemClickListener() {
							@SuppressWarnings("rawtypes")
							public void onItemClick(AdapterView parent, View v,
									int position, long id) {
								String image = imageNames.get(position);
								String imageName = image.substring(0,
										image.indexOf("."));
								books = DataBaseAdapter
										.getInstance()
										.getBooksByID(Long.parseLong(imageName));
								displayBookInfo();

							}
						});
			} else {
				if (pictureFolder.listFiles().length == 1) {
					setContentView(R.layout.showbook);

					titleET = (EditText) findViewById(R.id.titleInfoRecoveredShowBook);
					authorET = (EditText) findViewById(R.id.authorInfoRecoveredShowBook);
					yearET = (EditText) findViewById(R.id.yearInfoRecoveredShowBook);
					barcodeET = (EditText) findViewById(R.id.barcodeInfoRecoveredShowBook);
					edit = (Button) findViewById(R.id.editShowBook);
					delete = (Button) findViewById(R.id.deleteBookShowBook);
					moreInfo = (Button) findViewById(R.id.moreInfoShowBook);

					if (section.equalsIgnoreCase("Lent")
							|| section.equalsIgnoreCase("Borrowed")) {
						moreInfo.setEnabled(true);
					}
					edit.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							callBookInfo((ArrayList<Book>) books);

						}
					});

					delete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							createDeleteBookDialog();
						}
					});

					moreInfo.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							createMoreInfoDialog();
						}
					});

					ImageView imageView = (ImageView) findViewById(R.id.shelfImageView);
					File imageFile = new File(
							Environment.getExternalStorageDirectory()
									+ File.separator + "BookInventory"
									+ File.separator + "Thumbnails"
									+ File.separator
									+ pictureFolder.listFiles()[0].getName());
					Bitmap image;
					if (imageFile.exists()) {
						image = BitmapFactory.decodeFile(Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "BookInventory"
								+ File.separator
								+ "Thumbnails"
								+ File.separator
								+ pictureFolder.listFiles()[0].getName());
					} else {
						image = ((BitmapDrawable) getResources().getDrawable(
								R.drawable.coverless)).getBitmap();
					}
					imageView.setImageBitmap(image);
					imageViewName = pictureFolder.listFiles()[0].getName();

					imageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String imageName = imageViewName.substring(0,
									imageViewName.indexOf("."));
							books = DataBaseAdapter
							.getInstance()
							.getBooksByID(Long.parseLong(imageName));
							displayBookInfo();
						}
					});
				} else {
					Toast toast = Toast.makeText(BookCoverDisplay.this,
							getResources().getString(R.string.noBook),
							Toast.LENGTH_SHORT);
					toast.show();
					onBackPressed();
				}
			}
		}

	}

	private void displayBookInfo() {
		Book book = books.get(0);
		titleET.setText(book.getTitle());
		authorET.setText(book.getAuthor());
		barcodeET.setText(book.getBarcode());
		yearET.setText(book.getYear());
	}

	public class ImageAdapter extends BaseAdapter {

		private Context myContext;
		private List<Bitmap> imageIDs = new ArrayList<Bitmap>();

		public ImageAdapter(Context c) {
			this.myContext = c;
		}

		public int getCount() {
			return pictureFolder.listFiles().length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			if (pictureFolder.exists()) {
				for (File pictureFile : pictureFolder.listFiles()) {
					Bitmap image;
					File file = new File(
							Environment.getExternalStorageDirectory()
									+ File.separator + "BookInventory"
									+ File.separator + "Thumbnails"
									+ File.separator + pictureFile.getName());
					if (file.exists()) {
						image = BitmapFactory.decodeFile(Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "BookInventory"
								+ File.separator
								+ "Thumbnails"
								+ File.separator
								+ pictureFile.getName());
					} else {
						image = ((BitmapDrawable) getResources().getDrawable(
								R.drawable.coverless)).getBitmap();
					}
					imageIDs.add(image);
					imageNames.add(pictureFile.getName());
				}
			}
			ImageView imageView = new ImageView(myContext);
			imageView.setImageBitmap(imageIDs.get(position));
			imageView
					.setBackgroundResource(android.R.drawable.alert_dark_frame);

			return imageView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(BookCoverDisplay.this, BookInventory.class);
		startActivity(intent);
		BookCoverDisplay.this.finish();

	}

	private void callBookInfo(ArrayList<Book> books) {
		Intent intent = new Intent(BookCoverDisplay.this, BookInfo.class);
		this.finish();
		intent.putExtra("book", books);
		startActivity(intent);
	}

	private void deleteBook(ArrayList<Book> books) {
		DataBaseAdapter.getInstance().deleteBook(books.get(0));
		String category = getIntent().getExtras().getString("section");
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "BookInventory" + File.separator + category
				+ File.separator + books.get(0).getId() + ".png");
		file.delete();
	}

	private void createDeleteBookDialog() {
		final Dialog dialog = new Dialog(BookCoverDisplay.this);
		dialog.setTitle(R.string.delete);
		dialog.setContentView(R.layout.deletebookconfirmationdialog);
		Button yesButton = (Button) dialog
				.findViewById(R.id.yesdeletebookdialog);
		yesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteBook((ArrayList<Book>) books);
				Toast saveToast = Toast.makeText(BookCoverDisplay.this,
						R.string.sucessullyDeleted, Toast.LENGTH_SHORT);
				saveToast.show();
				dialog.dismiss();
				reloadIntent();
			}
		});

		Button noButton = (Button) dialog.findViewById(R.id.nodeletebookdialog);
		noButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	protected void reloadIntent() {
		String section = getIntent().getExtras().getString("section");
		Intent intent = new Intent(BookCoverDisplay.this,
				BookCoverDisplay.class);
		intent.putExtra("section", section);
		this.finish();
		startActivity(intent);

	}

	private void createLentMoreInfoDialog() {
		final Dialog dialog = new Dialog(BookCoverDisplay.this);
		dialog.setContentView(R.layout.lentmoreinfodialog);
		dialog.setTitle(getResources().getString(R.string.lendInformation));
		Book book = books.get(0);
		EditText name = (EditText) dialog
				.findViewById(R.id.lentToLentMoreInfoDialog);
		EditText date = (EditText) dialog
				.findViewById(R.id.dateLentMoreInfoDialog);
		name.setText(book.getLendedTo());
		Date thisDate = book.getLendingDate();
		int day = thisDate.getDate();
		int month = thisDate.getMonth() + 1;
		int year = thisDate.getYear();
		date.setText(day + "/" + month + "/" + year);

		Button closeButton = (Button) dialog
				.findViewById(R.id.cancelLentMoreInfoDialog);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void createBorrowedMoreInfoDialog() {
		final Dialog dialog = new Dialog(BookCoverDisplay.this);
		dialog.setContentView(R.layout.borrowedmoreinfodialog);
		dialog.setTitle(getResources().getString(R.string.borrowedInformation));
		Book book = books.get(0);
		EditText name = (EditText) dialog
				.findViewById(R.id.borrowedFromMoreInfoDialog);
		EditText date = (EditText) dialog
				.findViewById(R.id.dateBorrowedMoreInfoDialog);
		name.setText(book.getBorrowedFrom());
		Date thisDate = book.getBorrowingDate();
		int day = thisDate.getDate();
		int month = thisDate.getMonth() + 1;
		int year = thisDate.getYear();
		date.setText(day + "/" + month + "/" + year);

		Button closeButton = (Button) dialog
				.findViewById(R.id.cancelBorrowedMoreInfoDialog);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void createMoreInfoDialog() {
		if (section.equalsIgnoreCase("Lent")) {
			createLentMoreInfoDialog();
		} else if (section.equalsIgnoreCase("Borrowed")) {
			createBorrowedMoreInfoDialog();
		}
	}
}