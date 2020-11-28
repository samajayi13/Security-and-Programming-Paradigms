import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Arrays;

public class EncryptionHelper {
    private static  int idCount = 0;
    private Cipher cipher;
    private KeyPair pair;
    public int id;

    public EncryptionHelper() {
        // increments static property to keep track of how many instance of the class has been created.
        idCount++;
        // each time a new instance has been created a id is given to the instance
        // this helps identify which encryption key is to be used to decrypt the user's numbers
        this.id = idCount;
    }

    /**
     * takes the user's 6 numbers and turns it into cipher text
     * @param data is the user's 6 numbers being encrypted
     * @return a 256 length byte array representation of the cipher text
     */
    public byte[] encryptData(String data)  {
       try {
           KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
           pair = keyPairGen.generateKeyPair();
           PublicKey publicKey = pair.getPublic();
           cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
           cipher.init(Cipher.ENCRYPT_MODE, publicKey);
           cipher.update(data.getBytes());
           return cipher.doFinal();
       } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
       return null;
    }

    /**
     * passes path object of the filename argument and calls readAllBytes function.
     * @param filename is the name of the file the encrypted user numbers will be stored in.
     * @return a byte array representation of the all of the cipher text contained in the file or null if an exception occurs
     */
    public byte[] bytesFileReader(String filename){
        try {
            return Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * writes the byte array representation of the cipher text into the file.
     * @param filename is the filename to be written to.
     * @param data is the byte array representation of the cipher text.
     */
    public void bytesFileWriter(String filename, byte[] data){
        try {
            // setting append to true allows program not to overwrite existing files
            FileOutputStream os = new FileOutputStream(filename,true);
            os.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * decrypts the cipher text and turns it into plaintext
     * @param data is the byte array representation of the cipher text to be decrypted.
     * @return the plaintext representation of the cipher text that was decrypted. If exception is thrown it returns null
     */
    public String decryptData(byte[] data) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
            byte[] decipheredText = cipher.doFinal(data);
            return new String(decipheredText, StandardCharsets.UTF_8);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }


}
