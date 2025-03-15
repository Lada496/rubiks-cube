package com.rubiks.controller;

import com.rubiks.rubiks_cube.controller.CubeController;
import com.rubiks.rubiks_cube.exceptions.OperationException;
import com.rubiks.rubiks_cube.models.Operation;
import com.rubiks.rubiks_cube.models.Cube.Axis;
import com.rubiks.rubiks_cube.services.CubeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class CubeControllerTest {

    @Mock
    private CubeService cubeService;

    @InjectMocks
    private CubeController cubeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cubeController).build();
    }

    // Test: Get Cube
    @Test
    void testGetCube() throws Exception {
        when(cubeService.getCube()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/cube"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(cubeService, times(1)).getCube();
    }

    // Test: Operate - Success
    @Test
    void testOperateSuccess() throws Exception {
        String requestBody = "{\"axis\": \"X\", \"direction\": 1, \"index\": 1}";

        Operation expectedOperation = new Operation(Axis.X, 1, 1); 

        ArgumentCaptor<Operation> operationCaptor = ArgumentCaptor.forClass(Operation.class);

        mockMvc.perform(post("/cube/operate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent()); // 204 No Content (success)

        verify(cubeService, times(1)).operate(operationCaptor.capture());

        Operation capturedOperation = operationCaptor.getValue();
        assertEquals(expectedOperation.getAxis(), capturedOperation.getAxis());
        assertEquals(expectedOperation.getDirection(), capturedOperation.getDirection());
        assertEquals(expectedOperation.getIndex(), capturedOperation.getIndex());
    }

    // Test: Operate - Failure (Bad Request) with invalid axis
    @Test
    void testOperateFailureWithInvalidAxis() throws Exception {
        mockMvc.perform(post("/cube/operate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"axis\": \"A\", \"direction\": 1, \"index\": 1}"))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    // Test: Operate - Failure (Bad Request) with invalid direction
    @Test
    void testOperateFailureWithInvalidDirection() throws Exception {
        // Simulate the exception for invalid direction
        mockMvc.perform(post("/cube/operate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"axis\": \"X\", \"direction\": 3, \"index\": 1}"))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    // Test: Operate - Failure (Bad Request) with invalid index
    @Test
    void testOperateFailureWithInvalidIndex() throws Exception {
        mockMvc.perform(post("/cube/operate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"axis\": \"Y\", \"direction\": 1, \"index\": 4}"))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    // Test: Redo - Success
    @Test
    void testRedoSuccess() throws Exception {
        mockMvc.perform(post("/cube/operate")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"axis\": \"X\", \"direction\": 1, \"index\": 1}"))
            .andExpect(status().isNoContent()); 

        mockMvc.perform(put("/cube/undo"))
                .andExpect(status().isNoContent()); 

        mockMvc.perform(put("/cube/redo"))
                .andExpect(status().isNoContent()); // 204 No Content

        verify(cubeService, times(1)).redo();
    }

    // Test: Redo - Failure (Bad Request)
    @Test
    void testRedoFailure() throws Exception {
        doThrow(new OperationException("Redo failed")).when(cubeService).redo();

        mockMvc.perform(put("/cube/redo"))
                .andExpect(status().isBadRequest()); // 400 Bad Request

        verify(cubeService, times(1)).redo();
    }

    // Test: Undo - Success
    @Test
void testUndoSuccessWithPreviousOperation() throws Exception {
    mockMvc.perform(post("/cube/operate")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"axis\": \"X\", \"direction\": 1, \"index\": 1}"))
            .andExpect(status().isNoContent()); 

    mockMvc.perform(put("/cube/undo"))
            .andExpect(status().isNoContent()); 

    verify(cubeService, times(1)).undo(); 
}

    // Test: Undo - Failure (Bad Request)
    @Test
    void testUndoFailure() throws Exception {
        doThrow(new OperationException("Undo failed")).when(cubeService).undo();

        mockMvc.perform(put("/cube/undo"))
                .andExpect(status().isBadRequest()); // 400 Bad Request

        verify(cubeService, times(1)).undo();
    }
}
