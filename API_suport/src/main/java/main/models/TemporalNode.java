package main.models;

import java.util.HashMap;
import java.util.Map;

public class TemporalNode {
    private final String id;
    private final Map<String, Object> attributes;

    public TemporalNode(String id) {
        this.id = id;
        this.attributes = new HashMap<>();
    }

    public TemporalNode(String id, Map<String, Object> attributes) {
        this.id = id;
        this.attributes = attributes != null ? new HashMap<>(attributes) : new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public String toString() {
        return "TemporalNode{" +
                "id='" + id + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
