package com.example;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import com.example.Service.Impl.JeuImpl;

public class Server {
    public static void main(String[] args) {
        try {
            WebServer webServer = new WebServer(8081);
            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
            XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
            serverConfig.setEnabledForExtensions(true);

            PropertyHandlerMapping phm = new PropertyHandlerMapping();
            phm.addHandler("Jeu", JeuImpl.class);
            xmlRpcServer.setHandlerMapping(phm);
            webServer.start();
            System.out.println("Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}