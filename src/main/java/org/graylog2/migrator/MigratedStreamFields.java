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
    private final Configuration config;

    public MigratedStreamFields(DBObject fields, Configuration config) {
        this.fields = fields;
        this.config = config;
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

    public Configuration getConfig() {
        return config;
    }

    public String toString() {
        return fields.toMap().toString();
    }
}
