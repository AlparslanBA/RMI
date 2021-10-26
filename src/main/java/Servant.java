import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.*;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;


public class Servant extends UnicastRemoteObject implements ServiceClass {

    public Servant() throws RemoteException {
    }

    private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";

    LinkedList<String> printer1 = new LinkedList<String>();
    LinkedList<String> printer2 = new LinkedList<String>();
    Boolean isRunning = false;
    String configParameter = "";
    ArrayList<LinkedList<String>> listOfPrinters = new ArrayList<LinkedList<String>>();

    @Override
    public String echo(String input) throws RemoteException {
        return "From server: " + input;
    }

    @Override
    public void print(String fileName, String printer) {
        if (printer.equals("printer1")) {
            listOfPrinters.get(0).add(fileName);
        } else if (printer.equals("printer2")) {
            listOfPrinters.get(1).add(fileName);
        }
    }

    @Override
    public String queue(String printer) {
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
    public void topQueue(String printer, int job) {
        if (printer.equals("printer1")) {
            String file = listOfPrinters.get(0).remove(job);
            listOfPrinters.get(0).addFirst(file);
        } else if (printer.equals("printer2")) {
            String file = listOfPrinters.get(1).remove(job);
            listOfPrinters.get(1).addFirst(file);
        }
    }

    @Override
    public void start() throws RemoteException {
        if (listOfPrinters.isEmpty()) {
            listOfPrinters.add(printer1);
            listOfPrinters.add(printer2);
        }
        isRunning = true;
    }

    @Override
    public void stop() throws RemoteException {
        isRunning = false;
    }

    @Override
    public void restart() throws RemoteException {
        stop();
        for (int i = 0; i < listOfPrinters.size(); i++) {
            listOfPrinters.get(i).clear();
        }
        listOfPrinters.clear();
        start();
    }

    @Override
    public String status(String printer) {
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
    public String readConfig(String parameter) {
        return configParameter;
    }

    @Override
    public void setConfig(String parameter, String value) {
        configParameter = value;
    }







}


