package edu.asu.cassess.persist.entity.taiga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Subselect("SELECT fullName, WeekEnding, COALESCE(ClosedTasks, 0) AS ClosedTasks, COALESCE(OpenTasks, 0) AS OpenTasks FROM (SELECT tasktotals1.fullName, tasktotals1.retrievalDate AS WeekEnding, tasktotals1.tasksClosed - tasktotals2.tasksClosed as ClosedTasks, IF(tasktotals1.tasksClosed - tasktotals2.tasksClosed >= 0, (tasktotals1.tasksOpen - tasktotals2.tasksOpen) + (tasktotals1.tasksClosed - tasktotals2.tasksClosed), tasktotals1.tasksOpen - tasktotals2.tasksOpen) as OpenTasks FROM tasktotals tasktotals1 LEFT JOIN tasktotals tasktotals2 ON tasktotals1.id=tasktotals2.id AND tasktotals2.retrievalDate = (SELECT MAX(tasktotals3.retrievalDate) FROM tasktotals tasktotals3 WHERE tasktotals3.retrievalDate < tasktotals1.retrievalDate AND tasktotals3.id = tasktotals1.id) WHERE tasktotals1.fullName = ?1 ) AS WeeklyTotals")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeeklyTotals {

    @Id
    @Column(name = "WeekEnding")
    private String WeekEnding;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "ClosedTasks")
    private int ClosedTasks;

    @Column(name = "OpenTasks")
    private int OpenTasks;

    public WeeklyTotals() {

    }

    public WeeklyTotals(String WeekEnding, String fullName, int ClosedTasks, int OpenTasks) {
        this.WeekEnding = WeekEnding;
        this.fullName = fullName;
        this.ClosedTasks = ClosedTasks;
        this.OpenTasks = OpenTasks;
    }

    public String getWeekEnding() {
        return WeekEnding;
    }

    public void setWeekEnding(String WeekEnding) {

        Date date = new Date();
        this.WeekEnding = new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getClosedTasks() {
        return ClosedTasks;
    }

    public void setClosedTasks(int ClosedTasks) {
        ClosedTasks = ClosedTasks;
    }

    public int getOpenTasks() {
        return OpenTasks;
    }

    public void setOpenTasks(int OpenTasks) {
        OpenTasks = OpenTasks;
    }


}

