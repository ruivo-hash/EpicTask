package br.com.fiap.epictask.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.epictask.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String username);
	
	Page<User> findByNameLike(String name, Pageable pageable);
	
	@Query(value="from User order by points desc")
	List<User> listOrderByPoints();

}
