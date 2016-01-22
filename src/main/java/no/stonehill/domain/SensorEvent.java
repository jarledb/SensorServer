package no.stonehill.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class SensorEvent implements Serializable {
    public static Comparator<SensorEvent> REG_TIME_COMPARATOR = (o1, o2) -> {
        if (o1.getRegTime() != null && o2.getRegTime() != null) {
            if (o1.getRegTime().isBefore(o2.getRegTime())) {
                return 1;
            } else if (o1.getRegTime().isAfter(o2.getRegTime())) {
                return -1;
            }
        }
        return 0;
    };

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    //TODO: Stop circular reference from being parsed by JSON and not use JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sensorID")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private Sensor sensor;

    @JoinColumn(name = "sensorEventId")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<EventValue> values;

    @Convert(converter = no.stonehill.config.LocalDateAttributeConverter.class)
    private LocalDateTime regTime;

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public Set<EventValue> getValues() {
        return values;
    }

    public void setValues(Set<EventValue> values) {
        this.values = values;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public ZonedDateTime getRegTimeWithTimeZone() {
        return regTime.atZone(ZoneId.systemDefault());
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void addEventValue(EventValue value) {
        if (values == null) {
            values = new TreeSet<>();
        }
        values.add(value);
    }
}
