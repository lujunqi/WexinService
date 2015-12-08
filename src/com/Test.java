package com;

import java.io.UnsupportedEncodingException;

public class Test {

	public static void main(String[] args) {
		String s;
		try {
			s = java.net.URLEncoder.encode("http://10.80.1.188/prism/xx.jsp",
					"utf-8");
			System.out.println(s);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
