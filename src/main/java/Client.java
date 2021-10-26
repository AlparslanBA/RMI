import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
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
    }

}
