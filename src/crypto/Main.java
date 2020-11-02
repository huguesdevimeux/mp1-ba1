package crypto;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {

	// ---------------------------MAIN---------------------------

	
	public static void main(String args[]) {
	
		Scanner input = new Scanner(System.in);

		String inputMessage = Helper.readStringFromFile("text_one.txt");
		String key = "2cF%5";

		String messageClean = cleanString(inputMessage);

		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();

		// the user will decide whether to encrypt or decrypt to message above
		System.out.println("Please decide whether we will encrypt or decrypt the message \n"
				+ "enter 0 if you want to encrypt the message above or 1 if you want to decrypt a message");
		int encryptOrDecrypt = input.nextInt();

		while (encryptOrDecrypt != 0 && encryptOrDecrypt != 1) {
			System.out.println("Please enter either 0 or 1");
			encryptOrDecrypt = input.nextInt();
		}

		// switch statement to evaluate the cases where we encrypt or decrypt
		switch (encryptOrDecrypt) {
		case 0:
			System.out.println("Enter a number between 0 and 4. If you select \n"
					+ "0 : the message will be encrypted using caesar \n"
					+ "1 : the message will be encrypted using vigenere \n"
					+ "2 : the message will be encrypted using xor \n"
					+ "3 : the message will be encrypted using onetime \n"
					+ "4 : the message will be encrypted using CBC");

			int typeEncrypt = input.nextInt();

			while (typeEncrypt < 0 || typeEncrypt > 4) {
				System.out.println("Please input a number between 0 and 4");
				typeEncrypt = input.nextInt();
			}

			String ciphered = Encrypt.encrypt(messageClean, key, typeEncrypt);
			System.out.println(ciphered);
			break;

		case 1:
			System.out.println("Enter a number between 0 and 2. If you select \n"
					+ "0 : the message will be decrypted using caesar with frequencies \n"
					+ "1 : the message will be decrypted using vigenere frequencies \n"
					+ "2 : the message will be decrypted using xor brute force \n");

			int typeDecrypt = input.nextInt();

			while (typeDecrypt < 0 || typeDecrypt > 2) {
				System.out.println("Please input a number between 0 and 2");
				typeDecrypt = input.nextInt();
			}

			// printing the ciphered version ch0osing the algorithm chosen by the user
			String cipher = Encrypt.encrypt(messageClean, key, typeDecrypt);
			System.out.println("The message provided in main, encoded in the algorithm chosen is : \n" + cipher);
			System.out.println();

			System.out
					.println("Now we Decrypt the encrypted message, seeing if we can find the original message again");
			System.out.println();

			// Because we are using brute force for XOR, you will need to manually look for
			// the decrypted message
			String deciphered = Decrypt.breakCipher(cipher, typeDecrypt);
			System.out.println(deciphered);
		}
	
		System.out.println("------Caesar------");
		testCaesar(messageBytes, keyBytes[0]);

		System.out.println("------Vigenere------");
		testVigenere(messageBytes, keyBytes);

		System.out.println("------XOR-------");
		testXor(messageBytes, keyBytes[0]);

		System.out.println("------OTP-------");
		testOTP(); // TODO : make it input dependent !

		System.out.println("------CBC-------");
		testCBC(messageBytes, keyBytes);

		System.out.println("------UNIT TESTS-------");
		testsUnitsVigenere();
		
	}

	// Run the Encoding and Decoding using the caesar pattern
	public static void testCaesar(byte[] string, byte key) {
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
		
		byte[] encryptedMessage = Encrypt.caesar(string, key);
		byte guessedKey = Decrypt.caesarWithFrequencies(encryptedMessage); 
		assert (key == guessedKey); 
		
		// Testing caesarBruteForce with the following array
		// this array corresponds to "i want" in bytes
		// the following algorithm will create a .txt file and will contain 256
		// different arrays of characters
		// you can manually look for "i want" in the .txt file
		byte[] test = { 105, 32, 119, 97, 110, 116 };
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(test);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");
	
		System.out.println("Caesar Cryptanalysis tested successfully.");
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

		
		
		/*
		 * Testing xorBruteForce with the following array 
		 * the following array corresponds to "bonne journée" in bytes
		 * the Helper method will create a .txt file which will contain
		 * the 256 different arrays and you can manually look
		 * for "bonne journée"
		 */

		byte[] test = { 98, 111, 110, 110, 101, 32, 106, 111, 117, 114, 110, -23, 101 };
		byte[][] bruteForceResult = Decrypt.xorBruteForce(test);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceXor.txt");

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
		byte[] resultTemp = Encrypt.cbc(textBytes, pad);
		resultTemp = Decrypt.decryptCBC(resultTemp, pad);
		
		// Test symetry 
		assert (Arrays.equals(textBytes, resultTemp));
		System.out.println("CBC tested successfully.");

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

		System.out.println("Vigenere encryption tested successfully.");

		// Key length finder
		byte[] ciphered = Encrypt.vigenere(string, key);
		// NOTE removeSpaces is unit tested below. 
		List<Byte> messageBytes2 = Decrypt.removeSpaces(ciphered); 
		int a = Decrypt.vigenereFindKeyLength(messageBytes2);
		assert (a == key.length);

		byte[] guessedDecryptKey = Decrypt.vigenereWithFrequencies(result);
		assert (guessedDecryptKey.length == key.length);
		for (int i = 0; i < guessedDecryptKey.length; i++) {
			assert -guessedDecryptKey[i] == key[i];
		}
		byte[] decryptedUnsingGuessedKey = Encrypt.vigenere(result, guessedDecryptKey, false);
		assert (Arrays.equals(decryptedUnsingGuessedKey, string)); 
		System.out.println("Vigenere decryption tested successfully");

	}

	public static void testsUnitsVigenere() {
		// This is weird way to so unit tests, but as we don't know yet how to do them properly, we'll stick to that.
		// TODO : write a proper test for that. 
		// int a = Decrypt.getNumberCoincidences(tested, 2); 
		// assert (a == 1);
		// a = Decrypt.getNumberCoincidences(tested, 1);
		// assert (a == 0);
		
		// Test getShiftMaxima
		int[] sorted = {1,2,3,4,5};
		ArrayList<Integer> b = Decrypt.getShiftMaxima(sorted);
		assert (b.size() == 1);
		assert (b.get(0) == 4);  
		int[] allSame = { 1, 1, 1, 1 };
		b = Decrypt.getShiftMaxima(allSame);
		assert (b.size() == 0);
		int[] normal = { 1, 2, 4, 3, 3 };
		b = Decrypt.getShiftMaxima(normal);
		assert (b.size() == 1);
		assert (b.get(0) == 2);
		
		List<Byte> a = Arrays.asList(new Byte[] { 0, 1, 2, 3, 4, 5, 6 });
		byte[] c = Decrypt.getPartialArray(a, 0, 3);
		assert Arrays.equals(c, new byte[] { 0, 3,6 });
		c = Decrypt.getPartialArray(a, 1, 3);
		assert Arrays.equals(c, new byte[] { 1, 4 });

		System.out.println("Vigenere unit-tests passed");
	}
}