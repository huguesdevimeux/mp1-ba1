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
	public static final Scanner input = new Scanner(System.in);

	public static void main(String args[]) {

		String inputMessage = Helper.readStringFromFile("text_one.txt");
		// String inputMessage2 = Helper.readStringFromFile("text_two.txt");

		String key = "2cF%5";
		String messageClean = cleanString(inputMessage);

		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);

		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		// method that will display the user's options to encrypt or decrypt
		// (ie: the .txt file, their sentence or our unit tests)
		possibilities();

		// asking the user to input either 0,1,2 to choose what message they want to
		// test
		int testing = input.nextInt();
		// the console will display an error message if the 0assertion is false
		assert (testing == 0 || testing == 1 || testing == 2);

		if (testing == 0) {
			// the user will decide whether to encrypt or decrypt to message above
			System.out.println(
					"Now enter 0 if you want to encrypt the message above or 1 if you want to decrypt a message");
			int encryptOrDecrypt = input.nextInt();
			// this variable must be either 0 or 1 as there are only 2 possibilities
			// the while loop checks if the user has put a number other than 0 or 1
			while (encryptOrDecrypt != 0 && encryptOrDecrypt != 1) {
				System.out.println("Please enter either 0 or 1");
				encryptOrDecrypt = input.nextInt();
			}
			// switch statement to evaluate the cases where we encrypt or decrypt
			switch (encryptOrDecrypt) {
				case 0:
					// created 2 methods that we will use twice, to avoid redundant code
					// created a method that will display all possible algorithms you can use to
					// encrypt
					whatEncryption();
					int typeEncrypt = askUser(0, 4);
					// method to ask user to select an algorithm to encrypt
					// limited to 5 options as there are 5 algorithms possible
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
					// printing the ciphered version choosing the algorithm chosen by the user
					String cipher = Encrypt.encrypt(messageClean, key, typeDecrypt);
					System.out.println("The message provided in main, encoded in the algorithm chosen is : \n" + cipher
							+ "\n"
							+ "Now we Decrypt the encrypted message, seeing if we can find the original message again");
					System.out.println();
					// Because we are using brute force for XOR, you will need to manually look for
					// the decrypted message
					String deciphered = Decrypt.breakCipher(cipher, typeDecrypt);
					System.out.println(deciphered);
					break;
			}

		} else if (testing == 1) {
			// user inputs their own message

			System.out.println("Please input your message\n"
					+ "Please ensure you write a long enough message as we will then attempt to decrypt it\n"
					+ "using caesar/vigenere with frequencies, which rely on probabilities");
			input.nextLine();
			String myMessage = input.nextLine();

			System.out.println("Now please input a key");
			key = input.nextLine();
			assert (key != null);

			whatEncryption();
			int typeEncrypt = askUser(0, 4);
			String myMessageCiphered = Encrypt.encrypt(myMessage, key, typeEncrypt);
			System.out.println(myMessageCiphered + "\n");
			System.out.println(
					"Now do you want to decipher the encrypted message (using the same key and same algortihm) [y/n] ?\n"
							+ "Attention! Please input y ONLY if you have used Caesar, Xor or Vigenere to encrypt.\n"
							+ "otherwise the console will display an assertion error\n"
							+ "(These are the only algorithms that we can use to decrypt)\n"
							+ "if you chose xor, you will obtain an array if you decide to decrypt");
			input.nextLine();
			String toDecrypt = input.nextLine();
			assert (toDecrypt.equals("y") || toDecrypt.equals("n"));

			switch (toDecrypt) {
				case "y":
					// we are using the message that was encrypted before with the same algorithm to
					// decrpyt
					assert (typeEncrypt <= 2 && typeEncrypt >= 0);
					String decrypted = Decrypt.breakCipher(myMessageCiphered, typeEncrypt);
					System.out.println("The decrypted message is \n" + decrypted);
					break;

				case "n":
					break;
			}

		} else if (testing == 2) {
			System.out.println("------Caesar------");
			testCaesar(messageBytes, keyBytes[0]);

			System.out.println("------Vigenere------");
			testVigenere(messageBytes, keyBytes);

			System.out.println("------XOR-------");
			testXor(messageBytes, keyBytes[0]);

			System.out.println("------OTP-------");
			testOTP(messageBytes, keyBytes); 

			System.out.println("------CBC-------");
			testCBC(messageBytes, keyBytes);

			System.out.println("------UNIT TESTS-------");
			testsUnitsVigenere();
		}
		System.out.println();
		// method that will ask if the user needs any help with further information
		help();
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
		byte[] result = Encrypt.xor(textBytes, key, true);
		result = Encrypt.xor(result, key, true);
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
		/*
		 * Testing xorBruteForce with the following array the following array
		 * corresponds to "bonne journée" in bytes the Helper method will create a .txt
		 * file which will contain the 256 different arrays and you can manually look
		 * for "bonne journée"
		 */
		byte[] test = { 98, 111, 110, 110, 101, 32, 106, 111, 117, 114, 110, -23, 101 };
		byte[][] bruteForceResult = Decrypt.xorBruteForce(test);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceXor.txt");

		System.out.println("XOR encryption tested successfully.");
	}

	public static void testOTP(byte[] string, byte[] key) {
		byte[] pad = stringToBytes("allonsenfants");
		byte[] message = stringToBytes("helloworld");

		// Test symetry
		byte[] ciphered = Encrypt.oneTimePad(message, pad);
		byte[] cipheredBack = Encrypt.oneTimePad(ciphered, pad);
		assert Arrays.equals(message, cipheredBack);

		// test symetry with user-dep input
		byte[] pad2 = Encrypt.generatePad(string.length);
		assert pad2.length == string.length; 
		byte[] ciphered2 = Encrypt.oneTimePad(string, pad2);
		byte[] cipheredBack2 = Encrypt.oneTimePad(ciphered2, pad2);
		assert Arrays.equals(string, cipheredBack2);

		System.out.println("OTP encryption tested successfully.");
	}

	public static void testCBC(byte[] textBytes, byte[] pad) {
		byte[] resultTemp = Encrypt.cbc(textBytes, pad);
		resultTemp = Decrypt.decryptCBC(resultTemp, pad);
		// Test symetry
		assert (Arrays.equals(textBytes, resultTemp));
		System.out.println("CBC encryption tested successfully.");
	}

	public static void testVigenere(byte[] string, byte[] key) {

		byte[] result = Encrypt.vigenere(string, key);
		String plainText = "bonne journée";
		byte[] plainBytes = plainText.getBytes(StandardCharsets.ISO_8859_1);

		// creating array of keys used in example
		byte[] keys = { 1, 2, 3 };
		// encrypting without encoding spaces(spaceEncoding (boolean parameter of
		// vigenere) considered false)
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
		// This is weird way to so unit tests, but as we don't know yet how to do them
		// properly, we'll stick to that.

		// Test getShiftMaxima
		int[] sorted = { 1, 2, 3, 4, 5 };
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
		assert Arrays.equals(c, new byte[] { 0, 3, 6 });
		c = Decrypt.getPartialArray(a, 1, 3);
		assert Arrays.equals(c, new byte[] { 1, 4 });
		
		byte[] d = {(byte) 1, (byte) 2}; 
		float[] res = Decrypt.computeFrequencies(d);
		assert (res[1 + 128] == 0.5);  

		System.out.println("Vigenere unit-tests passed");
	}

	// method will simply display the different algorithms you can choose from - to
	// not 'pollute' the main method
	public static void whatEncryption() {
		System.out.println("Enter a number between 0 and 4. If you select \n"
				+ "0 : the message will be encrypted using caesar \n"
				+ "1 : the message will be encrypted using vigenere \n"
				+ "2 : the message will be encrypted using xor \n"
				+ "3 : the message will be encrypted using onetime \n" + "4 : the message will be encrypted using CBC");
	}

	// same as the previous method
	public static int askUser(int lowBound, int upBound) {
		int response = input.nextInt();
		while (response < lowBound || response > upBound) {
			System.out.println("Please input a number between " + lowBound + " and " + upBound);
			response = input.nextInt();
		}
		return response;
	}

	// method will ask what the user wants to encrypt or decrypt
	public static void possibilities() {
		System.out.println("Do you want to test encryption or decryption \n"
				+ "of the String from the text.txt file, from your own input or from our own examples? \nPlease input: \n"
				+ "0 in the console if you want to encrypt or decrypt the long message \n"
				+ "1 if you want to encrypt your own message \n" + "2 if you want to test our examples\n");
	}

	// method that will be called when the program is over to ask whether the user
	// needs additional information
	public static void help() {
		System.out.println("Do you require further information or help understanding the program [y/n]");
		input.nextLine();
		String a = input.nextLine();
		switch (a) {
			case "y":
				System.out.println("Firstly, the program will display all your options:\n"
						+ "You can select the sentence provided in res: text_one.txt, or your own sentence, or our unit tests\n"
						+ "you will need to input either 0, 1 or 2 to choose what you want to encrypt/decrypt\n(be careful, "
						+ "inputting anything else will result in an assertion error)\n"
						+ "you will then be asked to choose whether you want to encrypt or decrypt the message you've chosen just before\n"
						+ "0 to encrypt and 1 to decrypt, any other number or string will not work and will result in error\n"
						+ "then, you will choose which algorithm to use to encrypt/decrypt."
						+ "if you choose to decrypt the .txt file, \nwe will first encrypt it using the algorithm chosen and "
						+ "decrypt it back\n"
						+ "if you choose to decrypt your own message, you will be asked to type in a key for encryption");
				break;
			case "n":
				System.exit(0);
				break;
		}
	}

}
