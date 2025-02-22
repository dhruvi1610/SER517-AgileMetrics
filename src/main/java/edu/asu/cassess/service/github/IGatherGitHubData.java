package edu.asu.cassess.service.github;

import edu.asu.cassess.persist.entity.github.CommitData;

import edu.asu.cassess.persist.entity.rest.Student;
import java.util.List;
import java.util.Set;

public interface IGatherGitHubData {
    void fetchContributorsStats(String owner, String projectName, String course, String team, String accessToken);

    List<CommitData> getCommitList();
}
