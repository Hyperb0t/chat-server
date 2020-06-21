package ru.itis.chat.config;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionComponent implements Component {
    private Connection connection;

    public ConnectionComponent() {
    }

    public ConnectionComponent(String dbPropertiesFile) {
        this.connection = createConnection(dbPropertiesFile);
    }

    public ConnectionComponent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection createConnection(String dbPropertiesFile) {
        Properties pr = new Properties();
        try {
            pr.load(new FileReader(dbPropertiesFile));
        }catch (Exception e) {
            System.out.println("can't load properties file");
            throw new IllegalStateException(e);
        }
        try {
            return DriverManager.getConnection(pr.getProperty("url"),
                    pr.getProperty("user"), pr.getProperty("password"));
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {
        return "ConnectionComponent{" +
                "connection=" + connection +
                '}';
    }
}
