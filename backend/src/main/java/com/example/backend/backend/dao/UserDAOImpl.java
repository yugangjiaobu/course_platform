package com.example.backend.dao.impl;

import com.example.backend.dao.UserDAO;
import com.example.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDAOImpl implements UserDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                // Set other properties if needed
                return user;
            }
        });
    }
}
