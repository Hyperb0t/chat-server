package ru.itis.chat.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.dto.LoginMessageDto;
import ru.itis.chat.dto.MessageDto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends Thread {
    private ChatServer server;
    private Socket clientSocket;
    private BufferedReader in;
    private MessageResolverImpl resolver;
    private Boolean authenticated;

    public Boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    ClientHandler(Socket socket, ChatServer server) {
        this.server = server;
        this.clientSocket = socket;
        this.authenticated = false;
        server.getClients().add(this);
        System.out.println("New client " + clientSocket.getLocalAddress());
        resolver = new MessageResolverImpl(server.getServerContext());
    }

    private Map<String, String> getJwtData(JavalabProtocolPacket packet) {
        if (isAuthenticated()) {
            String jwt = packet.get("jwt");
            Algorithm algorithmHS = Algorithm.HMAC256(server.getServerContext().getSecretKey());
            JWTVerifier verifier = JWT.require(algorithmHS).build();
            Map<String, String> result = new HashMap<>();
            try {
                DecodedJWT Djwt = verifier.verify(jwt);
                result.put("role", Djwt.getClaim("role").asString());
                result.put("id", Djwt.getClaim("id").asString());
                result.put("login", Djwt.getClaim("login").asString());
            } catch (Exception e) {
                System.out.println("invalid jwt in request");
                result = null;
            }
            return result;
        } else {
            return null;
        }
    }

    public void run() {
        try {
            sendResponse(resolver.executeWelcomeService(this, server.getServerContext()));
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                JavalabProtocolPacket inputPacket = JavalabProtocolPacket.fromJson(inputLine);
                sendResponse(resolver.resolve(inputPacket, getJwtData(inputPacket),
                        this, server.getServerContext()));
            }
        } catch (SocketException e) {
            System.out.println(getClientSocket().getLocalAddress() + " disconnected");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void sendResponse(Dto dto) {
        try {
            if (dto.getClass().equals(MessageDto.class)) {
                MessageDto messageDto = (MessageDto) dto;
                if(messageDto.getHeader().equals("Logout")) {
                    this.setAuthenticated(false);
                }
                if (messageDto.getTarget().equals(MessageDto.Target.ALL)) {
                    for (ClientHandler handler : server.getClients()) {
                        if (handler.equals(this)) {
                            continue;
                        }
                        PrintWriter w = new PrintWriter(handler.getClientSocket().getOutputStream(), true);
                        w.println((new JavalabProtocolPacket(messageDto.getHeader(), messageDto.getPayload())).toJson());
                        return;
                    }
                }
            }
            else if(dto.getClass().equals(LoginMessageDto.class)) {
                this.setAuthenticated(true);
            }
            PrintWriter w = new PrintWriter(this.getClientSocket().getOutputStream(), true);
            w.println((new JavalabProtocolPacket(dto.getHeader(), dto.getPayload())).toJson());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}