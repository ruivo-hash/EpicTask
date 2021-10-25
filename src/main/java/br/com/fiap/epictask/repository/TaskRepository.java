package br.com.fiap.epictask.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fiap.epictask.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

	Page<Task> findByTitleLike(String title, Pageable pageable);

	@Query(value = "from Task where status <> 100")
	List<Task> listPendingTasks();
	
	@Query(value = "from Task where status = 100")
	List<Task> listCompletedTask();
}
