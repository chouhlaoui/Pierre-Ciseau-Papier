package com.example;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.Scanner;

public class GameClientGUI {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5001;
    Socket socket;
    Scanner scanner = new Scanner(System.in);

    public GameClientGUI(String type) throws IOException {
        try {
            initComponents();
            socket = new Socket(SERVER_IP, SERVER_PORT);
            if (socket != null) {
                pw = new PrintWriter(socket.getOutputStream(), true);
                in = new InputStreamReader(socket.getInputStream());
                bf = new BufferedReader(in);

                commType.append(type);
                pw.println(type);
                pw.flush();
            } else {
                System.exit(1);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initComponents() {

        rockButton = new JButton("Pierre");
        paperButton = new JButton("Papier");
        scissorsButton = new JButton("Ciseaux");
        historyButton = new JButton("Historique");
        rockButton.addActionListener(new PierreChoisi());
        scissorsButton.addActionListener(new CiseauxChoisi());
        paperButton.addActionListener(new PapierChoisi());
        historyButton.addActionListener(new HistoriqueChoisi());

        resultatGame = new JTextArea();
        historyArea = new JTextArea();
        commType = new JTextArea("Type de communication : ");
        clientChoice = new JTextArea("Votre choix : ");
        serverChoice = new JTextArea("Le choix de l'ordinateur : ");
        resultatRound = new JTextArea("Résultat round : ");

        resultatGame.setForeground(Color.RED);

        resultatGame.setEditable(false);
        commType.setEditable(false);
        clientChoice.setEditable(false);
        resultatRound.setEditable(false);
        serverChoice.setEditable(false);
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        historyArea.setEditable(false);
        historyArea.setRows(4);
        historyArea.setColumns(45);
        main = new JFrame("Pierre-Papier-Ciseaux Game");
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setLayout(new GridBagLayout());
        /************************************/
        GridBagConstraints gbcBouton = new GridBagConstraints();
        JPanel History = new JPanel();
        gbcBouton.gridx = 0;
        gbcBouton.gridy = 0;
        gbcBouton.insets = new Insets(20, 30, 10, 10);
        History.add(historyArea, gbcBouton);
        gbcBouton.gridx++;
        History.add(historyButton, gbcBouton);
        gbcBouton.gridx++;

        /************************************************/
        JPanel boutons = new JPanel();
        gbcBouton.gridx = 0;
        gbcBouton.gridy = 0;
        gbcBouton.insets = new Insets(20, 30, 10, 10);
        boutons.add(rockButton, gbcBouton);
        gbcBouton.gridx++;
        boutons.add(paperButton, gbcBouton);
        gbcBouton.gridx++;
        boutons.add(scissorsButton, gbcBouton);
        /************************************************/
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 5, 10, 5);
        gbc.gridy = 0;
        gbc.gridx = 0;
        main.add(resultatGame, gbc);
        gbc.gridy++;
        main.add(commType, gbc);
        gbc.gridy++;
        main.add(boutons, gbc);
        gbc.gridy++;
        main.add(clientChoice, gbc);
        gbc.gridy++;
        main.add(serverChoice, gbc);
        gbc.gridy++;
        main.add(resultatRound, gbc);
        gbc.gridy++;
        main.add(History, gbc);
        gbc.gridy++;

        main.setPreferredSize(new Dimension(1000, 700));
        main.pack();
        main.setLocationRelativeTo(null);
        main.setVisible(true);

    }

    class PierreChoisi implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            resultatGame.setText("");
            clientChoice.setText("Votre choix : Pierre");
            pw.println("Pierre");
            try {
                String msg = bf.readLine();
                if (msg.contains("Final")) {
                    msg = bf.readLine();
                    resultatGame.setText(msg);
                } else {
                    if (msg != null) {
                        serverChoice.setText("Le choix de l'ordinateur : " + msg);
                    }
                    msg = bf.readLine();
                    if (msg != null) {
                        resultatRound.setText("Résultat round : " + msg);
                    }
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    class CiseauxChoisi implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            resultatGame.setText("");

            clientChoice.setText("Votre choix : Ciseaux");
            pw.println("Ciseaux");
            try {
                String msg = bf.readLine();
                if (msg.contains("Final")) {
                    msg = bf.readLine();
                    resultatGame.setText(msg);
                } else {
                    if (msg != null) {
                        serverChoice.setText("Le choix de l'ordinateur : " + msg);
                    }
                    msg = bf.readLine();
                    if (msg != null) {
                        resultatRound.setText("Résultat round : " + msg);
                    }
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    class PapierChoisi implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            resultatGame.setText("");

            clientChoice.setText("Votre choix : Papier");
            pw.println("Papier");
            try {
                String msg = bf.readLine();
                if (msg.contains("Final")) {
                    msg = bf.readLine();

                    resultatGame.setText(msg);
                } else {
                    if (msg != null) {
                        serverChoice.setText("Le choix de l'ordinateur : " + msg);
                    }
                    msg = bf.readLine();
                    if (msg != null) {
                        resultatRound.setText("Résultat round : " + msg);
                    }
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    class HistoriqueChoisi implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            resultatGame.setText("");
            pw.println("Historique");
            pw.flush();

            try {
                String msg = "";
                String msg1;
                while (((msg1 = bf.readLine()) != null)
                        && !(msg1.equals("\n")) && !(msg1.equals("\0"))
                        && !(msg1.equals(""))) {
                    if (msg1.contains("Round") || msg1.contains("round") || msg1.contains("Aucun")) {
                        msg = msg.concat(msg1) + "\n";
                    }

                }
                historyArea.setText(msg);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private JTextArea commType;

    private JTextArea resultatGame;
    private JTextArea clientChoice;
    private JTextArea historyArea;
    private JTextArea serverChoice;
    private JTextArea resultatRound;
    private PrintWriter pw;
    private InputStreamReader in;
    private BufferedReader bf;
    private JButton rockButton, paperButton, scissorsButton, historyButton;
    private JFrame main;
}
