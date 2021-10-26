package br.com.fiap.epictask.controller.api;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.epictask.model.Task;
import br.com.fiap.epictask.service.ApiTaskService;

@RestController
@RequestMapping("/api/task")
public class ApiTaskController {
	
	@Autowired
	private ApiTaskService service;
	
	@GetMapping()
	@Cacheable("tasks")
	public Page<Task> index(@RequestParam(required = false) String title, @PageableDefault(size = 20) Pageable pageable){
		return service.getTasks(title, pageable);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Task>  get(@PathVariable Long id) {
		return service.getTask(id);
	}
	
	@PostMapping()
	@CacheEvict(value = "tasks", allEntries = true)
	public ResponseEntity<Task> create(@RequestBody @Valid Task task, UriComponentsBuilder uriBuilder) {
		return service.postTask(task, uriBuilder);
	}
	
	@PutMapping("{id}")
	@CacheEvict(value = "tasks", allEntries = true)
	public ResponseEntity<Task> update(@RequestBody @Valid Task newTask, @PathVariable Long id){
		return service.putTask(newTask, id);
	}
	
	@DeleteMapping("{id}")
	@CacheEvict(value = "tasks", allEntries = true)
	public ResponseEntity<Task> delete(@PathVariable Long id){
		return service.deleteTask(id);
	}
}
