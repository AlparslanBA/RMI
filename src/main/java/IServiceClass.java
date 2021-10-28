import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public interface IServiceClass extends Remote {

    public String echo(String input, String token) throws RemoteException;
    public void print(String fileName, String printer, String token) throws RemoteException;
    public String queue(String printer, String token) throws RemoteException;
    public void topQueue(String printer, int job, String token) throws RemoteException;
    public void start(String token) throws RemoteException;
    public void stop(String token) throws RemoteException;
    public void restart(String token) throws RemoteException;
    public String status(String printer, String token) throws RemoteException;
    public String readConfig(String parameter, String token) throws RemoteException;
    public void setConfig(String parameter, String value, String token) throws RemoteException;
    public String login(String username, String password) throws RemoteException, FileNotFoundException, NoSuchAlgorithmException;
}
