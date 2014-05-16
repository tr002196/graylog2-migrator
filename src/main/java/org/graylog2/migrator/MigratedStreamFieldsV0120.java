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

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
public class MigratedStreamFieldsV0120 extends MigratedStreamFields {
    public MigratedStreamFieldsV0120(DBObject fields, Configuration config) {
        super(fields, config);
    }

    @Override
    public Map<String, Object> getFields() {
        Map<String, Object> newFields = new HashMap<>();

        newFields.put("title", fields.get("title"));
        newFields.put("disabled", fields.get("disabled"));
        newFields.put("created_at", fields.get("created_at"));
        newFields.put("updated_at", fields.get("updated_at"));
        newFields.put("description", fields.get("description"));
        newFields.put("creator_user_id", getConfig().getCreator());

        return newFields;
    }

    @Override
    public List<? extends MigratedStreamFields> getStreamRules() {
        List<MigratedStreamFields> rules = new ArrayList<>();

        for (Object _rule : (BasicDBList) fields.get("streamrules")) {
            DBObject rule = (DBObject) _rule;
            rules.add(new MigratedStreamRulesFieldsV0120(rule, getConfig()));
        }

        return rules;
    }
}
