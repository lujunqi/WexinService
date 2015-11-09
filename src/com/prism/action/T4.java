package com.prism.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLEncoder;

public class T4 {

	public static void main(String[] args) {
		try {
			String v = "%E5%8D%A2%E4%BF%8A%E5%A5%87";
			System.out.println(v.replaceAll("\\%", "\\$"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
