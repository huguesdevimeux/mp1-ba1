package crypto;

import java.util.Arrays;
import java.util.Random;


import static crypto.Helper.*;

public class Encrypt {

	public static final int CAESAR = 0;
	public static final int VIGENERE = 1;
	public static final int XOR = 2;
	public static final int ONETIME = 3;
	public static final int CBC = 4;

	public static final byte SPACE = 32;

	final static Random rand = new Random();

	
	
	
	public static void main(String[] args) {
		byte [] plainText = {105, 32, 119, 115, 120};
		byte key = 10;
		byte [] cypherText = caesar(plainText, key);					//////TODO ATTENTION A BIEN ENLEVER -- ajouté fonction main pour tester les bails
	
		for(int val : cypherText) {
			System.out.print(val + ", ");
		}
	}
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
	public static String encrypt(String message, String key, int type) {
		// TODO: COMPLETE THIS METHOD

		return null; // TODO: to be modified
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

			ciphered[i] = plainText[i];

			if (spaceEncoding == false) {

				if (ciphered[i] != 32) {
					ciphered[i] += (byte) key;
					if (ciphered[i] >= 128) {
						ciphered[i] %= (byte) 128;
					}
				}
			} else {
				ciphered[i] += (byte) key;
				if (ciphered[i] >= 128) {
					ciphered[i] %= (byte) 128;
				}
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
		assert plainText.length > 0;
		// TODO ; Implement check for empty key ?
		byte[] ciphered = new byte[plainText.length];
		for (int i = 0; i < plainText.length; i++) {
			if (!(spaceEncoding) && plainText[i] == 32) {
				ciphered[i] = (byte) 32;
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

		// on est daccord sur cette assert?
		assert (keyword.length > 0 && keyword.length <= plainText.length);

		byte[] ciphered = new byte[plainText.length];

		for (int i = 0; i < plainText.length; ++i) {
			ciphered[i] = plainText[i];

			if (spaceEncoding == false) {

				if (ciphered[i] != 32) {

					for (int j = 0; j < keyword.length; ++j) {
						if (i == j) {
							if (i == 32) {

								ciphered[i + 1] += keyword[j];
							} else {
								ciphered[i] += keyword[j];
							}
						} else if (i > j) {

						}
					}
					if (ciphered[i] >= 128) {
						ciphered[i] -= 256;
					}

				}

			}

		}	
		return ciphered; // TODO: to be modified
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
		// TODO: COMPLETE THIS METHOD
		return null; // TODO: to be modified
	}

	// -----------------------One Time Pad-------------------------

	/**
	 * Method to encode a byte array using a one time pad of the same length. The
	 * method XOR them together. Spaces are by default encoded. 
	 * The pad length must be equal or longer than the message. 
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
	 * Encodes spaces.
	 * Important note : This method is not symetric ! 
	 * 
	 * @param plainText the byte array representing the string to encode
	 * @param iv        the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return an encoded byte array
	 */
	public static byte[] cbc(byte[] plainText, byte[] iv) {
		
		byte[] ciphered = new byte[plainText.length];
		// We copy it to avoid any reference related problem. 
		byte[] temp_pad = iv.clone(); 
		int lengthBlock = iv.length;	
		byte[] blockCipheredTemp = new byte[lengthBlock];
		int shift = lengthBlock; 
		for (int i = 0; i < plainText.length; i += lengthBlock) {
			// Arrays.copyOfRange fill up with zeros the array given as parameters, if the upper bound is greater than the size of the array. 
			// As zero would be processed by the ciphers functions as a normal elements, we want to avoid this and reduce the size of t
			// the array so there won't be any additional zero. 
			if ((i + lengthBlock) > plainText.length) {
				shift = plainText.length - i;
			}
			byte[] sliced = Arrays.copyOfRange(plainText, i, i + shift);
			
			blockCipheredTemp = oneTimePad(sliced, temp_pad);
			// Initialize the pad for the next iteration. 
			temp_pad = blockCipheredTemp.clone();
			// this aims to add append every element of the block ciphered to ciphered. 
			for (int j = 0; j < shift; j++) {
				int indexCiphered = i + j;
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
