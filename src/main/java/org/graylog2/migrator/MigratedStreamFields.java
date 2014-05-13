package org.graylog2.migrator;

import com.mongodb.DBObject;
import org.bson.types.ObjectId;

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

    public abstract Map<String, Object> getFields();
}
