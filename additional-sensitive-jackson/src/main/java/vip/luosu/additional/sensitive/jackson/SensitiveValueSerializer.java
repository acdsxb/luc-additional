package vip.luosu.additional.sensitive.jackson;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class SensitiveValueSerializer extends ValueSerializer<Object> {

    private final Sensitive sensitive;

    public SensitiveValueSerializer(Sensitive sensitive) {
        this.sensitive = sensitive;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializationContext ctxt) {
        if (value == null) {
            gen.writeNull();
            return;
        }

        if (!(value instanceof String str)) {
            gen.writePOJO(value);
            return;
        }

        gen.writeString(SensitiveUtil.mask(str, sensitive));
    }
}