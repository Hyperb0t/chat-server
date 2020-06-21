package ru.itis.chat.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.chat.models.Message;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class MessageRepositoryJdbcTemplateImpl implements MessageRepository {

    //SQL запросы вынесены в константы
    //language=SQL
    private  String SQL_INSERT = "INSERT INTO public.\"Messages\" " +
                            "(\"User_id\", \"Content\", \"DateTime\")" +
                            "VALUES (?,?,?)";
    //language=SQL
    private  String SQL_SELECT_ALL = "SELECT * FROM public.\"Messages\"";
    //language=SQL
    private  String SQL_SELECT_BY_ID = "SELECT * FROM public.\"Messages\" " +
            "WHERE \"Id\" = ?";
    //language=SQL
    private JdbcTemplate jdbcTemplate;

    //Конструктор принимает экземпляр JdbcTemplate
    public MessageRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Создание RowMapper'a с помощью лямбда выражения
    RowMapper<Message> messageRowMapper = (row, rowNumber) ->
            Message.builder()
            .id(Integer.toString(row.getInt(1)))
            .text(row.getString(3))
            .date(row.getTimestamp(4).toString())
            .build();

    @Override
    public void writeMessage(int userId, String message) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement st = con.prepareStatement(SQL_INSERT);
            st.setInt(1,userId);
            st.setString(2, message);
            st.setTimestamp(3, Timestamp.from(Instant.now()));
            return st;
        }, keyHolder);
    }

    @Override
    public List<Message> getMessagePage(int size, int page) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        RowMapper<Message> customMessageRowMapper = (row, rowNumber) ->
            Message.builder()
                    .id(row.getString(1))
                    .text(row.getString(2))
                    .date(row.getTimestamp(3).toString())
                    .build();
        String SQL = "SELECT public.\"Users\".\"Login\"," +
                            " public.\"Messages\".\"Content\"," +
                            " public.\"Messages\".\"DateTime\" FROM public.\"Messages\"" +
                            "JOIN public.\"Users\" ON " +
                            "public.\"Messages\".\"User_id\" = public.\"Users\".\"Id\"" +
                            "ORDER BY \"DateTime\" DESC " +
                            "LIMIT " + size + " OFFSET " +  (page-1)*size;

        return jdbcTemplate.query(SQL, customMessageRowMapper);
    }

    @Override
    public List<Message> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, messageRowMapper);
    }

    //Запрос для получения экземляра модели по Id
    @Override
    public Optional<Message> find(Integer id) {
        try {
            Message message = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, messageRowMapper);
            return Optional.ofNullable(message);
        }catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
