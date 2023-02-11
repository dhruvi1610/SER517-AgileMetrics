package edu.asu.cassess.persist.repo.rest;

import edu.asu.cassess.persist.entity.rest.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdminsRepo extends JpaRepository<Admin, String> {

}