package com.siri.todo.repositories;

import com.siri.todo.modals.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Task, Long> {
    List<Task> findByName(String name);
}
