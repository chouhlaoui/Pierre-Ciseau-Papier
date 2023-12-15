package com.example.Service.Impl;

import com.example.Service.IJeu;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class JeuImpl extends UnicastRemoteObject implements IJeu {
    private Map<String, Integer> scoresClient = new HashMap<>();
    private Map<String, Integer> scoresServer = new HashMap<>();
    private Map<String, StringBuilder> clientHistoryMap = new HashMap<>();

    public JeuImpl() throws RemoteException {
        super();
    }

    @Override
    public List<String> playRound(String clientId, String clientChoice) throws RemoteException {
        String serverChoice = generateRandomChoice();
        String roundResult = determineRoundResult(clientChoice, serverChoice);
        updateScores(clientId, roundResult);

        StringBuilder clientHistory = clientHistoryMap.getOrDefault(clientId, new StringBuilder());
        clientHistory.append("Round : Votre choix - ").append(clientChoice)
                .append(", Choix du serveur - ").append(serverChoice)
                .append(", Résultat - ").append(roundResult).append("\n");
        clientHistoryMap.put(clientId, clientHistory);

        return new ArrayList<>(Arrays.asList(serverChoice, roundResult));

    }

    private String generateRandomChoice() {
        String[] choices = { "Pierre", "Papier", "Ciseau" };
        Random random = new Random();
        int index = random.nextInt(choices.length);
        return choices[index];
    }

    private String determineRoundResult(String clientChoice, String serverChoice) {
        if (clientChoice.equals(serverChoice)) {
            return "Égalité";
        } else if ((clientChoice.contains("Pierre") && serverChoice.contains("Ciseaux")) ||
                (clientChoice.contains("Papier") && serverChoice.contains("Pierre")) ||
                (clientChoice.contains("Ciseaux") && serverChoice.contains("Papier"))) {
            return "Gagné";
        } else {
            return "Perdu";
        }
    }

    private void updateScores(String clientId, String roundResult) {
        int clientScore = scoresClient.getOrDefault(clientId, 0);
        int ServerScore = scoresClient.getOrDefault(clientId, 0);
        if (roundResult.contains("Gagné")) {
            clientScore += 1;
        } else if (roundResult.contains("Perdu")) {
            ServerScore += 1;
        }
        scoresClient.put(clientId, clientScore);
        scoresServer.put(clientId, ServerScore);
    }

    @Override
    public String getGameResult(String clientId) throws RemoteException {
        int clientScore = scoresClient.getOrDefault(clientId, 0);
        int serverScore = scoresServer.getOrDefault(clientId, 0);

        String gameResult;
        if (clientScore == serverScore) {
            gameResult = "Le jeu s'est terminé en égalité avec un score de " + clientScore;
        } else if (clientScore >= 2) {
            gameResult = "Félicitations! Vous avez gagné le jeu avec un score de " + clientScore;
        } else {
            gameResult = "Désolé, vous avez perdu le jeu avec un score de " + clientScore;
        }

        return gameResult;
    }

    @Override
    public String getClientHistory(String clientId) throws RemoteException {
        StringBuilder clientHistory = clientHistoryMap.getOrDefault(clientId, new StringBuilder());

        if (clientHistory.length() == 0) {
            return "Aucun historique disponible pour ce client.";
        }

        return "Votre historique des rounds :\n" + clientHistory.toString();
    }
}
