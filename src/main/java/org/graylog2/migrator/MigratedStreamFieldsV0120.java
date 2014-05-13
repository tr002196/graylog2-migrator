package org.graylog2.migrator;

import com.mongodb.DBObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
public class MigratedStreamFieldsV0120 extends MigratedStreamFields {
    public MigratedStreamFieldsV0120(DBObject fields) {
        super(fields);
    }

    @Override
    public Map<String, Object> getFields() {
        Map<String, Object> newFields = new HashMap<>();

        newFields.put("title", fields.get("title"));
        newFields.put("disabled", fields.get("disabled"));
        newFields.put("created_at", fields.get("created_at"));
        newFields.put("updated_at", fields.get("updated_at"));
        newFields.put("description", fields.get("description"));
        /* TODO: Check the database for the first admin user or add command line option? */
        newFields.put("creator_user_id", "admin");

        return newFields;
    }
}
