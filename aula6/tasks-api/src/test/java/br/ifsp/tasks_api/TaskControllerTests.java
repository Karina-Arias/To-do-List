package br.ifsp.tasks_api;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifsp.tasks_api.controller.TaskController;
import br.ifsp.tasks_api.dto.TaskDTO;
import br.ifsp.tasks_api.exception.InvalidTaskStateException;
import br.ifsp.tasks_api.service.TaskService;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private TaskDTO tarefaValida;
    
    @BeforeEach
    public void setUp() {
        tarefaValida = new TaskDTO();
        tarefaValida.setTitulo("Estudar Spring");
        tarefaValida.setDataLimite(LocalDate.now().plusDays(1));  // ou o valor adequado
    }

    @Test
    void deveCriarTarefaComDadosValidos() throws Exception {
        Map<String, Object> tarefaMap = new HashMap<>();
        tarefaMap.put("titulo", "Estudar Spring");
        tarefaMap.put("descricao", "Revisar testes");
        tarefaMap.put("prioridade", "ALTA"); // Enum como string
        tarefaMap.put("dataLimite", LocalDate.now().plusDays(2).toString()); // ano-mês-dia
        tarefaMap.put("categoria", "estudo");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaMap)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveRetornarErroAoCriarTarefaComDataLimiteInvalida() throws Exception {
        tarefaValida.setDataLimite(LocalDate.now().minusDays(1));

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefaValida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveBuscarTarefaPorId() throws Exception {
        Long id = 1L; 
        when(taskService.buscarPorId(id)).thenReturn(tarefaValida);

        mockMvc.perform(get("/api/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Estudar Spring"));
    }


    @Test
    void deveRetornarErroAoExcluirTarefaConcluida() throws Exception {
        Long id = 1L;
        doThrow(new InvalidTaskStateException("Tarefa já concluída")).when(taskService).deletarTarefa(id);

        mockMvc.perform(delete("/api/tasks/" + id))
               .andExpect(status().isConflict());
    }


    @Test
    void deveListarTarefasComPaginacao() throws Exception {
        mockMvc.perform(get("/api/tasks?page=0&size=5&sort=prioridade,asc"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarTarefasPorCategoria() throws Exception {
        mockMvc.perform(get("/api/tasks/search?categoria=estudo"))
                .andExpect(status().isOk());
    }
}
