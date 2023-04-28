package edu.asu.cassess.projections;

public interface GitFileChangesStats {

  String getTeam();

  String getFullName();

  Integer getAdditions();

  Integer getDeletions();
}