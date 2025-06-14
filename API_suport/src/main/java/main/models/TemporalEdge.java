package main.models;

public class TemporalEdge {
    private String from;
    private String to;
    private int startTime;
    private int duration;

    public TemporalEdge(String from, String to, int startTime, int duration) {
        this.from = from;
        this.to = to;
        this.startTime = startTime;
        this.duration = duration;
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
}
