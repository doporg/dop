import com.clsaa.dop.server.gateway.oauth.security.ClientKeysUtil;

public class GeneralClient {
    public static void main(String[] args) {
        String client_aes_key = "e5Fcq-fWX-EvE5jyM7Scsb9PUXYEC22NE0SgF9dp-3sVvLxW-_59sOJ4I5tFqgKz";
        String client_id = ClientKeysUtil.newClientId("softeng_dop_web");
        String client_secret = ClientKeysUtil.newPlainClientSecret();
        String client_secret_in_db = ClientKeysUtil.getAesChipperSecret(client_secret, client_aes_key);
        System.out.println("client_aes_key"+" : "+client_aes_key);
        System.out.println("client_id"+" : "+client_id);
        System.out.println("client_secret"+" : "+client_secret);
        System.out.println("client_secret_in_db"+" : "+client_secret_in_db);
    }
}
