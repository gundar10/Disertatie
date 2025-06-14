package main.models;

import java.util.HashMap;
import java.util.Map;

public class TemporalEdge {
    private final String from;
    private final String to;
    private final int startTime;
    private final int duration;
    private final Map<String, Object> attributes;

    public TemporalEdge(String from, String to, int startTime, int duration) {
        this(from, to, startTime, duration, new HashMap<>());
    }

    public TemporalEdge(String from, String to, int startTime, int duration, Map<String, Object> attributes) {
        this.from = from;
        this.to = to;
        this.startTime = startTime;
        this.duration = duration;
        this.attributes = attributes != null ? new HashMap<>(attributes) : new HashMap<>();
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
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
        return "TemporalEdge{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", attributes=" + attributes +
                '}';
    }
}
