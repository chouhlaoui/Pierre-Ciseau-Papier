package com.example.Service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IJeu extends Remote {

    List<String> playRound(String clientId, String clientChoice) throws RemoteException;

    String getGameResult(String clientId) throws RemoteException;

    String getClientHistory(String clientId) throws RemoteException;

}
