package com.google.beans;

public class IndustryIdentifier {

	private String type;
    private String identifier;
    
    public IndustryIdentifier() {
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public String toString() {
    	return type + " - " + identifier;
    }
}
