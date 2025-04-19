package br.ifsp.tasks_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.ifsp.tasks_api.controller.TaskController;
import br.ifsp.tasks_api.exception.InvalidTaskStateException;
import br.ifsp.tasks_api.model.Task;
import br.ifsp.tasks_api.repository.TaskRepository;
import br.ifsp.tasks_api.service.TaskService;
import br.ifsp.tasks_api.service.impl.TaskServiceImpl;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private TaskRepository repository;
    
    @InjectMocks
    private TaskController taskController;
    
    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private ModelMapper mapper;

    private Task tarefaConcluida;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tarefaConcluida = new Task();
        tarefaConcluida.setId(1L); 
        tarefaConcluida.setTitulo("Tarefa concluída");
        tarefaConcluida.setConcluida(true);
    }

    @Test
    void deveLancarExcecaoAoConcluirTarefaJaConcluida() {
        // Arrange
        tarefaConcluida.setConcluida(true);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(tarefaConcluida));

        // Act & Assert
        assertThrows(InvalidTaskStateException.class, () -> {
            taskService.marcarComoConcluida(1L);
        });
    }

    @Test
    void deveLancarExcecaoAoExcluirTarefaConcluida() {
        // Arrange
        tarefaConcluida.setConcluida(true);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(tarefaConcluida));

        // Act & Assert
        assertThrows(InvalidTaskStateException.class, () -> {
            taskService.deletarTarefa(1L);
        });
    }


}
