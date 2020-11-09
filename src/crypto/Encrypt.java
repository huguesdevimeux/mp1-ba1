package crypto;

import java.util.Arrays;
import java.util.Random;

public class Encrypt {

	public static final int CAESAR = 0;
	public static final int VIGENERE = 1;
	public static final int XOR = 2;
	public static final int ONETIME = 3;
	public static final int CBC = 4;

	public static final byte SPACE = 32;

	final static Random rand = new Random();

	// -----------------------General-------------------------

	/**
	 * General method to encode a message using a key, you can choose the method you
	 * want to use to encode.
	 * 
	 * @param message the message to encode already cleaned
	 * @param key     the key used to encode
	 * @param type    the method used to encode : 0 = Caesar, 1 = Vigenere, 2 = XOR,
	 *                3 = One time pad, 4 = CBC
	 * 
	 * @return an encoded String if the method is called with an unknown type of
	 *         algorithm, it returns the original message
	 */
	public static String encrypt(String plainText, String key, int type) {

		byte[] message = Helper.stringToBytes(plainText);
		// this array will convert the key into an array of bytes
		// to use later to encode the message
		byte[] keyArray = Helper.stringToBytes(key);

		// asserting the length keyArray is greater than 0 as a key with no elements
		// cannot encode a message
		assert (keyArray.length > 0);

		// some encryption methods such as caesar or xor only use one byte to encode a
		// message
		// singleKey will be the first element of keyArray when using caesar or xor
		// we could directly use keyArray[0] but singleKey will make it easier to notice
		// which algorithms
		// use a single byte as key
		byte singleKey = keyArray[0];

		// creating an array that will stock the values of the array of the algorithm
		// chosen
		byte[] cipher;

		// we initialise the ciphered message as null and the following switch statement
		// will affect the String
		String ciphered = "";

		switch (type) {

			case CAESAR: // == 0
				// we are not encoding spaces
				// so we do not have to input a third paramater which is a boolean for
				// spaceEncoding
				// using singleKey or keyArray[0] will give the same result
				cipher = caesar(message, singleKey);
				ciphered = Helper.bytesToString(cipher);
				break;

			case VIGENERE: // == 1
				cipher = vigenere(message, keyArray);
				ciphered = Helper.bytesToString(cipher);
				break;

			case XOR: // == 2
				cipher = xor(message, singleKey);
				ciphered = Helper.bytesToString(cipher);
				break;

			case ONETIME: // == 3
				// for OTP, we need to generate a pad that will allow to encrypt the message
				byte[] pad = generatePad(message.length);
				cipher = oneTimePad(message, pad);
				ciphered = Helper.bytesToString(cipher);
				break;

			case CBC:// == 4
				cipher = cbc(message, keyArray);
				ciphered = Helper.bytesToString(cipher);
				break;

			// no default statement needed as a while loop in Main.java already accounts for
			// the fact that the user must input a number between 0 and 4
		}
		return ciphered;
	}

	// -----------------------Caesar-------------------------

