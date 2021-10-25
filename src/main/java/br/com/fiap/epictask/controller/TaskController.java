package br.com.fiap.epictask.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.epictask.exception.NotAllowedException;
import br.com.fiap.epictask.model.Task;
import br.com.fiap.epictask.service.TaskService;

@Controller
@RequestMapping("/task")
public class TaskController {
	
	@Autowired
	private TaskService service;

	@GetMapping()
	public ModelAndView index() {
		return service.listTasks();
	}
	
	@GetMapping("/completed")
	public ModelAndView taskCompleted() {
		return service.listTaskCompleted();
	}
	
	@RequestMapping("/new")
	public String create(Task task) {
		return "task_form";
	}
	
	@PostMapping()
	public String save(@Valid Task task, BindingResult result, RedirectAttributes redirect) {
		if (!service.saveTask(task, result, redirect)) {
			return "task_form";			
		}
		return "redirect:/task";
	}
	
	@GetMapping("/hold/{id}")
	public String hold(@PathVariable Long id, Authentication auth) throws NotAllowedException {
		return service.holdTask(id, auth);
	}
	
	@GetMapping("/release/{id}")
	public String release(@PathVariable Long id, Authentication auth) throws NotAllowedException {
		return service.releaseTask(id, auth);
	}
}
