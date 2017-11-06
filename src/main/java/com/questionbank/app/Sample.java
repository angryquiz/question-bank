package com.questionbank.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").format(new Date()));
		
		System.out.println(isValidName("a123456789ffs012345678901234567890"));
		System.out.println(isValidTag("aaa asas sasa232-dddd_ ss!"));
	}
	
	public static boolean isValidName(String input) {
		//https://www.tutorialspoint.com/java/java_regular_expressions.htm
		String pattern = "^[A-Za-z_-][A-Za-z0-9_-]*$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(input);
		if (!m.find() || input.length() > 128) {
			return false;
		}
		return true;		
	}
	
	public static boolean isValidTag(String input) {
		//https://www.tutorialspoint.com/java/java_regular_expressions.htm
		//String pattern = "^\\w+( \\w+)*$";
		String pattern = "([\\w\\-]+)";
		//String pattern = "^[a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(input);
		if (!m.find() || input.length() > 32) {
			return false;
		}
		return true;		
	}	
	
	

}
