import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Scanner;

public class Server {
    File myObj = new File("publicFile.txt");
    Date now = new Date();
    public static void main(String[] args) throws RemoteException, FileNotFoundException, InterruptedException {
        Registry registry = LocateRegistry.createRegistry(9001);
        registry.rebind("communication", new Servant());

        Server server = new Server();

        //server.FileWriter("Alens got a big head");
        //server.FileWriter("Alen small pepe");
        //server.FileReader();

        //FileReader(new File("accessControllPolicy.txt")) ;

       // checkRole("David", "print");

        String token = server.createJWT("test");
        Thread.sleep(1);
        server.decodeJWT(token);
    }

    static private boolean checkRole(String username, String operations) throws FileNotFoundException {
        Scanner myReader = new Scanner("accessControllPolicy.txt");
        boolean userfound = false;
        System.out.println(username + "   "  + operations);
        System.out.println(myReader);

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();

            System.out.println(myReader);
            System.out.println("   h----");
           // System.out.println(myReader.nextLine());
            System.out.println("   h---");
            System.out.println(data);


            if(data.contains(username)) {
                System.out.println("It works");
                System.out.println(operations);
                return data.contains(operations);
            }
        }
        return false;
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

    static private void FileReader(File file) throws FileNotFoundException {
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            System.out.println(data);
        }
        myReader.close();
    }

    private String createJWT(String id) {
        String token = "";
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withSubject(id)
                    .withExpiresAt(new Date(now.getTime() + 10000))
                    .withIssuer("localhost")
                    .sign(algorithm);
            System.out.println("Token is genetrated");
        } catch (JWTCreationException exception){
            System.out.println("Cant create JWT");
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return token;
    }

    private void decodeJWT(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("localhost")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("Token is Valid");
        } catch (JWTVerificationException exception){
            System.out.println("Invalid JWT");
        }
    }
}
