import java.io.FileNotFoundException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, FileNotFoundException, NoSuchAlgorithmException {
        IServiceClass aliceClient = (IServiceClass) Naming.lookup("rmi://localhost:9001/communication");

        String token = aliceClient.login("Alice", "password");
        System.out.println(token);
        System.out.println(aliceClient.queue("",token));
        System.out.println(aliceClient.topQueue("",1,token));
        System.out.println(aliceClient.setConfig("","",token));
        System.out.println(aliceClient.readConfig("",token));
        System.out.println(aliceClient.start(token));
        System.out.println(aliceClient.status("",token));
        System.out.println(aliceClient.restart(token));
        System.out.println(aliceClient.stop(token));
    }
}
