package org.addy.model.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.addy.util.GenericTypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.function.Function;

public abstract class EntityDeserializer<E, K> extends JsonDeserializer<E> {
    private final Class<E> entityType;
    private final Class<K> keyType;

    private final String keyAttribute;
    private final Function<JsonNode, K> keyExtractor;
    private final Function<K, E> byKeyConstructor;

    @SuppressWarnings("unchecked")
    protected EntityDeserializer(String keyAttribute,
                                 Function<JsonNode, K> keyExtractor,
                                 Function<K, E> byKeyConstructor) {

        Type[] typeArgs = GenericTypeUtils.findParameterizedType(getClass()).getActualTypeArguments();
        this.entityType = (Class<E>) typeArgs[0];
        this.keyType = (Class<K>) typeArgs[1];

        this.keyAttribute = keyAttribute;
        this.keyExtractor = keyExtractor;
        this.byKeyConstructor = byKeyConstructor;
    }

    @Override
    public E deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode rootNode = parser.getCodec().readTree(parser);
        K key;

        if (rootNode.isValueNode()) {
            key = keyExtractor.apply(rootNode);
        } else if (rootNode.isObject() && rootNode.has(keyAttribute)) {
            key = keyExtractor.apply(rootNode.get(keyAttribute));
        } else {
            throw new IllegalArgumentException(String.format(
                    "Could not parse the given expression as a reference to %s",
                    entityType.getSimpleName()));
        }

        return byKeyConstructor.apply(key);
    }
}
