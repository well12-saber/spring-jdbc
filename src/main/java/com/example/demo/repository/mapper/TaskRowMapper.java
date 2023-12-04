package com.example.demo.repository.mapper;

import com.example.demo.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskRowMapper implements RowMapper {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        Task task = new Task();

        task.setId(rs.getLong(Task.Fields.id));
        task.setTitle(rs.getString(Task.Fields.title));
        task.setDescription(rs.getString(Task.Fields.description));
        task.setPriority(rs.getInt(Task.Fields.priority));

        return task;
    }
}
