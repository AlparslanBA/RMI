import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Servant extends UnicastRemoteObject implements IServiceClass {
    File publicFile = new File("publicFile.txt");
    File policyFile = new File("policy.txt");
    LinkedList<String> printer1 = new LinkedList<String>();
    LinkedList<String> printer2 = new LinkedList<String>();
    Boolean isRunning = false;
    String configParameter = "";
    ArrayList<LinkedList<String>> listOfPrinters = new ArrayList<LinkedList<String>>();
    
    public Servant() throws RemoteException {
        publicFile.setReadOnly();
        policyFile.setReadOnly();
    }


    @Override
    public String echo(String input, String token) throws RemoteException {
        return "From server: " + input;
    }

    @Override
    public String print(String fileName, String printer, String token) {
        int roleId = GetRoleFromToken(token);
        if (roleId == 0 || roleId == 2 ||  roleId == 3) {
            if (printer.equals("printer1")) {
                listOfPrinters.get(0).add(fileName);
            } else if (printer.equals("printer2")) {
                listOfPrinters.get(1).add(fileName);
            }
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }

    @Override
    public String queue(String printer, String token) {
        int roleId = GetRoleFromToken(token);
        if (roleId == 0 || roleId == 2 ||  roleId == 3) {
        String queue = "";
        if (printer.equals("printer1")) {
            for (int i = 0; i < listOfPrinters.get(0).size(); i++) {
                queue += "<job number " + i + "> \t" + "<file name " + listOfPrinters.get(0).get(i) + ">  \n";
            }
            return queue;
        } else if (printer.equals("printer2")) {
            for (int i = 0; i < listOfPrinters.get(1).size(); i++) {
                queue += "<job number " + i + "> \t" + "<file name " + listOfPrinters.get(1).get(i) + ">  \n";
            }
            return queue;
        }
        return queue;
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }

    @Override
    public String topQueue(String printer, int job, String token) {
        int roleId = GetRoleFromToken(token);
        if (roleId == 0 || roleId == 2) {
            if (printer.equals("printer1")) {
                String file = listOfPrinters.get(0).remove(job);
                listOfPrinters.get(0).addFirst(file);
            } else if (printer.equals("printer2")) {
                String file = listOfPrinters.get(1).remove(job);
                listOfPrinters.get(1).addFirst(file);
            }
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }

    @Override
    public String start(String token) throws RemoteException {
        int roleId = GetRoleFromToken(token);
        if (roleId == 0 || roleId == 1) {
            if (listOfPrinters.isEmpty()) {
                listOfPrinters.add(printer1);
                listOfPrinters.add(printer2);
            }
            isRunning = true;
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }

    @Override
    public String stop(String token) throws RemoteException {
        int roleId = GetRoleFromToken(token);
        if (roleId == 1) {
            isRunning = false;
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }

    @Override
    public String restart(String token) throws RemoteException {
        int roleId = GetRoleFromToken(token);
        if (roleId == 0 || roleId == 1 || roleId == 2) {
            stop(token);
            for (int i = 0; i < listOfPrinters.size(); i++) {
                listOfPrinters.get(i).clear();
            }
            listOfPrinters.clear();
            start(token);
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }

    @Override
    public String status(String printer, String token) {
        int roleId = GetRoleFromToken(token);
        if (roleId == 0 || roleId == 1) {
            if (!isRunning) {
                return "Server is not running";
            }
            if (listOfPrinters.isEmpty()) {
                return "Server is not started";
            }
            if (printer.equals("printer1")) {
                if (listOfPrinters.get(0).isEmpty()) {
                    return "waiting";
                } else {
                    return "printing";
                }
            } else if (printer.equals("printer2")) {
                if (listOfPrinters.get(1).isEmpty()) {
                    return "waiting";
                } else {
                    return "printing";
                }
            }
            return "";
        }
        System.out.println("No permission");
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }

    @Override
    public String readConfig(String parameter, String token) {
        int roleId = GetRoleFromToken(token);
        if (roleId == 0 || roleId == 1) {
            return configParameter;
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }

    @Override
    public String setConfig(String parameter, String value, String token) {
        if (GetRoleFromToken(token) == 1) {
            configParameter = value;
            return "Parameter: " + parameter + "changed to: " + value;
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }

    @Override
    public String login(String username, String password) throws RemoteException, FileNotFoundException, NoSuchAlgorithmException {
        if (ReadFromPublicFile(username, EncryptPassword(password))){
            int roleId = ReadFromRoleFile(username);
            String token = createJWT(username, roleId);
            return token;
        }

        return "Username or password incorrect";
    }

    @Override
    public String deleteUser(String username, String token) throws IOException {
        if (GetRoleFromToken(token) == 0) {
            policyFile.setWritable(true);
            Scanner reader = new Scanner(policyFile);
            BufferedWriter bw = new BufferedWriter(new FileWriter(policyFile));
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                System.out.println(data);
                if(!data.contains(username)) {
                    bw.write(data+System.getProperty("line.separator"));
                }
            }
            policyFile.setWritable(false);

            publicFile.setWritable(true);
            reader = new Scanner(publicFile);
            bw = new BufferedWriter(new FileWriter(publicFile));
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if(!data.contains(username)) {
                    bw.write(data+System.getProperty("line.separator"));
                }
            }
            publicFile.setWritable(false);

            bw.close();
            reader.close();
            return username  + " has been deleted";
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }
    @Override
    public String addUser(String username, String password,String role, String token) throws IOException, NoSuchAlgorithmException {
        if (GetRoleFromToken(token) == 0) {
            publicFile.setWritable(true);
            FileWriter fw = new FileWriter(publicFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(username + ";" + EncryptPassword(password));
            bw.newLine();
            bw.close();
            publicFile.setWritable(false);


            policyFile.setWritable(true);
            fw = new FileWriter(policyFile, true);
            bw = new BufferedWriter(fw);

            bw.write(username + ";" + role);
            bw.newLine();
            bw.close();
            policyFile.setWritable(false);
            return username + " with role: " + role + " has been added";
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();
    }
    @Override
    public String updateUserRole(String username, String role, String token) throws IOException {
        if (GetRoleFromToken(token) == 0) {
            File tempFile = new File("myTempFile.txt");
            Scanner reader = new Scanner(policyFile);
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                System.out.println(data);
                if(data.contains(username)) {
                    bw.write(data.split(";")[0] + ";" + role + System.getProperty("line.separator"));
                }
                else{
                    bw.write(data+System.getProperty("line.separator"));
                }
            }
            bw.close();
            reader.close();
            //policyFile.setWritable(false);
            policyFile.delete();
            tempFile.renameTo(policyFile);
            policyFile = tempFile;
            return username + " updated to have role: " + role;
        }
        return "You do not have permission to " + new Object(){}.getClass().getEnclosingMethod().getName();

    }

    private Boolean ReadFromPublicFile(String username, String password) throws FileNotFoundException {
        Scanner myReader = new Scanner(publicFile);

        Boolean loggedIn = false;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();

            if (username.equals(data.split(";")[0]) && password.equals(data.split(";")[1])){
                loggedIn = true;
                break;
            }
        }
        myReader.close();
        return loggedIn;
    }
    private int ReadFromRoleFile(String username) throws FileNotFoundException {
        Scanner myReader = new Scanner(policyFile);

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();

            if (username.equals(data.split(";")[0])){
                myReader.close();
                return Integer.parseInt(data.split(";")[1]);
            }
        }
        myReader.close();
        return 9999;
    }


    private String EncryptPassword(String password) throws NoSuchAlgorithmException {
     MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = msgDigest.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );
        return bytesToHex(encodedHash);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String createJWT(String id, int roleId) {
        String token = "";
        try {
            Date now = new Date();
            Algorithm algorithm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withSubject(id)
                    .withClaim("roleId", roleId)
                    .withExpiresAt(new Date(now.getTime() + 10000000))
                    .withIssuer("localhost")
                    .sign(algorithm);
            System.out.println("Token is genetrated");
        } catch (JWTCreationException exception){
            System.out.println("Cant create JWT");
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return token;
    }

    private int GetRoleFromToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("localhost")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            jwt.getPayload();

            return Integer.parseInt(jwt.getClaim("roleId").toString());

        } catch (JWTVerificationException exception){
            return 9999;
        }
    }
}
