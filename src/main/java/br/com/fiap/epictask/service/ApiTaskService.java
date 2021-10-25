package br.com.fiap.epictask.service;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.epictask.model.Task;
import br.com.fiap.epictask.repository.TaskRepository;

@Service
public class ApiTaskService {

	@Autowired
	private TaskRepository repository;
	
	public Page<Task> getTasks(String title, Pageable pageable){
		if(title == null) 
			return repository.findAll(pageable);
		
		return repository.findByTitleLike("%" + title + "%", pageable);
	}
	public Optional<Task> getTask(Long id) {
		return repository.findById(id);
	}
	public URI postTask(Task task, UriComponentsBuilder uriBuilder) {
		repository.save(task);
		return uriBuilder.path("/api/task/{id}").buildAndExpand(task.getId()).toUri();
	}
	public ResponseEntity<Task> putTask(Task newTask, @PathVariable Long id){
		Optional<Task> optional = repository.findById(id);
		
		if(optional.isEmpty()) 
			return ResponseEntity.notFound().build();
		
		Task task = optional.get(); 
		
		task.setTitle(newTask.getTitle());
		task.setDescription(newTask.getDescription());
		task.setPoints(newTask.getPoints());
		
		repository.save(task);
		
		return ResponseEntity.ok(task);
	}
	public ResponseEntity<Task> deleteTask(Long id) {
		Optional<Task> task = repository.findById(id);
		
		if(task.isEmpty()) 
			return ResponseEntity.notFound().build() ;
		
		repository.deleteById(id);
		
		return ResponseEntity.ok().build();
	}
}
