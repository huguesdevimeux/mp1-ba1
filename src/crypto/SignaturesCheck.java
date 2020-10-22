package crypto;

import java.util.ArrayList;
import java.util.List;

public final class SignaturesCheck {
	
	/** =========================
	 *  !!!! DO NOT MODIFY !!!!
	 *  =========================
	 *  
	 *  Any submission for which this file does not compile will not be accepted
	 *  
	 */

    @SuppressWarnings("unused")
    public static final void check() {

		byte[] plainText = new byte[10];
		byte key = 1;
		boolean spaceEncoding = true;
		byte[] keyword = new byte[4];
		byte[] pad = new byte[8];
		byte[] iv = new byte[8];
		
		byte[] cipherText = null;
		String encodedMessage = null;
		byte[][] bruteForceResult = new byte[1][2];
	
		float[] frequencies = null;
		int keyLength = 0;
		List<Byte> cipherArray = new ArrayList<Byte>();
		 
		encodedMessage = Encrypt.encrypt(new String("hello friend"), new String("a"), 1);
		cipherText = Encrypt.caesar(plainText, key, spaceEncoding); 
		cipherText = Encrypt.caesar(plainText, key);
		cipherText = Encrypt.xor(plainText, key, spaceEncoding);
		cipherText = Encrypt.xor(plainText, key);
		cipherText = Encrypt.vigenere(plainText, keyword, spaceEncoding);
		cipherText = Encrypt.vigenere(plainText, keyword);
		cipherText = Encrypt.oneTimePad(plainText, pad);
		cipherText = Encrypt.cbc(plainText, iv);
		cipherText = Encrypt.generatePad(8);
	    
		String message = null;
		message = Decrypt.breakCipher(encodedMessage, 1);
		message = Decrypt.arrayToString(bruteForceResult);
		
		bruteForceResult = Decrypt.caesarBruteForce(cipherText);
		key = Decrypt.caesarWithFrequencies(cipherText);	
		frequencies = Decrypt.computeFrequencies(cipherText);
		key = Decrypt.caesarFindKey(frequencies);
	
		bruteForceResult = Decrypt.xorBruteForce(cipherText);
		
		cipherText = Decrypt.vigenereWithFrequencies(cipherText);
		keyLength = Decrypt.vigenereFindKeyLength(cipherArray);
		cipherText = Decrypt.vigenereFindKey(cipherArray, keyLength);
		
		cipherArray = Decrypt.removeSpaces(cipherText);
	
		cipherText = Decrypt.decryptCBC(cipherText, iv);

    }
}
