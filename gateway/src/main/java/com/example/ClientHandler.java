package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;

import com.example.Service.IJeu;

public class ClientHandler implements Runnable {
    private String IDClient;
    private PrintWriter out;
    private BufferedReader bf;
    private String typeConnexion;
    private Socket sc;
    private String Histo = "";

    public ClientHandler(Socket socket, String id) throws IOException {
        sc = socket;
        this.IDClient = id;
        bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    List<String> Game = new ArrayList<>();

    @Override
    public void run() {
        try {
            int i = 0;
            while (true) {
                String str = bf.readLine();

                if (str != null && typeConnexion == null) {
                    typeConnexion = str;
                    System.out.println(str);
                } else if (str != null && typeConnexion != null) {

                    if (typeConnexion.contains("RMI")) {
                        IJeu stub = (IJeu) Naming.lookup("rmi://localhost:1099/Games/PPC");

                        new Thread(() -> {
                            try {
                                if (str.contains("Historique")) {
                                    String res = (String) HistoLoad(stub, IDClient);
                                    if (res != null) {
                                        out.println(res);
                                        out.flush();

                                    }
                                } else {
                                    List<String> res = (List<String>) RMI(stub, IDClient, str);
                                    if (res != null) {
                                        out.println(res.get(0));
                                        out.flush();
                                        out.println(res.get(1));
                                        out.flush();
                                    }
                                }

                            } catch (MalformedURLException | NotBoundException | RemoteException e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }
                        }).start();
                        if (str.contains("Pierre") || str.contains("Ciseaux") || str.contains("Papier")) {
                            i++;
                        }
                        if (i > 2) {
                            String res = RMIResult(stub, IDClient);
                            out.println("Final");
                            out.flush();
                            out.println(res);
                            out.flush();
                            i = 1;
                        }
                    } else if (typeConnexion.contains("RPC")) {

                        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
                        config.setEnabledForExtensions(true);
                        config.setServerURL(new URL("http://localhost:8081/xmlrpc"));
                        XmlRpcClient client = new XmlRpcClient();
                        client.setConfig(config);

                        new Thread(() -> {
                            try {
                                if (str.contains("Historique")) {

                                    if (Histo != null) {
                                        if (Histo.length() == 0) {
                                            Histo = "Aucun historique disponible pour ce client.";
                                        } else {
                                            Histo = "Votre historique des rounds :\n".concat(Histo);
                                        }
                                        out.println(Histo);
                                        out.flush();

                                    }
                                } else {
                                    Object result = client.execute("Jeu.playRound", new Object[] { IDClient, str });
                                    if (result instanceof Object[]) {
                                        Object[] arrayResult = (Object[]) result;
                                        List<String> res = Arrays
                                                .asList(Arrays.copyOf(arrayResult, arrayResult.length, String[].class));
                                        Game.add(res.get(1));
                                        Histo = Histo + "Round : Votre choix - " + str + ", Choix du serveur - "
                                                + res.get(0) + ", Résultat - " + res.get(1) + "\n";
                                        out.println(res.get(0));
                                        out.flush();
                                        out.println(res.get(1));
                                        out.flush();
                                    }
                                }

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }
                        }).start();
                        if (str.contains("Pierre") || str.contains("Ciseaux") || str.contains("Papier")) {
                            i++;
                        }
                        if (i > 2) {

                            out.println("Final");
                            out.flush();
                            int clientScore = Collections.frequency(Game, "Gagné");
                            int serverScore = Collections.frequency(Game, "Perdu");
                            String gameResult;
                            out = new PrintWriter(sc.getOutputStream(), true);
                            if (clientScore == serverScore) {
                                gameResult = "Le jeu s'est terminé en égalité avec un score de " + clientScore;
                            } else if (clientScore > serverScore) {
                                gameResult = "Félicitations! Vous avez gagné le jeu avec un score de " + clientScore;
                            } else {
                                gameResult = "Désolé, vous avez perdu le jeu avec un score de " + clientScore;
                            }
                            out.println(gameResult);
                            out.flush();
                            Game.clear();
                            i = 1;
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> RMI(IJeu stub, String clientId, String choix)
            throws MalformedURLException, NotBoundException, RemoteException {
        List<String> res = (List<String>) stub.playRound(clientId, choix);
        return res;
    }

    public String RMIResult(IJeu stub, String clientId)
            throws MalformedURLException, NotBoundException, RemoteException {
        return (stub.getGameResult(clientId));
    }

    public String HistoLoad(IJeu stub, String clientId)
            throws MalformedURLException, NotBoundException, RemoteException {
        String res = (String) stub.getClientHistory(clientId);
        return res;
    }
}
