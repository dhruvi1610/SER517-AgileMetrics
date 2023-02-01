package edu.asu.cassess.persist.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Thomas on 2/11/2017. Used to populate authority during user registration.
 *
 * @author tjjohn1
 */
@Entity
@Subselect("SELECT * FROM taskdata")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserID {

    @Id
    @Column(name = "Max")
    public long Max;

    public UserID() {
    }

    public UserID(long Max) {
        this.Max = Max;
    }

    public long getMax() {
        return Max;
    }

    public void setMax(long Max) {
        this.Max = Max;
    }

}
