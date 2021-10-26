import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceClass extends Remote {

    public String echo(String input) throws RemoteException;
    public void print(String fileName, String printer) throws RemoteException;
    public String queue(String printer) throws RemoteException;
    public void topQueue(String printer, int job) throws RemoteException;
    public void start() throws RemoteException;
    public void stop() throws RemoteException;
    public void restart() throws RemoteException;
    public String status(String printer) throws RemoteException;
    public String readConfig(String parameter) throws RemoteException;
    public void setConfig(String parameter, String value) throws RemoteException;
    public String login(String username, String password) throws RemoteException, FileNotFoundException;
}
