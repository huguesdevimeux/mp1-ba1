package crypto;

import java.util.List;

public class Decrypt {
	
	
	public static final int ALPHABETSIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1 ; //256
	public static final int APOSITION = 97 + ALPHABETSIZE/2; 
	
	//source : https://en.wikipedia.org/wiki/Letter_frequency
	public static final double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};
	
	public static void main (String[] args) {
		
	byte[] name = {91, 32, 69, 83, 92, 70};
	
	byte key = 50;
	
	byte[][] is = xorBruteForce(name);
	for(int i = 0; i < is.length; ++i) {
		for(int j = 0; j < is[i].length; ++j) {
			
		
		System.out.print(is[i][j] + " ");
	}
	}
	}
	
	
	/**
	 * Method to break a string encoded with different types of cryptosystems
	 * @param type the integer representing the method to break : 0 = Caesar, 1 = Vigenere, 2 = XOR
	 * @return the decoded string or the original encoded message if type is not in the list above.
	 */
	public static String breakCipher(String cipher, int type) {
		//TODO : COMPLETE THIS METHOD
		
		return null; //TODO: to be modified
	}
	
	
	/**
	 * Converts a 2D byte array to a String
	 * @param bruteForceResult a 2D byte array containing the result of a brute force method
	 */
	public static String arrayToString(byte[][] bruteForceResult) {

		//initalising 2 strings with "" 
		//result will be the final String, combining the strings of all the arrays
		
		//decoded will be the string of every array in the 2D array
		//result will combine every "decoded" array
		String result = "";
		String decoded = "";
		
		for (int i = 0; i < bruteForceResult.length; ++i) {
			
			decoded = Helper.bytesToString(bruteForceResult[i]);
			result += decoded + System.lineSeparator();
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
		byte[][] bruteForcePossibilities = new byte[256][cipher.length];

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
		//TODO : COMPLETE THIS METHOD
		return null; //TODO: to be modified
	}
	
	
	/**
	 * Method that finds the key used by a  Caesar encoding from an array of character frequencies
	 * @param charFrequencies the array of character frequencies
	 * @return the key
	 */
	public static byte caesarFindKey(float[] charFrequencies) {
		//TODO : COMPLETE THIS METHOD
		return -1; //TODO: to be modified
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
		byte[][] bruteForcePossibilities = new byte[256][cipher.length];
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
