package com.google.beans;
import java.util.Arrays;

public class BookContent {
	private String kind;
	private int totalItems;
	private Item[] items;

	// Getters and setters are not required for this example. 
	// GSON sets the fields directly.

	public BookContent() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return kind + " - " + totalItems + " - " + Arrays.toString(items);
	}

	public Item[] getItems() {
		return items;
	}
}