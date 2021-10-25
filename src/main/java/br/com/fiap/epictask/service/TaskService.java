package br.com.fiap.epictask.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.fiap.epictask.exception.NotAllowedException;
import br.com.fiap.epictask.exception.TaskNotFoundException;
import br.com.fiap.epictask.model.Task;
import br.com.fiap.epictask.model.User;
import br.com.fiap.epictask.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository repository;
	
	@Autowired
	private MessageSource message;
	
	public ModelAndView listTasks() {
		List<Task> tasks = repository.listPendingTasks();
		ModelAndView modelAndView = new ModelAndView("tasks");
		modelAndView.addObject("tasks", tasks);
		return modelAndView;
	}
	
	public ModelAndView listTaskCompleted() {
		List<Task> completedTasks = repository.listCompletedTask();
		ModelAndView modelAndView = new ModelAndView("tasks_completed");
		modelAndView.addObject("tasks", completedTasks);
		return modelAndView;
	}
	
	public boolean saveTask(Task task, BindingResult result, RedirectAttributes redirect) {
		if(result.hasErrors()) return false;
		repository.save(task);
		redirect.addFlashAttribute("message", message.getMessage("task.new.sucess", null, LocaleContextHolder.getLocale()));
		return true;
	}
	
	public String holdTask(Long id, Authentication auth) {
		Optional<Task> optional = repository.findById(id);
		if(optional.isEmpty())
			throw new TaskNotFoundException("Tarefa não encontrada");
		
		Task task = optional.get();
		
		if(task.getUser() != null)
			throw new NotAllowedException("A tarefa já esta atribuída");
		
		User user = (User) auth.getPrincipal();
		task.setUser(user);
		
		repository.save(task);
		
		return "redirect:/task";
	}
	
	public String releaseTask(Long id, Authentication auth) {
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
