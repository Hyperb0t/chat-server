package ru.itis.chat.config;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class CmdArgs {
    @Parameter (names = "--port")
    private Integer port;
    @Parameter (names = "--db-properties")
    private String dbProperties;

    public Integer getPort() {
        return port;
    }

    public String getDbProperties() {
        return dbProperties;
    }
}
