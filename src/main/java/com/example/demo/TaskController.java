package com.example.demo;

import com.example.demo.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/")
    private String index(Model model){

        model.addAttribute("tasks", taskService.findAll());

        return "index";

    }

    @GetMapping("/task/create")
    public String showCreateForm(Model model){

        model.addAttribute("task", new Task());

        return "create";
    }

    @PostMapping("/task/create")
    public String createTask(@ModelAttribute Task task){

        task.setId(System.currentTimeMillis());
        taskService.save(task);

        return "redirect:/";

    }

    @GetMapping("/task/edit/{id}")
    public String showEditForm(@PathVariable long id, Model model){

        Task task = taskService.findById(id); 
        if (task!=null){
            model.addAttribute("task", task);

            return "edit";
        }

        return "redirect:/";

    }

    @PostMapping("/task/edit")
    public String editTask(@ModelAttribute Task task){

        Task existedTask = taskService.update(task);

        if (existedTask!=null){

            existedTask.setPriority(task.getPriority());
            existedTask.setDescription(task.getDescription());
            existedTask.setTitle(task.getTitle());

        }

        return "redirect:/";

    }

    @GetMapping("/task/delete/{id}")
    public String taskDelete(@PathVariable Long id){

        Task task = taskService.findById(id);

        if (task!=null){
            taskService.deleteById(id);
        }

        return "redirect:/";

    }

}
