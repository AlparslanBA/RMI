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
        IServiceClass serviceClass = (IServiceClass) Naming.lookup("rmi://localhost:9001/communication");
        System.out.println("--- " + serviceClass.echo("server" + "  " + serviceClass.getClass().getName(),"token"));
        serviceClass.restart("token");
        System.out.println(serviceClass.status("printer1","token") + " 1");
        serviceClass.start("token");
        serviceClass.print("exam1.png", "printer1", "token");
        serviceClass.print("exam2.png", "printer1", "token");
        System.out.println(serviceClass.status("printer1", "token") + " 2");
        System.out.println(serviceClass.queue("printer1", "token"));
        serviceClass.topQueue("printer1", 1,"token");
        System.out.println(serviceClass.queue("printer1", "token"));

        System.out.println(serviceClass.login("Test von Test", "password"));
    }
}
