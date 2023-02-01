package edu.asu.cassess.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Thomas on 4/11/2017.
 */
@Entity
@Subselect("SELECT * FROM admins")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminCount {

    @Id
    @Column(name = "Total")
    public int Total;

    public AdminCount() {
    }

    public AdminCount(int Total) {
        this.Total = Total;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }
}
