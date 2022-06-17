package com.janilso.crudspring.jobs.services;

import com.janilso.crudspring.jobs.models.TaskModel;
import com.janilso.crudspring.jobs.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServices {
    @Autowired
    private TaskRepository taskRepository;

    public List<TaskModel> findByTitleContainingIgnoreCaseAndChecked(String title, boolean checked){
        return this.taskRepository.findByTitleContainingIgnoreCaseAndChecked(title, checked);
    }
    public List<TaskModel> findByTitleContainingIgnoreCase(String title){
        return this.taskRepository.findByTitleContainingIgnoreCase(title);
    }
    public List<TaskModel> findByChecked(boolean checked){
        return this.taskRepository.findByChecked(checked);
    }
    public List<TaskModel> findAll(){
        return this.taskRepository.findAll();
    }
    public Optional<TaskModel> findById(UUID uuid){
        return this.taskRepository.findById(uuid);
    }
    @Transactional
    public TaskModel save(TaskModel taskModel){
        return this.taskRepository.save(taskModel);
    }
    @Transactional
    public void deleteById(UUID uuid){
        this.taskRepository.deleteById(uuid);
    }
    public List<TaskModel> findByTitleIsLikeIgnoreCase(String title){
        return this.taskRepository.findByTitleIsLikeIgnoreCase(title);
    }
}
