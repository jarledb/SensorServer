package no.stonehill.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Access(AccessType.FIELD)
public class EventValue implements Comparable<EventValue>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    //TODO: Stop circular reference from being parsed by JSON and not use JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sensorEventId")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private SensorEvent sensorEvent;

    private String key;
    private  String value;

    public EventValue() {
    }

    public EventValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SensorEvent getSensorEvent() {
        return sensorEvent;
    }

    public void setSensorEvent(SensorEvent sensorEvent) {
        this.sensorEvent = sensorEvent;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(EventValue o) {
        return key.compareTo(o.getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventValue that = (EventValue) o;

        if (id != that.id) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
