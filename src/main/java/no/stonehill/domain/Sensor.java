package no.stonehill.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Sensor<T> implements Serializable {

    private long id;
    private String sensorId;
    private String name;
    private List<T> values;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }
    public void addValue(T value) {
        if (values == null) {
            values = new ArrayList<>();
        }
        values.add(value);
    }
}
