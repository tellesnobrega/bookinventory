package com.google.beans;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BookRequester {

	public static List<Book> requestBook(String isnb) {
		List<Book> books = new ArrayList<Book>();
		Book book = null;
		try {
			URL bookURL = new URL(
					"https://www.googleapis.com/books/v1/volumes?q=ISBN:"
							+ isnb);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					bookURL.openStream()));
			Gson gson = new GsonBuilder().create();
			BookContent bookcontent = gson.fromJson(reader, BookContent.class);

			reader.close();

			if (bookcontent.getItems() != null
					&& bookcontent.getItems().length > 0) {
				for (Item item : bookcontent.getItems()) {
					book = Book.parse(item);
					book.setBarcode(isnb);
					books.add(book);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return books;
	}
	

	public static boolean saveBookThumbnail(String url, FileOutputStream fos) {
		try {
			URI uri = (new URL(url)).toURI();

			HttpGet httpget = new HttpGet(uri);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(httpget);
			response.getEntity().writeTo(fos);
			return true;
		} catch (Exception e) {
			return false;
		}

	}
}