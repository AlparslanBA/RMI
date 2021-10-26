import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Server {
    File myObj = new File("publicFile.txt");

    public static void main(String[] args) throws RemoteException, FileNotFoundException {
        Registry registry = LocateRegistry.createRegistry(6969);
        registry.rebind("communication", new Servant());

        Server server = new Server();

        server.FileWriter("Alens got a big head");
        server.FileWriter("Alen small pepe");
        server.FileReader();
    }

    private void FileWriter(String input){
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(myObj, true);
            myWriter.write("\n"+input);
            myWriter.close();
            System.out.println("File has been updated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void FileReader() throws FileNotFoundException {
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            System.out.println(data);
        }
        myReader.close();
    }
}
