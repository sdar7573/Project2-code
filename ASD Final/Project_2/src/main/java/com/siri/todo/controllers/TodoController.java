package com.siri.todo.controllers;

import com.siri.todo.modals.Task;
import com.siri.todo.repositories.TodoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Controller()
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/")
    public String getAllTasks(Model model) {
        List<Task> tasks = todoRepository.findAll();
        tasks.sort(Comparator.comparing(Task::getDue));
        model.addAttribute("taskList", tasks);
        return "index";
    }

    @GetMapping("/task/{id}")
    public Task getTaskById(@PathVariable("id") long id) {
        return todoRepository.findById(id).orElseGet(() -> {
            System.out.println("Not Found");
            return new Task();
        });
    }

    @GetMapping("/add")
    public String createTaskPage(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        return "new-task";
    }

    @PostMapping("/save")
    public String createTask(@ModelAttribute("task") Task task) {
        List<Task> tasks = todoRepository.findByName(task.getName());
        if (tasks.size() == 0) {
            todoRepository.save(task);
        }
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public String updateTaskPage(@PathVariable("id") long id, Model model) {
        Optional<Task> taskOptional = todoRepository.findById(id);
        taskOptional.ifPresent(task -> model.addAttribute("task", task));
        return "edit-task";
    }

    @RequestMapping("/complete/{id}")
    public String completeTask(@PathVariable long id) {
        Task task = new Task();
        if (todoRepository.findById(id).isPresent()) {
            task = todoRepository.findById(id).get();
            task.setActive(false);
        }
        todoRepository.save(task);
        return "redirect:/";
    }

    @RequestMapping("/delete/{id}")
    public String deleteTask(@PathVariable long id) {
        todoRepository.deleteById(id);
        return "redirect:/";
    }
}
