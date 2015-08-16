package com.google.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class BookInventory extends Activity {
	/** Called when the activity is first created. */
	private ImageView viewMyBooksButton;
	private ImageView wishListBooksButton;
	private ImageView lentBooksButton;
	private ImageView borrowedBooksButton;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		viewMyBooksButton = (ImageView) findViewById(R.id.myBooksImageButton);
		viewMyBooksButton.setOnClickListener(myBooksListener);

		wishListBooksButton = (ImageView) findViewById(R.id.wishListImageButton);
		wishListBooksButton.setOnClickListener(wishlistListener);

		lentBooksButton = (ImageView) findViewById(R.id.lentBooksImageButton);
		lentBooksButton.setOnClickListener(lentListener);

		borrowedBooksButton = (ImageView) findViewById(R.id.borrowedImageButton);
		borrowedBooksButton.setOnClickListener(borrowedListener);

	}

	public Button.OnClickListener myBooksListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			showBooks("My Books");
		}
	};

	public Button.OnClickListener wishlistListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			showBooks("Wishlist");
		}
	};

	public Button.OnClickListener lentListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			showBooks("Lent");
		}
	};

	public Button.OnClickListener borrowedListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			showBooks("Borrowed");
		}
	};

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(BookInventory.this, Menu.class);
		this.finish();
		startActivity(intent);
	}

	public void showBooks(String section) {
		Intent intent = new Intent(BookInventory.this, BookCoverDisplay.class);
		intent.putExtra("section", section);
		this.finish();
		startActivity(intent);
	}

}