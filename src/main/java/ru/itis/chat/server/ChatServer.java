package ru.itis.chat.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.itis.chat.config.CmdArgs;
import ru.itis.chat.config.ServerContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private List<ClientHandler> clients;
    private ServerContext serverContext;

    public ChatServer() {
        clients = new CopyOnWriteArrayList<>();
    }

    public void start(CmdArgs cmdArgs) {
        serverContext = new ServerContext(cmdArgs.getDbProperties());
        serverContext.init(Package.getPackage("ru.itis.chat"));
        ApplicationContext springContext = new ClassPathXmlApplicationContext("context.xml");
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(cmdArgs.getPort());
            System.out.println("Server started on port " + cmdArgs.getPort());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        while (true) {
            try {
                new ClientHandler(serverSocket.accept(), this).start();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public ServerContext getServerContext() {
        return serverContext;
    }
}