	/**
	 * Method to encode a byte array message using a single character key the key is
	 * simply added to each byte of the original message
	 * 
	 * @param plainText     The byte array representing the string to encode
	 * @param key           the byte corresponding to the char we use to shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key, boolean spaceEncoding) {
		assert (plainText != null);

		byte[] ciphered = new byte[plainText.length];
		for (int i = 0; i < plainText.length; ++i) {

			// copying elements of plainText in ciphered to not interfere with reference
			ciphered[i] = plainText[i];

			if (spaceEncoding == false) {

				if (ciphered[i] != SPACE) {
					ciphered[i] += (byte) key;
				}
			} else {
				ciphered[i] += (byte) key;
			}
		}
		return ciphered;
	}

	/**
	 * Method to encode a byte array message using a single character key the key is
	 * simply added to each byte of the original message spaces are not encoded
	 * 
	 * @param plainText The byte array representing the string to encode
	 * @param key       the byte corresponding to the char we use to shift
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key) {
		return caesar(plainText, key, false);
	}

	// -----------------------XOR-------------------------

	/**
	 * Method to encode a byte array using a XOR with a single byte long key
	 * 
	 * @param plaintext     the byte array representing the string to encode
	 * @param key           the byte we will use to XOR
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key, boolean spaceEncoding) {
		assert plainText.length > 0 : "Text is empty!";
		byte[] ciphered = new byte[plainText.length];
		for (int i = 0; i < plainText.length; i++) {
			if (!(spaceEncoding) && plainText[i] == SPACE) {
				ciphered[i] = SPACE;
			} else {
				ciphered[i] = (byte) (plainText[i] ^ key);
			}
		}
		return ciphered;
	}

	/**
	 * Method to encode a byte array using a XOR with a single byte long key spaces
	 * are not encoded
	 * 
	 * @param key the byte we will use to XOR
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key) {
		return xor(plainText, key, false);
	}
	// -----------------------Vigenere-------------------------

	/**
	 * Method to encode a byte array using a byte array keyword The keyword is
	 * repeated along the message to encode The bytes of the keyword are added to
	 * those of the message to encode
	 * 
	 * @param plainText     the byte array representing the message to encode
	 * @param keyword       the byte array representing the key used to perform the
	 *                      shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword, boolean spaceEncoding) {
		assert (plainText != null);
		assert (keyword.length > 0 && keyword.length <= plainText.length);

		byte[] ciphered = new byte[plainText.length];
		// setting the variable below to allow for elements of
		// plainText array to go back to the beginning of keyword array
		int keywordArray_index = 0;

		for (int i = 0; i < plainText.length; ++i) {
			if (!spaceEncoding && plainText[i] == SPACE) {
				ciphered[i] = plainText[i];
			} else {
				ciphered[i] = (byte) (plainText[i] + keyword[keywordArray_index % keyword.length]);
				++keywordArray_index;
			}
		}
		return ciphered; 
	}

	/**
	 * Method to encode a byte array using a byte array keyword The keyword is
	 * repeated along the message to encode spaces are not encoded The bytes of the
	 * keyword are added to those of the message to encode
	 * 
	 * @param plainText the byte array representing the message to encode
	 * @param keyword   the byte array representing the key used to perform the
	 *                  shift
	 * @return an encoded byte array
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword) {
		return vigenere(plainText, keyword, false);
	}

	// -----------------------One Time Pad-------------------------
	/**
	 * Method to encode a byte array using a one time pad of the same length. The
	 * method XOR them together. Spaces are by default encoded. The pad length must
	 * be equal or longer than the message.
	 * 
	 * @param plainText the byte array representing the string to encode
	 * @param pad       the one time pad
	 * @return an encoded byte array
	 */
	public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
		assert (pad.length >= plainText.length);

		byte[] ciphered = new byte[plainText.length];
		for (int i = 0; i < plainText.length; i++) {
			ciphered[i] = (byte) (plainText[i] ^ pad[i]);
		}
		return ciphered;
	}

	// -----------------------Basic CBC-------------------------

	/**
	 * Method applying a basic chain block counter of XOR without encryption method.
	 * Encodes spaces. Important note : This method is not symetric !
	 * 
	 * @param plainText the byte array representing the string to encode
	 * @param iv        the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return an encoded byte array
	 */
	public static byte[] cbc(byte[] plainText, byte[] iv) {
		return cbcInternal(plainText, iv, false);
	}

	/**
	 * Method applying a basic chain block counter of XOR without encryption method.
	 * Can be used in both senses, ciphering or deciphering. Encodes spaces.
	 * 
	 * @param plainText the byte array representing the string to encode
	 * @param iv        the pad of size BLOCKSIZE we use to start the chain encoding
	 * @param decipher  Whether or not the cbc is in decipher mode.
	 * @return an encoded byte array
	 */
	public static byte[] cbcInternal(byte[] plainText, byte[] iv, boolean decipher) {

		byte[] ciphered = new byte[plainText.length];
		// We copy it to avoid any reference related problem, as we will change its
		// value.
		byte[] temp_pad = iv.clone();
		int lengthBlock = iv.length;
		byte[] blockCipheredTemp = new byte[lengthBlock];
		int shift = lengthBlock;
		for (int indexBlock = 0; indexBlock < plainText.length; indexBlock += lengthBlock) {
			// Arrays.copyOfRange fill up with zeros the array given as parameters, if the
			// upper bound is greater than the size of the array.
			// As zero would be processed by the ciphers functions as a normal elements, we
			// want to avoid this and reduce the size of t
			// the array so there won't be any additional zero.
			if ((indexBlock + lengthBlock) > plainText.length) {
				shift = plainText.length - indexBlock;
			}
			byte[] blockToCipher = Arrays.copyOfRange(plainText, indexBlock, indexBlock + shift);

			blockCipheredTemp = oneTimePad(blockToCipher, temp_pad);
			// Initialize the pad for the next iteration.
			// Deciphering method is slightly method than ciphering.
			if (decipher) {
				temp_pad = blockToCipher;
			} else {
				temp_pad = blockCipheredTemp.clone();
			}
			// This aims to add append every element of the block ciphered to ciphered.
			for (int j = 0; j < shift; j++) {
				int indexCiphered = indexBlock + j;
				ciphered[indexCiphered] = blockCipheredTemp[j];
			}
		}
		return ciphered;
	}

	/**
	 * Generate a random pad/IV of bytes to be used for encoding
	 * 
	 * @param size the size of the pad
	 * @return random bytes in an array
	 */
	public static byte[] generatePad(int size) {
		assert (size > 0);
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			result[i] = (byte) rand.nextInt(256);
		}
		return result;
	}
}