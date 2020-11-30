import java.util.HashMap;
import java.util.List;

public class EncryptionHelperMap {
    private HashMap<String,List<EncryptionHelper>> encryptionHashMap = new HashMap<>();

    public EncryptionHelperMap() {
    }

    /**
     * creates a new key value pair with the username being the key and the list of encryption objects being the value
     * @param username the value stored in the username session attribute
     * @param encryptionHelperList the list of encryption objects which contain keys to decrypt the user lottery draws numbers
     */
    public void createNewKeyPair (String username, List<EncryptionHelper> encryptionHelperList ) {
        this.encryptionHashMap.put(username,encryptionHelperList);
    }

    /**
     * updates the replaces with encryption object list in the key value pair that has the username as the key
     * @param username the value stored in the username session attribute
     * @param encryptionHelperList the list of encryption objects which contain keys to decrypt the user lottery draws numbers
     */
    public void updateKeyPair(String username,List<EncryptionHelper> encryptionHelperList){
        this.encryptionHashMap.replace(username,encryptionHelperList);
    }

    /**
     * @return the hash map that contains all key value pair objects for the application users
     */
    public HashMap<String, List<EncryptionHelper>> getEncryptionHashMap() {
        return encryptionHashMap;
    }

}
