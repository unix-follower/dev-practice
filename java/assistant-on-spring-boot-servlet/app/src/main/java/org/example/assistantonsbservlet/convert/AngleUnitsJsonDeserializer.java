package org.example.assistantonsbservlet.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.assistantonsbservlet.api.model.AngleUnit;

import java.io.IOException;

public class AngleUnitsJsonDeserializer extends StdDeserializer<AngleUnit> {
    protected AngleUnitsJsonDeserializer() {
        super(AngleUnit.class);
    }

    @Override
    public AngleUnit deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return AngleUnit.valueOf(p.getText().toUpperCase());
    }
}
