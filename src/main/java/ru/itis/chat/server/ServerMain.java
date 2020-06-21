package ru.itis.chat.server;

import com.beust.jcommander.JCommander;
import ru.itis.chat.config.CmdArgs;

public class ServerMain {
    public static void main(String[] args) {
        CmdArgs cmdArgs = new CmdArgs();
        JCommander.newBuilder().addObject(cmdArgs).build().parse(args);
        ChatServer chatServer = new ChatServer();
        chatServer.start(cmdArgs);
    }
}
