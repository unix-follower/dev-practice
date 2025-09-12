package org.example.assistantonsbservlet.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ScientificNotationJsonSerializer extends StdSerializer<Double> {
    protected ScientificNotationJsonSerializer() {
        super(Double.class);
    }

    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(String.format("%e", value));
    }
}
