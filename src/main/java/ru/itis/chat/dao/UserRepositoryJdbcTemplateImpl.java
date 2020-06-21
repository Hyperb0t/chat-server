package ru.itis.chat.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.chat.models.User;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcTemplateImpl implements UserRepository {
    //language=SQL
    private  String SQL_SELECT_BY_LOGIN = "SELECT * FROM public.\"Users\" " +
            "WHERE \"Login\"=? LIMIT 1";

    private  String SQL_SELECT_PASSWORD_BY_LOGIN = "SELECT \"Password\" FROM public.\"Users\"" +
                            "WHERE \"Login\"=?" +
                            "LIMIT 1";
    private  String SQL_INSERT = "INSERT INTO public.\"Users\" (\"Login\", \"Password\", \"Role\") " +
            "VALUES (?,?,?,?)";

    private  String SQL_SELECT_ALL ="SELECT * FROM public.\"Users\"";

    private JdbcTemplate jdbcTemplate;

    public UserRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> userRowMapper = (row, rowNumber) ->
            User.builder()
                    .id(row.getInt("id"))
                    .login(row.getString(2))
                    .password(row.getString(3))
                    .role(row.getString(4))
                    .build();

    @Override
    public Optional<User> getUser(String login) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_LOGIN, new Object[]{login}, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> checkPassword(String login) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_LOGIN, new Object[]{login}, userRowMapper);
            Optional<User> optUser = Optional.ofNullable(user);
            if(optUser.isPresent()) {
                return Optional.ofNullable(optUser.get().getPassword());
            }
            else throw new EmptyResultDataAccessException("user not found",1);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            return statement;
        }, keyHolder);

        user.setId((Integer) keyHolder.getKey());
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, userRowMapper);
    }
}
