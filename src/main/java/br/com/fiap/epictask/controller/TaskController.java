package br.com.fiap.epictask.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.epictask.exception.TaskNotFoundException;
import br.com.fiap.epictask.model.Task;
import br.com.fiap.epictask.model.User;
import br.com.fiap.epictask.repository.TaskRepository;

@Controller
@RequestMapping("/task")
public class TaskController {
	
	@Autowired
	private TaskRepository repository;
	
	@Autowired
	private MessageSource message;

	@GetMapping()
	public ModelAndView index() {
		List<Task> tasks = repository.findAll();
		ModelAndView modelAndView = new ModelAndView("tasks"); //essa tasks é a view, nome da pagina html
		modelAndView.addObject("tasks", tasks); // (nome do atributo que será passado pra view, objeto que será passda pra view)
		return modelAndView;
	}
	
	@PostMapping()
	public String save(@Valid Task task, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) return "task_form";
		repository.save(task);
		redirect.addFlashAttribute("message", message.getMessage("task.new.sucess", null, LocaleContextHolder.getLocale()));
		return "redirect:/task";
	}
	
	@RequestMapping("/new")
	public String create(Task task) {
		return "task_form";
	}
	
	@GetMapping("/hold/{id}")
	public String hold(@PathVariable Long id, Authentication auth) throws NotAllowedException {
		//buscar a tarfa no banco
		Optional<Task> optional = repository.findById(id);
		if(optional.isEmpty())
			throw new TaskNotFoundException("Tarefa não encontrada");
		
		Task task = optional.get();
		
		if(task.getUser() != null)
			throw new NotAllowedException("A tarefa já esta atribuída");
		
		// atribuir o ser na task
		User user = (User) auth.getPrincipal();
		task.setUser(user);
		
		//salvo no banco
		repository.save(task);
		
		return "redirect:/task";
	}
	
	@GetMapping("/release/{id}")
	public String release(@PathVariable Long id, Authentication auth) throws NotAllowedException {
		Optional<Task> optional = repository.findById(id);
		if (optional.isEmpty())
			throw new TaskNotFoundException("Tarefas não encontrada");
		
		Task task = optional.get();
		User user = (User) auth.getPrincipal();
		
		if(!task.getUser().equals(user))
			throw new NotAllowedException("A tarefa está tribuída para outra pessoa");

		task.setUser(null);
		
		repository.save(task);
		
		return "redirect:/task";
	}
}
