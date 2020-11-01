package crypto;

import java.util.List;

public class Decrypt {
	
	
	public static final int ALPHABETSIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1 ; //256
	public static final int APOSITION = 97 + ALPHABETSIZE/2; 
	
	//source : https://en.wikipedia.org/wiki/Letter_frequency
	public static final double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};
	
	
	/**
	 * Method to break a string encoded with different types of cryptosystems
	 * @param type the integer representing the method to break : 0 = Caesar, 1 = Vigenere, 2 = XOR
	 * @return the decoded string or the original encoded message if type is not in the list above.
	 */
	public static String breakCipher(String cipher, int type) {
		
		//firstly let's assert the value of type is comprised between 0 and 2
		//because there are 2 - 0 + 1 = 3 algorithms considered
		assert(type >= 0 && type <= 2);
		
		//converting cipher in a byte array
		byte [] message = Helper.stringToBytes(cipher);
		
		//initialising deciphered string to null
		//the following switch statement will affect the string
		String deciphered = "";
		
		
		switch(type) {
		
		//we can use the constants provided in encrypt with CAESAR = 0
		//VIGENERE = 1
		//XOR = 2
		
		case Encrypt.CAESAR:
			
			byte guessedKey = caesarWithFrequencies(message);			
			byte[] decipher = Encrypt.caesar(message, (byte) -guessedKey);
			deciphered = Helper.bytesToString(decipher);
			break;
			
		case Encrypt.VIGENERE:
			
			break;
			
			
		case Encrypt.XOR:
			//creating a 2D array that will stock the values of the array for the xor brute force algorithm

			byte[][] xorDecipher = xorBruteForce(message);
			deciphered = arrayToString(xorDecipher);
			break;

			
		}		
		return deciphered; 
	}
	
	/**
	 * Converts a 2D byte array to a String
	 * @param bruteForceResult a 2D byte array containing the result of a brute force method
	 */
	public static String arrayToString(byte[][] bruteForceResult) {

		//initalising a string with "" 
		//result will be the final String, combining the strings of all the arrays
		
		//decoded will be the string of every array in the 2D array
		//result will combine every "decoded" array
		String result = "";
		
		for (int i = 0; i < bruteForceResult.length; ++i) {
			result +=  Helper.bytesToString(bruteForceResult[i]) + System.lineSeparator();
		} 
		return result;
	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 *  Method to decode a byte array  encoded using the Caesar scheme
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return a 2D byte array containing all the possibilities
	 */
	public static byte[][] caesarBruteForce(byte[] cipher) {

		assert (cipher.length > 0);

		// We are going to use the following array to stock caesar encryption
		// with the ciphered text and the POTENTIAL key
		byte[] potential;

		// declaring a 2D array that will stock in each line, the potential array
		// will contain 256 lines and the number of columns depends on the length of
		// cipher array
		byte[][] bruteForcePossibilities = new byte[ALPHABETSIZE][cipher.length];

		for (byte key = -128; key < 127; ++key) {
			potential = Encrypt.caesar(cipher, (byte) key);
			bruteForcePossibilities[key + 128] = potential;
		}

		return bruteForcePossibilities;
	}

	/**
	 * Method that finds the key to decode a Caesar encoding by comparing frequencies
	 * @param cipherText the byte array representing the encoded text
	 * @return the encoding key
	 */
	public static byte caesarWithFrequencies(byte[] cipherText) {
		//TODO : COMPLETE THIS METHOD

		return -1; //TODO: to be modified
	}
	
	/**
	 * Method that computes the frequencies of letters inside a byte array corresponding to a String
	 * @param cipherText the byte array 
	 * @return the character frequencies as an array of float
	 */
	public static float[] computeFrequencies(byte[] cipherText) {
		// TODO Unit TEST ! 
		float[] frequencies = new float[ALPHABETSIZE];
		int numberCharNotSpace = 0; 
		for (byte charTemp : cipherText) {

			// We have to skip the spaces, which does not make any sen .. well, we have to 
			if (charTemp != 32) {
				numberCharNotSpace += 1;
				// The index is charTemp shifted by 126, as index are only positives integrers
				// and bytes casted to int are from -128 to 127. 
				frequencies[charTemp + 128] += 1.0;
			}
		}
		for (int i = 0; i < frequencies.length; i++) {
			frequencies[i] /= numberCharNotSpace;
		}
		return frequencies;
	}
	
	
	/**
	 * Method that finds the key used by a  Caesar encoding from an array of character frequencies
	 * @param charFrequencies the array of character frequencies
	 * @return the key
	 */
	public static byte caesarFindKey(float[] charFrequencies) {
		assert (charFrequencies.length == 256);
		double dotProductTemp = 0;
		// initialized to -in so "is greatest" comparison will return false, for the first iteration.
		double maxDotProductTemp = Double.NEGATIVE_INFINITY;
		int indexMax = -1;

		for (int index = 0; index < charFrequencies.length; index++) {

			// Compute the dot product
			for (int j = 0; j < ENGLISHFREQUENCIES.length; j++) {
				// The module handles the warp-around (if the index is greater than the size of charFrequency, then we come back to the beginning)
				dotProductTemp += ENGLISHFREQUENCIES[j] * charFrequencies[(index + j) % charFrequencies.length];
			}
			if (dotProductTemp > maxDotProductTemp) {
				maxDotProductTemp = dotProductTemp;
				indexMax = index;
			}
			dotProductTemp = 0;
		}
		// APOSITION is a constant holding the a position in the byte alphabet, 225
		return (byte) (indexMax - APOSITION) ; 
	}
	
	
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to decode a byte array encoded using a XOR 
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return the array of possibilities for the clear text
	 */
	public static byte[][] xorBruteForce(byte[] cipher) {
		assert (cipher.length > 0);

		// We are going to use the following array to stock xor encryption
		// with the ciphered text and the POTENTIAL key
		byte[] potential;

		// declaring a 2D array that will stock in each line, the potential array
		// will contain 256 lines and the number of columns depends on the length of
		// cipher array
		byte[][] bruteForcePossibilities = new byte[ALPHABETSIZE][cipher.length];
		for (byte key = -128; key < 127; ++key) {
			potential = Encrypt.xor(cipher, key);
			bruteForcePossibilities[key + 128] = potential;

		}

		return bruteForcePossibilities;
	}
	
	
	
	//-----------------------Vigenere-------------------------
	// Algorithm : see  https://www.youtube.com/watch?v=LaWp_Kq0cKs	
	/**
	 * Method to decode a byte array encoded following the Vigenere pattern, but in a clever way, 
	 * saving up on large amounts of computations
	 * @param cipher the byte array representing the encoded text
	 * @return the byte encoding of the clear text
	 */
	public static byte[] vigenereWithFrequencies(byte[] cipher) {
		//TODO : COMPLETE THIS METHOD
		return null; //TODO: to be modified
	}
	
	
	
	/**
	 * Helper Method used to remove the space character in a byte array for the clever Vigenere decoding
	 * @param array the array to clean
	 * @return a List of bytes without spaces
	 */
	public static List<Byte> removeSpaces(byte[] array){
		//TODO : COMPLETE THIS METHOD
		return null;
	}
	
	
	/**
	 * Method that computes the key length for a Vigenere cipher text.
	 * @param cipher the byte array representing the encoded text without space
	 * @return the length of the key
	 */
	public static int vigenereFindKeyLength(List<Byte> cipher) {
		//TODO : COMPLETE THIS METHOD
		return -1; //TODO: to be modified
	}

	
	
	/**
	 * Takes the cipher without space, and the key length, and uses the dot product with the English language frequencies 
	 * to compute the shifting for each letter of the key
	 * @param cipher the byte array representing the encoded text without space
	 * @param keyLength the length of the key we want to find
	 * @return the inverse key to decode the Vigenere cipher text
	 */
	public static byte[] vigenereFindKey(List<Byte> cipher, int keyLength) {
		//TODO : COMPLETE THIS METHOD
		return null; //TODO: to be modified
	}
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method used to decode a String encoded following the CBC pattern
	 * @param cipher the byte array representing the encoded text
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return the clear text
	 */
	public static byte[] decryptCBC(byte[] cipher, byte[] iv) {
		return Encrypt.cbcInternal(cipher, iv, true);
	}
	
	
	

		
		
		
		
		
}
