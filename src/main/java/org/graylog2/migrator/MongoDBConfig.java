package org.graylog2.migrator;

import java.net.URI;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
public class MongoDBConfig {
    public static class MongoDBConfigError extends Exception {
        public MongoDBConfigError(String message) {
            super(message);
        }
    }

    private final String DEFAULT_HOST = "localhost";
    private final int DEFAULT_PORT = 27017;

    private final String host;
    private final int port;
    private final String database;

    public static MongoDBConfig create(URI uri) throws MongoDBConfigError {
        return new MongoDBConfig(uri);
    }

    private MongoDBConfig(URI uri) throws MongoDBConfigError {
        if (uri.getHost() == null) {
            host = DEFAULT_HOST;
        } else {
            host = uri.getHost();
        }

        if (uri.getPort() == -1) {
            port = DEFAULT_PORT;
        } else {
            port = uri.getPort();
        }
        if (uri.getPath().isEmpty()) {
            throw new MongoDBConfigError("Missing database in MongoDB URI");
        } else {
            database = uri.getPath().substring(1);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String toString() {
        return String.format("mongodb://%s:%s/%s", host, port, database);
    }
}
