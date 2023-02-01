package edu.asu.cassess.persist.repo.rest;

import edu.asu.cassess.persist.entity.rest.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StudentRepo extends JpaRepository<Student, String> {

}
