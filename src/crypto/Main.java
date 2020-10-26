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
		

    // System.out.println("Original input sanitized : " + messageClean);
		// System.out.println();


		System.out.println("------Caesar------");
		// testCaesar(messageBytes, keyBytes[0]);
    
    System.out.println("------Vigenere------");
		testVigenere(messageBytes, keyBytes);

		System.out.println("------XOR-------");
		testXor(messageBytes, keyBytes[0]);

		System.out.println("------OTP-------");
		testOTP();

		System.out.println("------CBC-------");
		testCBC(messageBytes, keyBytes);

	}

	// Run the Encoding and Decoding using the caesar pattern
	public static void testCaesar(byte[] string, byte key) {
		
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
			
	
		
		// Decoding without key
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		// Helper.writeStringToFile(sDA, "bruteForceCaesar.txt"); TODO : BROKEN 


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

		byte[] message = stringToBytes("helloworld");
		// This control data has been generated with an external Xor cipher. 
		byte[] controlData = { 108, 97, 104, 104, 107, 115, 107, 118, 104, 96 };
		byte[] ciphered = Encrypt.xor(message, (byte) 4);
		assert Arrays.equals(ciphered, controlData);

		System.out.println("XOR tested successfully.");
	}

	public static void testOTP()
	{
		byte[] pad = stringToBytes("allonsenfants");
		byte[] message = stringToBytes("helloworld");

		// Test symetry
		byte[] ciphered = Encrypt.oneTimePad(message, pad);
		byte[] cipheredBack = Encrypt.oneTimePad(ciphered, pad);
		assert Arrays.equals(message, cipheredBack);
		
		// TODO implement other tests 
		System.out.println("OTP tested successfully.");
	}

	public static void testCBC(byte[] textBytes, byte[] pad){
		Encrypt.cbc(textBytes, pad); 
		// TODO Implement good tests for this ?
	}



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