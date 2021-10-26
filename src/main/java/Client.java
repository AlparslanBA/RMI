import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, FileNotFoundException, NoSuchAlgorithmException {
        ServiceClass serviceClass = (ServiceClass) Naming.lookup("rmi://localhost:6969/communication");
        System.out.println("--- " + serviceClass.echo("server" + "  " + serviceClass.getClass().getName()));
        serviceClass.restart();
        System.out.println(serviceClass.status("printer1") + " 1");
        serviceClass.start();
        serviceClass.print("alp.png", "printer1");
        serviceClass.print("alp2.png", "printer1");
        System.out.println(serviceClass.status("printer1") + " 2");
        System.out.println(serviceClass.queue("printer1"));
        serviceClass.topQueue("printer1", 1);
        System.out.println(serviceClass.queue("printer1"));

        MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = msgDigest.digest(
                "password".getBytes(StandardCharsets.UTF_8)
        );
        String pswHash = bytesToHex(encodedHash);

        System.out.println(serviceClass.login("Test von Test", pswHash));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
