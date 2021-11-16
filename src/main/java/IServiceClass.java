import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public interface IServiceClass extends Remote {

    public void Testing() throws IOException;
    public String echo(String input, String token) throws RemoteException;
    public String print(String fileName, String printer, String token) throws RemoteException;
    public String queue(String printer, String token) throws RemoteException;
    public String topQueue(String printer, int job, String token) throws RemoteException;
    public String start(String token) throws RemoteException;
    public String stop(String token) throws RemoteException;
    public String restart(String token) throws RemoteException;
    public String status(String printer, String token) throws RemoteException;
    public String readConfig(String parameter, String token) throws RemoteException;
    public String setConfig(String parameter, String value, String token) throws RemoteException;
    public String login(String username, String password) throws IOException, NoSuchAlgorithmException;
}
