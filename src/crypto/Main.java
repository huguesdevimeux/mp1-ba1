package crypto;

import static crypto.Helper.bytesToString;
import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {

	// ---------------------------MAIN---------------------------

	
	public static void main(String args[]) {

		String inputMessage = Helper.readStringFromFile("text_one.txt");
		String key = "2cF%5";

		String messageClean = cleanString(inputMessage);

		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		//Caesar testing
		System.out.println("------Caesar------");
		testCaesar(messageBytes, keyBytes[0]);

		//Vigenere testing
		System.out.println("------Vigenere------");
		testVigenere(messageBytes, keyBytes);
	}

	// Run the Encoding and Decoding using the caesar pattern
	public static void testCaesar(byte[] string, byte key) {
		// Encoding
		byte[] result = Encrypt.caesar(string, key);
		String plainText = "bonne journée";
		byte[] plainBytes = plainText.getBytes(StandardCharsets.ISO_8859_1);

		byte[] encrypt1 = Encrypt.caesar(plainBytes, (byte) 3);
		String cipherText = Helper.bytesToString(encrypt1);
		assert (cipherText.equals("erqqh mrxuqìh"));

		byte[] encrypt2 = Encrypt.caesar(plainBytes, (byte) 3, true);
		String cipherText1 = Helper.bytesToString(encrypt2);
		// Test Caesar with spaces
		assert (cipherText1.equals("erqqh#mrxuqìh"));

		System.out.println("Caesar tested successfully.");

		/*
		 * // Decoding with key String sD = bytesToString(Encrypt.caesar(result, (byte)
		 * (-key))); System.out.println("Decoded knowing the key : " + sD);
		 */
		
		//fait au dessus non??
	
	// Decoding without key
	byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
	String sDA = Decrypt.arrayToString(bruteForceResult);
	// Helper.writeStringToFile(sDA, "bruteForceCaesar.txt"); TODO : BROKEN 

	byte decodingKey = Decrypt.caesarWithFrequencies(result);
	String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
//	System.out.println("Decoded without knowing the key : " + sFD);
}

	public static void testXor(byte[] textBytes, byte key) {
		// Test symetry
		byte[] result = Encrypt.xor(textBytes, key);
		result = Encrypt.xor(result, key);
		assert (Arrays.equals(result, textBytes));

		// Test spaces disabled
		assert (Arrays.equals(Encrypt.xor(new byte[] { 32 }, (byte) 4), new byte[] { 32 }));

		// test spaces enabled :
		assert (Arrays.equals(Encrypt.xor(new byte[] { 32 }, (byte) 6, true), new byte[] { 38 }));
		// TODO : Implementing other test space related.

		System.out.println("XOR tested successfully.");
	}
	// TODO : TO BE COMPLETED



	public static void testVigenere(byte[] string, byte[] key) {

		byte[] result = Encrypt.vigenere(string, key);
		String plainText = "bonne journée";
		byte[] plainBytes = plainText.getBytes(StandardCharsets.ISO_8859_1);

		// creating array of keys used in example
		byte[] keys = { 1, 2, 3 };

		// encrypting without encoding spaces(spaceEncoding (boolean parameter of vigenere) considered false)
		byte[] cipherBytes1 = Encrypt.vigenere(plainBytes, keys, false);
		String cipherText1 = Helper.bytesToString(cipherBytes1);
		assert (cipherText1.equals("cqqog mpwuoëh"));

		// encrypting and encoding spaces(spaceEncoding considered as True)
		byte[] cipherBytes2 = Encrypt.vigenere(plainBytes, keys, true);
		String cipherText2 = Helper.bytesToString(cipherBytes2);
		assert (cipherText2.equals("cqqog#kqxspìf"));

		System.out.println("Vigenere tested successfully");
	}
}



