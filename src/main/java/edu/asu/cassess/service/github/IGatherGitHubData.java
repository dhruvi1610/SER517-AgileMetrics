package edu.asu.cassess.service.github;

import edu.asu.cassess.persist.entity.github.CommitData;

import java.util.List;

public interface IGatherGitHubData {
    void fetchData(String owner, String projectName, String course, String team, String accessToken);

    List<CommitData> getCommitList();
}
