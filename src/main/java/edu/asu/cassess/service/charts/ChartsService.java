/*package edu.asu.cassess.service.charts;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChartsService {

    ///Keeps track of the timezone for Phoenix
    private final ZoneId zoneId = ZoneId.of("America/Phoenix");

    public <T> String getJSONString(List<T> passedInList) {

        ///Creates an empty initial String
        String jsonString = "";

        /// Creates chart data in the form [{<data1>},{<data2>}]
        ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
            jsonString = mapper.writeValueAsString(passedInList);
        }catch(Exception e){
            e.printStackTrace();
        }

        return jsonString;
    }

    public List<List<Long>> getGitHubChartDataPoints(String JsonString, String contributor){

        /// Create returned collections that will be arrays in the json object
        List<List<Long>> commitArray = new ArrayList<>();
        //List<Long> weeks = new ArrayList<>();

        /// Read in the chart Data as a JSON array
        JSONArray jsonarray = new JSONArray(JsonString);

        /// Check Each object in the JSON array for contributor name
        for (int i = 0; i < jsonarray.length(); i++) {
            /// Separates each  JSON object out of the JSON array.
            JSONObject jsonObject = jsonarray.getJSONObject(i);
            JSONObject commitDataPK = jsonObject.getJSONObject("commitDataPK");

            ///Gets the username or contributor from chartData
            String username = commitDataPK.getString("username");

            ///Makes sure username is equal to contributor
            if(username.equals(contributor)){
                /// Gets the week they committed
                String date = commitDataPK.getString("date");
                LocalDate day = LocalDate.parse(date);
                long week = day.atStartOfDay(zoneId).toEpochSecond() * 1000;

                /// Gets the number of commits they made
                int commits = jsonObject.getInt("commits");
                Long commitsObj = new Long(commits);

                ///Creates an array of [x,y] data points
                List<Long> dataPoints = new ArrayList<>();
                ///Adds the x
                dataPoints.add(new Long(week));
                ///Adds the y
                dataPoints.add(commitsObj);
                /// Adds the array of data points to an array
                commitArray.add(dataPoints);
            }
        }

        return commitArray;
    }


    public List<List<Long>> getTaigaChartDataPoints(String JsonString, String dayName, String value) {

        /// Create collections that will be arrays in the json object
        List<List<Long>> chartData = new ArrayList<>();

        /// Read in the chart Data as a JSON array
        JSONArray progressArray = new JSONArray(JsonString);

        /// Check Each object in the JSON for the day and value
        for (int i = 0; i < progressArray.length(); i++) {

            List<Long> dataPoints = new ArrayList<>();

            /// Separates each  JSON object out of the JSON array.
            JSONObject jsonObject = progressArray.getJSONObject(i);

            ///Converts the String into milliseconds
            String weekEnding = jsonObject.getString(dayName);
            LocalDate day = LocalDate.parse(weekEnding);
            long longDay = day.atStartOfDay(zoneId).toEpochSecond() * 1000;
            Long longDayObj = new Long(longDay);
            ///Adds the day(milliseconds)into the arrays.
            dataPoints.add(longDayObj);

            /// Gets the Long for the value
            Long closedTasks = jsonObject.getLong(value);
            ///Adds the value to
            dataPoints.add(closedTasks);

            ///Adds the [x,y] values to the List of Lists
            chartData.add(dataPoints);
        }

        return chartData;
    }

}*/
