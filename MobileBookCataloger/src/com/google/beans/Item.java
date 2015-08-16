package com.google.beans;

public class Item {
	private String id;
	private VolumeInfo volumeInfo;
	
	public Item() {
	}
	
	@Override
	public String toString() {
		return volumeInfo.toString();
	}
	
	public VolumeInfo getVolumeInfo() {
		return volumeInfo;
	}

	public String getId() {
		return id;
	}

}
