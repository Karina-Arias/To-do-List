package br.ifsp.tasks_api.repository;

import br.ifsp.tasks_api.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByCategoriaIgnoreCase(String categoria, Pageable pageable);
}

