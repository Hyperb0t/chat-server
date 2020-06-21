package ru.itis.chat.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.chat.config.Component;
import ru.itis.chat.config.ServerContext;
import ru.itis.chat.dao.UserRepository;
import ru.itis.chat.dao.UserRepositoryImpl;
import ru.itis.chat.dto.Dto;
import ru.itis.chat.dto.LoginMessageDto;
import ru.itis.chat.dto.MessageDto;
import ru.itis.chat.models.Message;
import ru.itis.chat.models.User;

import java.util.Date;

public class AuthService implements Component {
    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Dto loginUser(ServerContext context, String email, String password) {
        Message response = new Message();
        response.setId("server");
        User user = context.getComponent(UserRepositoryImpl.class).getUser(email).orElse(null);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (user != null && encoder.matches(password, user.getPassword())) {
            String jwt = JWT.create().
                    withClaim("role", user.getRole()).
                    withClaim("id", user.getId().toString()).
                    withClaim("login", user.getLogin()).
                    withExpiresAt(new Date(new Date().getTime() + (int) (6 * 10e6))).
                    sign(Algorithm.HMAC256(context.getSecretKey()));
            response.setText("succesfully logged in as " + user.getLogin() + " (" + user.getRole() + ")");
            return new LoginMessageDto(response, jwt); //jwt used to belong to message
        } else {
            return new MessageDto(new Message("server", "wrong login or password, try again"),
                    MessageDto.Target.THIS);
        }
    }

    public Dto logoutUser(ServerContext context) {
        return new MessageDto(new Message("server", "you have successfully logged out"),
                MessageDto.Target.THIS);
    }

    public void registerUser(User user) {

    }
}
