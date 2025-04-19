package br.ifsp.tasks_api.service.impl;

import br.ifsp.tasks_api.dto.*;
import br.ifsp.tasks_api.model.*;
import br.ifsp.tasks_api.exception.*;
import br.ifsp.tasks_api.repository.TaskRepository;
import br.ifsp.tasks_api.service.TaskService;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public TaskDTO criarTarefa(TaskCreateDTO dto) {
        Task task = mapper.map(dto, Task.class);
        task.setPrioridade(dto.getPrioridade());        
        Task salva = repository.save(task);
        return mapper.map(salva, TaskDTO.class);
    }

    @Override
    public TaskDTO atualizarTarefa(Long id, TaskUpdateDTO dto) {
        Task task = buscarTarefaOuLancar(id);
        if (task.isConcluida()) throw new InvalidTaskStateException("Tarefa já concluída");

        task.setTitulo(dto.getTitulo());
        task.setDescricao(dto.getDescricao());
        task.setPrioridade(dto.getPrioridade());
        task.setDataLimite(dto.getDataLimite());
        task.setCategoria(dto.getCategoria());

        return mapper.map(repository.save(task), TaskDTO.class);
    }

    @Override
    public TaskDTO buscarPorId(Long id) {
        Task task = buscarTarefaOuLancar(id);
        return mapper.map(task, TaskDTO.class);
    }

    @Override
    public Page<TaskDTO> listarTarefas(Pageable pageable) {
        return repository.findAll(pageable).map(task -> mapper.map(task, TaskDTO.class));
    }

    @Override
    public Page<TaskDTO> buscarPorCategoria(String categoria, Pageable pageable) {
        return repository.findByCategoriaIgnoreCase(categoria, pageable)
                .map(task -> mapper.map(task, TaskDTO.class));
    }

    @Override
    public TaskDTO marcarComoConcluida(Long id) {
        Task task = buscarTarefaOuLancar(id);
        if (task.isConcluida()) {
            throw new InvalidTaskStateException("Tarefa já concluída.");
        }
        task.setConcluida(true);
        return mapper.map(repository.save(task), TaskDTO.class);
    }


    @Override
    public void deletarTarefa(Long id) {
        Task task = buscarTarefaOuLancar(id);
        if (task.isConcluida()) throw new InvalidTaskStateException("Tarefa já concluída não pode ser removida.");
        repository.delete(task);
    }

    private Task buscarTarefaOuLancar(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));
    }
}
