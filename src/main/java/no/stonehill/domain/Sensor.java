package no.stonehill.domain;

import com.fasterxml.jackson.annotation.JsonView;
import no.stonehill.web.rest.Views;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
public class Sensor implements Serializable {
    public static Comparator<Sensor> SENSOR_NAME_COMPARATOR = (o1, o2) -> {
        if (o1.getName() != null && o2.getName() != null) {
            return o2.getName().compareTo(o1.getName());
        }
        return 0;
    };

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Summary.class)
    private long id;
    @JsonView(Views.Summary.class)
    private String sensorId;
    @JsonView(Views.Summary.class)
    private String name;
    @JsonView(Views.Summary.class)
    private String type;

    @JoinColumn(name = "sensorId")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SensorEvent> events;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SensorEvent> getEvents() {
        return events;
    }

    public void setEvents(List<SensorEvent> values) {
        this.events = values;
    }

    public void addValue(SensorEvent value) {
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(value);
    }

    public void sortEventsByDate() {
        List newSet = new ArrayList<>();
        newSet.addAll(getEvents());
        newSet.sort(SensorEvent.UPDATED_TIME_COMPARATOR);
        setEvents(newSet);
    }
}
