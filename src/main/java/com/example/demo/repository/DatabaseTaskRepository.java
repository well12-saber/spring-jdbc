package com.example.demo.repository;

import com.example.demo.Task;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.repository.mapper.TaskRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
@Primary
public class DatabaseTaskRepository implements TaskRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Task> findAll() {
        log.debug("Calling DB findAll");

        String sql = "select * from task";

        return jdbcTemplate.query(sql, new TaskRowMapper());
    }

    @Override
    public Optional<Task> findById(Long id) {
        log.debug("Callind DB findByID with id:{}" , id);

        String sql = "Select * from task where id = ?";
        Task task = DataAccessUtils.singleResult(
                (Collection<Task>) jdbcTemplate.query(
                        sql
                        , new ArgumentPreparedStatementSetter(new Object[] {id})
                        , new RowMapperResultSetExtractor<>(new TaskRowMapper(), 1)
                )
        );

        return Optional.ofNullable(task);
    }

    @Override
    public Task save(Task task) {
        log.debug("Calling DB save with task : {}" , task);

        task.setId(System.currentTimeMillis());

        String sql = "Insert to task (title,description, priority , id)" +
                "values (? , ? , ? , ?)";

        jdbcTemplate.update(sql
                , task.getTitle()
                , task.getDescription()
                , task.getPriority()
                , task.getId()
        );

        return task;
    }

    @Override
    public Task update(Task task) {
        log.debug("Calling DB update with task : {}" , task);

        Task existedTask = findById(task.getId()).orElse(null);

        if (existedTask !=null){


            String sql = "Update task" +
                    " SET TITLE ?" +
                    ", SET description ?" +
                    ", SET PRIPITY >" +
                    "WHERE ID = ?";

            jdbcTemplate.update(sql
                    , task.getTitle()
                    , task.getDescription()
                    , task.getPriority()
                    , task.getId()
            );
        }

        log.warn("Task with ID {} not found ", task.getId());

        throw new TaskNotFoundException("Task for update not found");
    }

    @Override
    public void deleteById(Long id) {

        log.debug("Calling DB delete with ID : {}" , id);

        String sql = "DELETE from task where id = ?";

        jdbcTemplate.update(sql, id);

    }

    @Override
    public void batchInsert(List<Task> tasks) {

        log.debug("Calling batchInsert");

        String sql = "Insert to task (title,description, priority , id)" +
                "values (? , ? , ? , ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Task task = tasks.get(i);
                ps.setString(1 , task.getTitle());
                ps.setString(2 , task.getDescription());
                ps.setInt(   3 , task.getPriority());
                ps.setLong(  4 , task.getId());
            }

            @Override
            public int getBatchSize() {
                return tasks.size();
            }
        });

    }
}
