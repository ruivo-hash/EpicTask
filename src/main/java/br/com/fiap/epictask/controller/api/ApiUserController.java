package br.com.fiap.epictask.controller.api;

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

import br.com.fiap.epictask.model.User;
import br.com.fiap.epictask.service.ApiUserService;

@RestController
@RequestMapping("/api/user")
public class ApiUserController {

	@Autowired
	private ApiUserService service;
	
	@GetMapping
	@Cacheable("users")
	public Page<User> index(@RequestParam(required = false) String name, @PageableDefault(size = 20) Pageable pageable) {
		return service.getUsers(name, pageable);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<User> get(@PathVariable Long id) {
		return service.getUser(id);
	}
	
	@PostMapping
	@CacheEvict(value = "users", allEntries = true)
	public ResponseEntity<User> create(@RequestBody User user, UriComponentsBuilder uriBuilder) {
		return service.postUser(user, uriBuilder);
	}
	
	@PutMapping("{id}")
	@CacheEvict(value = "users", allEntries = true)
	public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User newUser){
		return service.putUser(id, newUser);
	}
	
	@DeleteMapping("{id}")
	@CacheEvict(value = "users", allEntries = true)
	public ResponseEntity<User> delete(@PathVariable Long id){
		return service.deleteUser(id);
	}
}
