package com.janilso.crudspring.jobs.controllers;

import com.janilso.crudspring.jobs.dtos.TaskDTO;
import com.janilso.crudspring.jobs.models.TaskModel;
import com.janilso.crudspring.jobs.services.TaskServices;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskServices taskServices;

    @GetMapping()
    public ResponseEntity<List<TaskModel>> getTasks(
            @RequestParam(value = "checked", required=false) Boolean checked,
            @RequestParam(value = "title", required=false) String title ){

        if (checked != null && title != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    this.taskServices.findByTitleContainingIgnoreCaseAndChecked(title, checked));
        }
        if (checked != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    this.taskServices.findByChecked(checked));
        }
        if (title != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    this.taskServices.findByTitleContainingIgnoreCase(title));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                this.taskServices.findAll());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Object> getTaskByUUID(@PathVariable("uuid") String uuidStr){
        try {
            UUID uuid = UUID.fromString(uuidStr);
            Optional<TaskModel> taskModel = this.taskServices.findById(uuid);
            if(taskModel.isEmpty()){
                return notFoundResponse();
            }
            return ResponseEntity.status(HttpStatus.OK).body(taskModel.get());
        } catch (Exception e){
            return notFoundResponse();
        }
    }

    @PostMapping()
    public ResponseEntity<Object> createTask(@RequestBody @Valid TaskDTO taskDTO){

        var taskModel = new TaskModel();
        BeanUtils.copyProperties(taskDTO, taskModel);
        taskModel.setChecked(false);
        taskModel.setCreated_at(LocalDateTime.now(ZoneId.of("UTC")));
        taskModel.setUpdated_at(LocalDateTime.now(ZoneId.of("UTC")));

        List<TaskModel> tasksContainEqualTitle = this.taskServices.findByTitleIsLikeIgnoreCase(taskModel.getTitle());

        if(!tasksContainEqualTitle.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A task with that title already exists.");
        }
        return  ResponseEntity.status(HttpStatus.CREATED).body(this.taskServices.save(taskModel));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Object> deleteTask(@PathVariable("uuid") UUID uuid){
        Optional<TaskModel> taskModel = this.taskServices.findById(uuid);
        if(taskModel.isEmpty()){
            return notFoundResponse();
        }
        this.taskServices.deleteById(uuid);
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted");
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Object> updateTask(@PathVariable("uuid") String uuidStr,
                                             @RequestBody @Valid TaskDTO taskDTO){
        try {
            UUID uuid = UUID.fromString(uuidStr);
            Optional<TaskModel> taskModelOptional = this.taskServices.findById(uuid);

            if(taskModelOptional.isEmpty()){
                return notFoundResponse();
            }
            var taskModel = new TaskModel();
            BeanUtils.copyProperties(taskDTO, taskModel);
            taskModel.setUuid(taskModelOptional.get().getUuid());
            taskModel.setCreated_at(taskModelOptional.get().getCreated_at());
            taskModel.setUpdated_at(LocalDateTime.now(ZoneId.of("UTC")));

            return ResponseEntity.status(HttpStatus.OK).body(this.taskServices.save(taskModel));

        } catch (Exception e){
            return notFoundResponse();
        }
    }

    private ResponseEntity<Object> notFoundResponse(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
    }

}
