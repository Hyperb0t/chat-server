package ru.itis.chat.server;

import ru.itis.chat.config.Component;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.controllers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageResolverImpl implements MessageResolver {
    private List<AbstractController> controllers;
    private AbstractController welcomeService;
    private AbstractController notFoundService;

    public MessageResolverImpl(ServerContext context) {
        controllers = new ArrayList<>();
        this.welcomeService = context.getComponent(WelcomeController.class);
        this.notFoundService = context.getComponent(NotFoundController.class);

        for(Map.Entry e : context.getComponentInstances().entrySet()) {
            if(e.getValue().getClass().getSuperclass().equals(AbstractController.class)) {
                controllers.add((AbstractController)e.getValue());
            }
        }
        controllers.stream().forEach(System.out::println);
    }

    public Dto resolve(JavalabProtocolPacket command, Map<String, String> jwtData, ClientHandler thisHandler,
                       ServerContext context) {

        if(command.getHeader().equals("Command")) {
            for (AbstractController s : controllers) {
                if (command.get("command").equals(s.getCommandName())) {
                    return s.execute(command, jwtData, context);
                }
            }
        }else {
            for (AbstractController s : controllers) {
                if (command.getHeader().equals(s.getCommandName())) {
                    return s.execute(command, jwtData, context);
                }
            }
        }
        return notFoundService.execute(command, jwtData, context);
    }

    public Dto executeWelcomeService(ClientHandler thisHandler,
                                      ServerContext context) {

        return welcomeService.execute(null, null, context);
    }
}
