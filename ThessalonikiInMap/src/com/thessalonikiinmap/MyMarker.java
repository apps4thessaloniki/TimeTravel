package com.thessalonikiinmap;

public class MyMarker {

		private double markerLat;
		private double markerLng;
		private String markerTitle;
		private String markerSnippet;
		private String markerIcon;
		private String markerLink;
		
		public MyMarker(double markerLat, double markerLng, String markerTitle, 
				String markerSnippet, String markerIcon, String markerLink) {
			
			this.markerLat = markerLat;
			this.markerLng = markerLng;
			this.markerIcon = markerIcon;
			this.markerTitle = markerTitle;
			this.markerSnippet = markerSnippet;
			this.markerLink = markerLink;
		}
		

		public double getMarkerLat() {
			return markerLat;
		}

		public void setMarkerLat(double markerLat) {
			this.markerLat = markerLat;
		}

		public double getMarkerLng() {
			return markerLng;
		}

		public void setMarkerLng(double markerLng) {
			this.markerLng = markerLng;
		}

		public String getMarkerIcon() {
			return markerIcon;
		}

		public void setMarkerIcon(String markerIcon) {
			this.markerIcon = markerIcon;
		}

		public String getMarkerTitle() {
			return markerTitle;
		}

		public void setMarkerTitle(String markerTitle) {
			this.markerTitle = markerTitle;
		}

		public String getMarkerSnippet() {
			return markerSnippet;
		}

		public void setMarkerSnippet(String markerSnippet) {
			this.markerSnippet = markerSnippet;
		}

		public String getMarkerLink() {
			return markerLink;
		}

		public void setMarkerLink(String markerLink) {
			this.markerLink = markerLink;
		}
}
