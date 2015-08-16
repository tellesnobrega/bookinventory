package com.google.beans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import com.google.util.Constants;

public class Book implements Serializable {

	private Long id;
	private String title;
	private String author;
	private String picture;
	private String category;
	private String year;
	private String edition;
	private String barcode;
	private String lendedTo;
	private Date lendingDate;
	private String borrowedFrom;
	private Date borrowingDate;
	private String thumbnailLink;

	public Book(Long id) {
		this.id = id;
		this.lendingDate = new Date();
		this.borrowingDate = new Date();
		this.category = "DESCONHECIDO";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getLendedTo() {
		return lendedTo;
	}

	public void setLendedTo(String lendedTo) {
		this.lendedTo = lendedTo;
	}

	public Date getLendingDate() {
		return lendingDate;
	}

	public void setLendingDate(Date lendingDate) {
		this.lendingDate = lendingDate;
	}

	public String getBorrowedFrom() {
		return borrowedFrom;
	}

	public void setBorrowedFrom(String borrowedFrom) {
		this.borrowedFrom = borrowedFrom;
	}

	public Date getBorrowingDate() {
		return borrowingDate;
	}

	public void setBorrowingDate(Date borrowingDate) {
		this.borrowingDate = borrowingDate;
	}

	public Long getId() {
		return id;
	}

	private void setThumbnailLink(String thumbnailLink) {
		this.thumbnailLink = thumbnailLink;
	}

	public String getThumbnailLink() {
		return thumbnailLink;
	}

	public static Book parse(Item item) {
		Book book = new Book(null);
		book.setAuthor(Arrays.toString(item.getVolumeInfo().getAuthors()));
		book.setTitle(item.getVolumeInfo().getTitle());
		book.setYear(item.getVolumeInfo().getPublishedDate());
		book.setPicture(item.getId() + Constants.PICTURE_EXTENSION);
		try{
			book.setThumbnailLink(item.getVolumeInfo().getImageLinks().getSmallThumbnail());
		} catch (NullPointerException e) {
			book.setThumbnailLink(Constants.NO_IMAGE_AVAILABLE);
		}
		return book;
	}
}
