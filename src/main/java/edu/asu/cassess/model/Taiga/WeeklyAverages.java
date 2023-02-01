package edu.asu.cassess.model.Taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyAverages {

    @Id
    @Column(name = "week")
    private String week;

    @Column(name = "weekBeginning")
    private String weekBeginning;

    @Column(name = "weekEnding")
    private String weekEnding;

    @Column(name = "DoneAverage")
    private String DoneAverage;

    @Column(name = "InProgressAverage")
    private String InProgressAverage;

    @Column(name = "ToTestAverage")
    private String ToTestAverage;

    public WeeklyAverages() {
    }


    public WeeklyAverages(String week, String weekBeginning, String weekEnding, String doneAverage, String inProgressAverage, String toTestAverage) {
        this.week = week;
        this.weekBeginning = weekBeginning;
        this.weekEnding = weekEnding;
        this.DoneAverage = doneAverage;
        this.InProgressAverage = inProgressAverage;
        this.ToTestAverage = toTestAverage;
    }


    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeekBeginning() {
        return weekBeginning;
    }

    public void setWeekBeginning(String weekBeginning) {
        this.weekBeginning = weekBeginning;
    }

    public String getWeekEnding() {
        return weekEnding;
    }

    public void setWeekEnding(String weekEnding) {
        this.weekEnding = weekEnding;
    }

    public String getDoneAverage() {
        return DoneAverage;
    }

    public void setDoneAverage(String doneAverage) {
        DoneAverage = doneAverage;
    }

    public String getInProgressAverage() {
        return InProgressAverage;
    }

    public void setInProgressAverage(String inProgressAverage) {
        InProgressAverage = inProgressAverage;
    }

    public String getToTestAverage() {
        return ToTestAverage;
    }

    public void setToTestAverage(String toTestAverage) {
        ToTestAverage = toTestAverage;
    }


}
