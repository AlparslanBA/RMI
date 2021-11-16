import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Servant extends UnicastRemoteObject implements IServiceClass {
    File file = new File("publicFile.txt");
    File passwordFile = new File("accessControllPolicy.txt");
    LinkedList<String> printer1 = new LinkedList<String>();
    LinkedList<String> printer2 = new LinkedList<String>();
    Boolean isRunning = false;
    String configParameter = "";
    ArrayList<LinkedList<String>> listOfPrinters = new ArrayList<LinkedList<String>>();
    
    public Servant() throws RemoteException {
        file.setReadOnly();
    }


    @Override
    public String echo(String input, String token) throws RemoteException {
        return "From server: " + input;
    }

    @Override
    public void print(String fileName, String printer, String token) {
        if (printer.equals("printer1")) {
            listOfPrinters.get(0).add(fileName);
        } else if (printer.equals("printer2")) {
            listOfPrinters.get(1).add(fileName);
        }
    }

    @Override
    public String queue(String printer, String token) {
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

    @Override
    public void topQueue(String printer, int job, String token) {
        if (printer.equals("printer1")) {
            String file = listOfPrinters.get(0).remove(job);
            listOfPrinters.get(0).addFirst(file);
        } else if (printer.equals("printer2")) {
            String file = listOfPrinters.get(1).remove(job);
            listOfPrinters.get(1).addFirst(file);
        }
    }

    @Override
    public void start(String token) throws RemoteException {
        if (listOfPrinters.isEmpty()) {
            listOfPrinters.add(printer1);
            listOfPrinters.add(printer2);
        }
        isRunning = true;
    }

    @Override
    public void stop(String token) throws RemoteException {
        isRunning = false;
    }

    @Override
    public void restart(String token) throws RemoteException {
        stop(token);
        for (int i = 0; i < listOfPrinters.size(); i++) {
            listOfPrinters.get(i).clear();
        }
        listOfPrinters.clear();
        start(token);
    }

    @Override
    public String status(String printer, String token) {
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

    @Override
    public String readConfig(String parameter, String token) {
        return configParameter;
    }

    @Override
    public void setConfig(String parameter, String value, String token) {
        configParameter = value;
    }

    @Override
    public String login(String username, String password) throws IOException, NoSuchAlgorithmException {
        if (ReadFromPublicFile(username, EncryptPassword(password))){
            System.out.println(username +  " : Works ");
            System.out.println(checkRole(username, "print"));
            System.out.println(username + "  :  Nooo ");
            System.out.println(checkRole(username, "status"));
            System.out.println("delete:" + deleteUser("George"));
            addUser("Henry", new String[]{"print, queue"});
            return "Successfully logged in";
        }

        return "Username or password incorrect";
    }

    private boolean checkRole(String username, String operations) throws FileNotFoundException {
        Scanner myReader = new Scanner(passwordFile);
        boolean userfound = false;
     //   System.out.println(username + "   "  + operations);

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if(data.contains(username)) {
                if(data.contains(operations)) {
                    userfound = true;
                } else {
                    userfound = false;
                }
                break;
            }
        }
        return userfound;
    }

    private boolean deleteUser(String username) throws IOException {
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(passwordFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.contains(username)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        boolean successful = tempFile.renameTo(passwordFile);
        return successful;
    }

    private void addUser(String username, String[] pernmissions) throws IOException {

            FileWriter fw = new FileWriter(passwordFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            String perms = "";
        for (String role: pernmissions) {
           perms = perms.concat(role+", ");

        }
            bw.write(username+": " + perms);
            bw.newLine();
            bw.close();
    }

    private Boolean ReadFromPublicFile(String username, String password) throws FileNotFoundException {
        Scanner myReader = new Scanner(file);

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
    
    private String EncryptPassword(String password) throws NoSuchAlgorithmException {
     MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = msgDigest.digest(
                "password".getBytes(StandardCharsets.UTF_8)
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
}
