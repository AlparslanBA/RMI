import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        ServiceClass serviceClass = (ServiceClass) Naming.lookup("rmi://localhost:6969/communication");
        System.out.println("--- " + serviceClass.echo("hey server" + "  " + serviceClass.getClass().getName()));
        //System.out.println("--- " + serviceClass.readConfig());
        //System.out.println("--- " + serviceClass.print("testNavn", serviceClass.getClass().getName()));
    }
}
