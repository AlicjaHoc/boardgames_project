package com.project.boardgames.controllers;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

import com.project.boardgames.ErrorUtilities.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RequestMapping("/api/v1")
@RestControllerAdvice
public class ApiV1ControllerAdvice {
    private static final Logger logger = Logger.getLogger(ApiV1ControllerAdvice.class.getName());

    @Autowired
    private DataSource dataSource;

    @RequestMapping("/")
    public void checkDatabaseConnectivity() throws AppException {
        try {
            logger.log(Level.INFO, "Checking database connectivity");
            Connection connection = dataSource.getConnection();
            connection.close();
            logger.log(Level.INFO, "Database connectivity check succeeded");
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Database connectivity check failed", ex);
            throw new AppException("Cannot connect to the database.", 400, "error", true);
        }
    }
}