package vip.luosu.additional.sensitive.jackson;

import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.introspect.AnnotatedMember;
import tools.jackson.databind.ser.BeanPropertyWriter;
import tools.jackson.databind.ser.ValueSerializerModifier;

import java.util.List;

public class SensitiveValueSerializerModifier extends ValueSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription.Supplier beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {

        for (BeanPropertyWriter writer : beanProperties) {
            AnnotatedMember member = writer.getMember();
            if (member == null) {
                continue;
            }

            Sensitive sensitive = member.getAnnotation(Sensitive.class);
            if (sensitive != null && writer.getType().getRawClass() == String.class) {
                writer.assignSerializer(new SensitiveValueSerializer(sensitive));
            }
        }

        return beanProperties;
    }
}