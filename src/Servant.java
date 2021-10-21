import java.io.Console;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class Servant extends UnicastRemoteObject implements ServiceClass {

    public Servant() throws RemoteException {
    }
    LinkedList<String> list = new LinkedList<String>();


    @Override
    public String echo(String input) throws RemoteException {
        return "From server: "+ input;
    }

    @Override
    public void print(String fileName, String printer) {
        list.add(fileName);
    }

    @Override
    public void queue(String printer) {
        for (int i = 0; i < list.size(); i++){
            System.out.println("<job number "+ i + "> \t" + "<file name " + list.get(i) + "> ");
        }
    }

    @Override
    public void topQueue(String printer, int job) {
        String file = list.remove(job);
        list.addFirst(file);
    }

    @Override
    public void start() throws RemoteException {
        Server.main(null);
    }

    @Override
    public void stop() throws RemoteException {
        System.exit(1);
    }

    @Override
    public void restart() throws RemoteException {
        stop();
        System.out.flush();
        start();
    }

    @Override
    public void status(String printer) {

    }

    @Override
    public void readConfig(String parameter) {
        System.out.println(parameter);
    }

    @Override
    public void setConfig(String parameter, String value) {
        value = parameter;
    }
}
