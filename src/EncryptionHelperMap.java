import java.util.HashMap;
import java.util.List;

public class EncryptionHelperMap {
    private HashMap<String,List<EncryptionHelper>> encryptionHashMap = new HashMap<>();

    public void createNewKeyPair (String username,List<EncryptionHelper> encryptionHelperList ) {
        this.encryptionHashMap.put(username,encryptionHelperList);
    }

    public void updateKeyPair(String username,List<EncryptionHelper> encryptionHelperList){
        this.encryptionHashMap.replace(username,encryptionHelperList);
    }

    public HashMap<String, List<EncryptionHelper>> getEncryptionHashMap() {
        return encryptionHashMap;
    }

    public List<EncryptionHelper> getUsersEncryptionHelperList(String username){
        return this.encryptionHashMap.get(username);
    }
}
