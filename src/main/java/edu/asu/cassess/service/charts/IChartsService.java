package edu.asu.cassess.service.charts;

import java.util.List;

public interface IChartsService {

    <T> String getJSONString(List<T> passedInList);

    List<List<Long>> getGitHubChartDataPoints(String JsonString, String contributor);

}