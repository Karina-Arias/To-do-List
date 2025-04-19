package br.ifsp.tasks_api.service;

import br.ifsp.tasks_api.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDTO criarTarefa(TaskCreateDTO dto);
    TaskDTO atualizarTarefa(Long id, TaskUpdateDTO dto);
    TaskDTO buscarPorId(Long id);
    Page<TaskDTO> listarTarefas(Pageable pageable);
    Page<TaskDTO> buscarPorCategoria(String categoria, Pageable pageable);
    TaskDTO marcarComoConcluida(Long id);
    void deletarTarefa(Long id);
}
