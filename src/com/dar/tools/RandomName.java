package com.dar.tools;

public class RandomName {

	final static String lexicon = "abcdefghijklmnopqrstuvwxyz12345674890";

	final static java.util.Random rand = new java.util.Random();

	public static String randomIdentifier() {
	    StringBuilder builder = new StringBuilder();
	    while(builder.toString().length() == 0) {
	        int length = rand.nextInt(5)+30;
	        for(int i = 0; i < length; i++) {
	            builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
	        }
	    }
	    return builder.toString();
	}
}
