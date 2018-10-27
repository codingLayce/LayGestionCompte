package fr.layce.V1_0.modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Config {
	private static HashMap<String, String> configs;
	private static ArrayList<String> lines;
	private static ArrayList<String> entetes;
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String FILE = "config.ini";
	private final static String VERSION = "0.2";
	
	public static void init() throws IOException {
		initConfigs();
		checkVersion();
	}
	
	private static void checkVersion() throws IOException {
		if (!configs.get("version").equals(VERSION)) {
			configs.put("version", VERSION);
			save();
		}
	}
	
	private static void initConfigs() throws IOException {
		initArray();
		
		File f = new File(FILE);
		if (!f.exists()) {
			createConfigFile(f);
		}
		readLines(f);
		for (String s : lines) {
			String[] arr = s.split("=");
			if (arr.length >= 2)
				configs.put(arr[0], arr[1]);
		}
	}
	
	private static void initArray() {
		configs = new HashMap<String, String>();
		entetes = new ArrayList<String>();
		entetes.add("version");
		entetes.add("cheminDefaut");
		for (String s : entetes) {
			configs.put(s, "");
		}
	}
	
	private static void readLines(File f) throws FileNotFoundException{
		lines = new ArrayList<String>();
		Scanner in;
		in = new Scanner(new FileReader(f.getName()));
		while (in.hasNext())
			lines.add(in.nextLine());
		in.close();
	}
	
	private static void createConfigFile(File f) throws IOException {
		FileOutputStream out;
		out = new FileOutputStream(f);
		for (String s : entetes) {
			String st = s + "=" + NEW_LINE;
			out.write(st.getBytes());
		}
		defaultValues(f);
		out.close();
	}
	
	private static void defaultValues(File f) throws IOException {
		String cheminDefaut = "C:/users/" + System.getProperty("user.name") + "/Desktop/";
		
		FileOutputStream out;
		BufferedReader br = new BufferedReader(new FileReader(FILE));
		String line = "";
		String input = "";
		while ((line = br.readLine()) != null) {
			input += line + NEW_LINE;
		}
		input = input.replace("cheminDefaut=", "cheminDefaut=" + cheminDefaut);
		input = input.replace("version=", "version=" + VERSION);
		
		out = new FileOutputStream(f);
		out.write(input.getBytes());
		br.close();
		out.close();
	}
	
	public static void save() throws IOException {
		String data = "";
		for (String s : entetes) {
			data += s + "=" + configs.get(s) + NEW_LINE;
		}
		FileOutputStream out = new FileOutputStream(new File(FILE));
		out.write(data.getBytes());
		out.close();
	}
	
	/* GETTERS */
	public static HashMap<String, String> getConfigs(){
		return configs;
	}
}
