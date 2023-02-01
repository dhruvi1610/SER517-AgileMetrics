package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyTaskTotals {

    @Id
    @Column(name = "Date")
    private String Date;

    @Column(name = "InProgress")
    private long InProgress;

    @Column(name = "ToTest")
    private long ToTest;

    @Column(name = "Done")
    private long Done;

    public DailyTaskTotals() {
    }

    public DailyTaskTotals(String date, long inProgress, long toTest, long done) {
        Date = date;
        InProgress = inProgress;
        ToTest = toTest;
        Done = done;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public long getInProgress() {
        return InProgress;
    }

    public void setInProgress(long inProgress) {
        InProgress = inProgress;
    }

    public long getToTest() {
        return ToTest;
    }

    public void setToTest(long toTest) {
        ToTest = toTest;
    }

    public long getDone() {
        return Done;
    }

    public void setDone(long done) {
        Done = done;
    }
}
