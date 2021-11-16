import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;

public class Server {
    File myObj = new File("publicFile.txt");
    Date now = new Date();
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(9001);
        registry.rebind("communication", new Servant());

        Server server = new Server();

        /*
        String token = server.createJWT("test", 0);
        Thread.sleep(5000);
        server.decodeJWT(token);
        */

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
