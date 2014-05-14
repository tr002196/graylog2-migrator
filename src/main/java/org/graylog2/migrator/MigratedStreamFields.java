package org.graylog2.migrator;

import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
public abstract class MigratedStreamFields {
    protected final DBObject fields;

    public MigratedStreamFields(DBObject fields) {
        this.fields = fields;
    }

    public ObjectId getId() {
        return (ObjectId) fields.get("_id");
    }

    public abstract Map<String, Object> getFields() throws UnsupportedFieldsError;

    public List<? extends MigratedStreamFields> getStreamRules() {
        return new ArrayList<>();
    }

    public static class UnsupportedFieldsError extends Exception {
        public UnsupportedFieldsError(String message) {
            super(message);
        }
    }

    public String toString() {
        return fields.toMap().toString();
    }
}
