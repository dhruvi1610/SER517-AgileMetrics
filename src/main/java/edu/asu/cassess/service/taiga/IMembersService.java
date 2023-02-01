package edu.asu.cassess.service.taiga;

import edu.asu.cassess.persist.entity.taiga.TaigaMember;

import java.util.List;

/**
 * Created by Thomas on 4/11/2017.
 */
public interface IMembersService {
    List<TaigaMember> getMembers(Long projectId, String token, int page, String team, String course);

    /* Method to provide single operation on
        updating the member_data table based on the course, student and project tables
         */
    void updateMembership(String course);
}
