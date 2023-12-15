package com.example;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.example.Service.Impl.JeuImpl;

public class Server {
    public static void main(String[] args) throws RemoteException, MalformedURLException, InterruptedException {
        LocateRegistry.createRegistry(1099);
        JeuImpl distanceObject = new JeuImpl();
        System.out.println(distanceObject.toString());
        Naming.rebind("rmi://localhost:1099/Games/PPC", distanceObject);
    }
}
