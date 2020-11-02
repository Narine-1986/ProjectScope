package am.itspace.projectscope.repository;

import am.itspace.projectscope.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer>, QuerydslPredicateExecutor<Project> {

    @Query(value = "SELECT * from project p JOIN project_user pu ON p.id=pu.project_id JOIN user u ON pu.user_id=u.id WHERE u.id= ?1 ", nativeQuery = true)
    List<Project> findAllByUserId(Integer id);



}
