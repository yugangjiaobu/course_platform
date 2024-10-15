import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addUser(User user) throws Exception {
        String query = "INSERT INTO users (user_id, password, role, name, email, phone, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(query, new String[]{"user_id"});
                stmt.setString(1, user.getUserId());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getRole());
                stmt.setString(4, user.getName());
                stmt.setString(5, user.getEmail());
                stmt.setString(6, user.getPhone());
                stmt.setString(7, user.getCreatedAt());
                stmt.setString(8, user.getUpdatedAt());
                return stmt;
            }
        }, keyHolder);
        // If you need the generated key (e.g., for auto-increment fields)
        Number generatedId = keyHolder.getKey().longValue();
    }

    @Override
    public void updateUser(User user) throws Exception {
        String query = "UPDATE users SET password = ?, role = ?, name = ?, email = ?, phone = ?, updated_at = ? WHERE user_id = ?";
        jdbcTemplate.update(query,
            user.getPassword(),
            user.getRole(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getUpdatedAt(),
            user.getUserId()
        );
    }

    @Override
    public void deleteUser(String userId) throws Exception {  
        String query = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(query, userId);
    }

    @Override
    public User getUserById(String userId) throws Exception {
        String query = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{userId}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }
        });
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                );
            }
        });
    }
}
