package crypto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class Helper {
	private static final String SEP = File.separator;
	private static final String RES_PATH_HEADER = "res" + SEP;

	/**
	 * Method to clean a string, only keeping lower case letters, and spaces.
	 * 
	 * @param s the string to clean
	 * @return the string cleaned
	 */
	public static String cleanString(String s) {
		String cleaned = s.toLowerCase();
		cleaned = cleaned.replaceAll("[:,;.]", " ");
		cleaned = cleaned.replaceAll("[^a-z ]", ""); // 97-122,32

		assert (cleaned.matches("[a-z ]*"));
		return cleaned;
	}

	/**
	 * Method turning a String into a byte array
	 * 
	 * @param message the String
	 * @return a byte array corresponding to the String
	 */
	public static byte[] stringToBytes(String message) {
		return message.getBytes(StandardCharsets.ISO_8859_1);
	}

	/**
	 * Method turning a byte array into a String
	 * 
	 * @param numbers the byte array
	 * @return a String corresponding to the byte array
	 */
	public static String bytesToString(byte[] numbers) {
		return new String(numbers, StandardCharsets.ISO_8859_1);
	}

	/**
	 * Write a String to a file, clears the file each time it is called
	 * 
	 * @param text the string to write
	 * @param name the name of the file
	 */
	public static void writeStringToFile(String text, String name) {
		writeStringToFile(text, name, false);
	}

	/**
	 * Method to write Strings to a File, we can choose between appending or not at
	 * the end of file.
	 * 
	 * @param text the array of string lines we want to write
	 * @param name the name of the file
	 */
	public static void writeStringToFile(String text, String name, boolean append) {
		try {
			File f = new File(RES_PATH_HEADER + name);
			if (!append) {
				f.delete();
			}
			if (!f.exists()) {
				f.createNewFile();
			}

			Writer writer = new OutputStreamWriter(new FileOutputStream(f.getAbsoluteFile()));
			BufferedWriter bWriter = new BufferedWriter(writer);
			bWriter.write(text);
			bWriter.newLine();

			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to read a file into a string
	 * 
	 * @param fileName the name of the file
	 */
	public static String readStringFromFile(String fileName) {
		String string = "";
		File file = new File(RES_PATH_HEADER + fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String temp;
			boolean checkFirst = false;
			while ((temp = br.readLine()) != null) {
				if (checkFirst) {
					string += (" " + temp);
				} else {
					string += temp;
					checkFirst = true;
				}
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return string;

	}

	/***
	 * Convert a list of byte into it's string representation in binary.
	 * 
	 * @param bytes
	 * @return a String corresponding to the byte array converted.
	 */
	public static String bytesToBinaryRepr(byte[] bytes) {
		String result = "";
		for (byte el : bytes) {
			result += Integer.toBinaryString((el & 0xFF) + 0x100).substring(1);
		}
		return result;
	}

	public static String byteToBinaryRepr(byte byteToRepr) {
		return bytesToBinaryRepr(new byte[] { byteToRepr });
	}

}
