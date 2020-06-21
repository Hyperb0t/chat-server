package ru.itis.chat.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.itis.chat.models.Product;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryJdbcTemplateImpl implements ProductRepository {

    //language=SQL
    private  String SQL_SELECT_ALL = "SELECT * FROM public.\"Products\"";
    //language=SQL
    private  String SQL_SELECT_BY_ID = "SELECT * FROM public.\"Products\" " +
            "WHERE \"Id\" = ?";
    //language=SQL
    private  String SQL_INSERT = "INSERT INTO public.\"Products\" (\"Name\", \"Price\", \"Description\") " +
            "VALUES (?,?,?)";
    //language=SQL
    private  String SQL_INSERT_BINDING = "INSERT INTO public.\"Products_to_users\" " +
                            "(\"Product_id\", \"User_id\")" +
                            "VALUES (?,?)";

    private JdbcTemplate jdbcTemplate;

    public ProductRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Product> productRowMapper = (row, rowNumber) ->
        Product.builder()
            .id(row.getInt(1))
            .name(row.getString(2))
            .description(row.getString(3))
            .price(row.getInt(4))
            .build();

    @Override
    public List<Product> getAllProducts() {
        return jdbcTemplate.query(SQL_SELECT_ALL, productRowMapper);
    }

    @Override
    public void addProduct(String name, int price, String description) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement st =connection.prepareStatement(SQL_INSERT);
            st.setString(1, name);
            st.setInt(2, price);
            st.setString(3, description);
            return st;
        }, keyHolder);

    }

    @Override
    public void addProductToUser(int productId, int userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection ->{
            PreparedStatement st = connection.prepareStatement(SQL_INSERT_BINDING);
            st.setInt(1, productId);
            st.setInt(2, userId);
            return st;
        }, keyHolder);
    }

    @Override
    public Optional<Product> find(Integer id) {
        try {
            Product product = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, productRowMapper);
            return Optional.ofNullable(product);
        }catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
