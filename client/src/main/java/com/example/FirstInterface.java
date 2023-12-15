package com.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;

public class FirstInterface {

    public FirstInterface() {
        initComponents();
    }

    private void initComponents() {

        RPCbtn = new JButton("Jouer avec RPC");
        RMIbtn = new JButton("Jouer avec RMI");
        QuitBtn = new JButton("Quitter");

        Main = new JFrame("Pierre-Papier-Ciseaux Game");
        Main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Main.setLayout(new GridBagLayout());

        RPCbtn.addActionListener(e -> {
            try {
                LancerRPC(e);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        });
        RMIbtn.addActionListener(e -> {
            try {
                LancerRMI(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        QuitBtn.addActionListener(this::Quit);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 5, 10, 5);
        Main.add(RPCbtn, gbc);

        gbc.gridy++;
        Main.add(RMIbtn, gbc);

        gbc.gridy++;
        Main.add(QuitBtn, gbc);

        Main.setPreferredSize(new Dimension(600, 500));
        Main.pack();
        Main.setLocationRelativeTo(null);
        Main.setVisible(true);

    }

    private void LancerRPC(ActionEvent evt) throws IOException {
        Main.setVisible(false);
        new GameClientGUI("RPC");
    }

    private void LancerRMI(ActionEvent evt) throws IOException {
        Main.setVisible(false);
        new GameClientGUI("RMI");
    }

    private void Quit(ActionEvent evt) {
        System.exit(0);
    }

    public static void main(String args[]) throws UnsupportedLookAndFeelException {

        FlatDarkLaf.setup();
        UIManager.setLookAndFeel(new FlatDarkLaf());
        new FirstInterface();
    }

    private JButton QuitBtn;
    private JButton RMIbtn;
    private JButton RPCbtn;
    private JFrame Main;
}
