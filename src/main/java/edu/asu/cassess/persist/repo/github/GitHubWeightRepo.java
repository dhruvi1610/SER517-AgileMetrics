package edu.asu.cassess.persist.repo.github;

import edu.asu.cassess.persist.entity.github.GitHubPK;
import edu.asu.cassess.persist.entity.github.GitHubWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GitHubWeightRepo extends JpaRepository<GitHubWeight, GitHubPK> {
}

