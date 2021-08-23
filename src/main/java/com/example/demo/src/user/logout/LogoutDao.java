package com.example.demo.src.user.logout;

import com.example.demo.src.user.model.PostLogoutRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;

@Repository
public class LogoutDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean isLogOutJwt(String jwt) {
        String isLogoutJwtQuery = "select exists (select * from Logout where jwt = ?)";
        String isLogoutJwtParams = jwt;

        return this.jdbcTemplate.queryForObject(isLogoutJwtQuery,
                boolean.class,
                isLogoutJwtParams);
    }


    public PostLogoutRes logout(BigInteger idx, String jwtToken) {
        String logoutQuery = "insert into Logout (jwt) VALUES (?)";
        Object[] logoutParams = new Object[] { jwtToken };

        this.jdbcTemplate.update(logoutQuery, logoutParams);

        return new PostLogoutRes(idx, jwtToken);
    }
}
