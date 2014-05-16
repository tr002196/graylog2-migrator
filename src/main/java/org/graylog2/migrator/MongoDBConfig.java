/*
 * Copyright 2012-2014 TORCH GmbH
 *
 * This file is part of Graylog2.
 *
 * Graylog2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog2.  If not, see <http://www.gnu.org/licenses/>.
 */

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

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 27017;

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
