package org.graylog2.migrator;

import com.mongodb.DBObject;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
public class StreamFieldsBuilder {
    public static MigratedStreamFields buildStreamFields(DBObject obj, String version) throws RuntimeException {
        switch (version) {
            case "0.12.0":
                return new MigratedStreamFieldsV0120(obj);
            default:
                throw new RuntimeException("Unknown version: " + version);
        }
    }
}
