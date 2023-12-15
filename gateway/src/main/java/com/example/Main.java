package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT = 5001;
    private static final int MAX_POOL_SIZE = 4;
    private static ExecutorService pool = Executors.newFixedThreadPool(MAX_POOL_SIZE);

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                String IDClient = UUID.randomUUID().toString();
                System.out.println("[SERVER] Waiting for client connection...");
                Socket client = serverSocket.accept();
                System.out.println("[SERVER] Connected to client!" + client);
                ClientHandler handler = new ClientHandler(client, IDClient);
                pool.execute(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}