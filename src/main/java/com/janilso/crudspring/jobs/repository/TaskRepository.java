package com.janilso.crudspring.jobs.repository;

import com.janilso.crudspring.jobs.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {

    // Query manual
    @Query("SELECT j from TaskModel j where j.checked = :checked")
    public List<TaskModel> findAllJobsByChecked(@Param("checked") boolean checked);

    // Usando o padrão do JPA
    public List<TaskModel> findByChecked(boolean checked);

    public List<TaskModel> findByTitleContainingIgnoreCase(String title);
    public List<TaskModel> findByTitleContainingIgnoreCaseAndChecked(String title, boolean checked);

    public List<TaskModel> findByTitleIsLikeIgnoreCase(String title);
}
