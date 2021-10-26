import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(6969);
        registry.rebind("communication", new main.java.Servant());
    }
}
