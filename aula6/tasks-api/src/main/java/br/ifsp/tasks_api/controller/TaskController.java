package br.ifsp.tasks_api.controller;

import br.ifsp.tasks_api.dto.*;
import br.ifsp.tasks_api.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Criar nova tarefa
    @PostMapping
    public ResponseEntity<TaskDTO> criarTarefa(@Valid @RequestBody TaskCreateDTO taskCreateDTO) {
        TaskDTO taskDTO = taskService.criarTarefa(taskCreateDTO);
        return new ResponseEntity<>(taskDTO, HttpStatus.CREATED);
    }

    // Listar tarefas com paginação
    @GetMapping
    public ResponseEntity<Page<TaskDTO>> listarTarefas(Pageable pageable) {
        Page<TaskDTO> tasks = taskService.listarTarefas(pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Buscar tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> buscarTarefaPorId(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.buscarPorId(id);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    // Buscar tarefas por categoria
    @GetMapping("/search")
    public ResponseEntity<Page<TaskDTO>> buscarPorCategoria(@RequestParam String categoria, Pageable pageable) {
        Page<TaskDTO> tasks = taskService.buscarPorCategoria(categoria, pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Marcar tarefa como concluída
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<TaskDTO> concluirTarefa(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.marcarComoConcluida(id);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    // Atualizar tarefa
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> atualizarTarefa(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {
        TaskDTO taskDTO = taskService.atualizarTarefa(id, taskUpdateDTO);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    // Deletar tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        taskService.deletarTarefa(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
