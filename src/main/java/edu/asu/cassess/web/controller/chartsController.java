package edu.asu.cassess.web.controller;/*package edu.asu.cassess.web.controller;

import edu.asu.cassess.dao.taiga.ITaskTotalsQueryDao;
import edu.asu.cassess.model.Taiga.DisplayAllTasks;
import edu.asu.cassess.persist.entity.github.CommitData;
import edu.asu.cassess.persist.entity.taiga.WeeklyTotals;
import edu.asu.cassess.service.charts.ChartsService;
import edu.asu.cassess.service.github.GatherGitHubData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class chartsController {

    ///Keeps track of the timezone for Phoenix
    private final ZoneId zoneId = ZoneId.of("America/Phoenix");

    @Autowired
    ITaskTotalsQueryDao iTaskTotalsQueryDao;

    @Autowired
    private GatherGitHubData gatherGitHubData;

    @Autowired
    private ChartsService chartsService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/GitHubIndividualCommitChart", produces = "application/json")
    public  Map<String, Object> gitHubIndividualCommitChartsResource(){

        ///Creates a hash map
        Map<String, Object> model = new HashMap<>();

        ///Gets the commit data as a whole
        List<CommitData> chartData = gatherGitHubData.getCommitList();

        //Converts the List to JSON String
        String jsonString = chartsService.getJSONString(chartData);

        /// Create returned collections that will be arrays in the json object
        List<List<Long>> commitArray = chartsService.getGitHubChartDataPoints(jsonString, "tjjohn1");

        model.put("values", commitArray);
        model.put("key", "GitHubData");

        return model;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/TaigaBarGraph", produces = "application/json")
    public  List<Object> TaigaBarGraphChartsResource(){

        List<Object> data = new ArrayList();

        ///Creates a hash map
        Map<String, Object> closedTasks = new HashMap<>();
        Map<String, Object> inProgress = new HashMap<>();
        Map<String, Object> toTest = new HashMap<>();

        ///Converts the Progress Data to chart Data

        List<WeeklyTotals> tasksProgress = iTaskTotalsQueryDao.getWeeklyTasks("Christopher Moretti");

        //Converts the List to JSON String
        String progressString = chartsService.getJSONString(tasksProgress);

        List<List<Long>> closedTasksData = chartsService.getTaigaChartDataPoints(progressString, "weekEnding", "closedTasks");

        ////Converts the Record data to data for a chart

        List<DisplayAllTasks> tasksRecords = iTaskTotalsQueryDao.getTaskTotals("Christopher Moretti");

        //Converts the List to JSON String
        String recordString = chartsService.getJSONString(tasksRecords);

        List<List<Long>> inProgressTasksData = chartsService.getTaigaChartDataPoints(recordString, "retrievalDate", "tasks_in_progress");

        List<List<Long>> toTestTasksData = chartsService.getTaigaChartDataPoints(recordString, "retrievalDate", "tasks_ready_for_test");

        ///Create a map that becomes the chart data object
        //inProgress.put("key", "In Progress Data");
        inProgress.put("values",inProgressTasksData);
        inProgress.put("key", "In Progress Data");

        ///Add the data to the array
        data.add(inProgress);

        closedTasks.put("key", "Closed Tasks");
        closedTasks.put("values", closedTasksData);

        ///Add the data to the array
        data.add(closedTasks);

        toTest.put("key","To Test Tasks");
        toTest.put("values",toTestTasksData);

        ///Add the data to the array
        data.add(toTest);

        return data;
    }
}*/
