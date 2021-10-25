package br.com.fiap.epictask.service;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.epictask.model.User;
import br.com.fiap.epictask.repository.UserRepository;

@Service
public class ApiUserService {

	@Autowired
	private UserRepository repository;
	
	public Page<User> getUsers(String name, Pageable pageable) {
		if(name == null)
			return repository.findAll(pageable);
	
		return repository.findByNameLike("%"+name+"%", pageable);
	}
	public ResponseEntity<User> getUser(Long id) {
		return ResponseEntity.of(repository.findById(id));
	}
	public ResponseEntity<User> postUser(User user, UriComponentsBuilder uriBuilder) {
		repository.save(user);
		URI uri = uriBuilder.path("/api/user/{id}")
				.buildAndExpand(user.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}
	public ResponseEntity<User> putUser(Long id, User newUser) {
		Optional<User> optional = repository.findById(id);
		
		if(optional.isEmpty()) 
			return ResponseEntity.notFound().build();
		
		User user = optional.get();
		
		user.setName(newUser.getName());
		user.setEmail(newUser.getEmail());
		user.setPassword(newUser.getPassword());
		user.setAge(newUser.getAge());
		
		repository.save(user);
		
		return ResponseEntity.ok().build();
	}
	public ResponseEntity<User> deleteUser(Long id) {
		Optional<User> user = repository.findById(id);
		if(user.isEmpty()) 
			return ResponseEntity.notFound().build();
		
		repository.deleteById(id);
		
		return ResponseEntity.ok().build();
	}
}
