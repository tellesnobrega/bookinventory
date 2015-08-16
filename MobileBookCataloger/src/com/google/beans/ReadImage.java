package com.google.beans;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;

public class ReadImage {

	public static void main(String[] args) throws MalformedURLException,
			FileNotFoundException, IOException, URISyntaxException {
		URI uri = URIUtils
				.createURI(
						"http",
						"bks2.books.google.com.br",
						-1,
						"/books",
						"id=DxNiQgAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api",
						null);
		
		HttpGet httpget = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(httpget);
		response.getEntity().writeTo(new FileOutputStream("image.jpg"));
	}

}
