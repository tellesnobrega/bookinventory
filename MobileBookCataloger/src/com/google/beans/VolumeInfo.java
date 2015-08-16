package com.google.beans;
import java.util.Arrays;


public class VolumeInfo {
	private String title;
    private String[] authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private IndustryIdentifier[] industryIdentifiers;
    private int pageCount;
    private String[] categories;
    private ImageLinks imageLinks;
    private String language;
    
	public VolumeInfo() {
	}
	
	@Override
	public String toString() {
		return title + " - " + Arrays.toString(authors) + " - " + publisher + " - " + Arrays.toString(industryIdentifiers);
	}
	
	public ImageLinks getImageLinks() {
		return imageLinks;
	}

	public String getTitle() {
		return title;
	}

	public String[] getAuthors() {
		return authors;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public String getDescription() {
		return description;
	}

	public IndustryIdentifier[] getIndustryIdentifiers() {
		return industryIdentifiers;
	}

	public int getPageCount() {
		return pageCount;
	}

	public String[] getCategories() {
		return categories;
	}

	public String getLanguage() {
		return language;
	}
	
	
}
