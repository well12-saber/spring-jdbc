package com.example.demo.repository;

import com.example.demo.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class InMemoryTaskRepository implements TaskRepository {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> findAll() {
        log.debug("Call findAll");

        return tasks;
    }

    @Override
    public Optional<Task> findById(Long id) {

        log.debug("Call findByID");

        return tasks.stream()
                .filter(t -> t.getId()==id)
                .findFirst();
    }

    @Override
    public Task save(Task task) {
        log.debug("Calling save");

        task.setId(System.currentTimeMillis());
        tasks.add(task);

        return task;

    }

    @Override
    public Task update(Task task) {

        log.debug("Calling update");

        Task existedTask = findById(task.getId()).orElse(null);

        if (existedTask!=null){

            existedTask.setPriority(task.getPriority());
            existedTask.setDescription(task.getDescription());
            existedTask.setTitle(task.getTitle());

        }
        return existedTask;
    }

    @Override
    public void deleteById(Long id) {

        log.debug("Calling delete, id is " + id);

        findById(id).ifPresent(tasks::remove);

    }

    @Override
    public void batchInsert(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }
}
