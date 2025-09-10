package org.example.assistantonsbservlet.api.chemistry;

import org.example.assistantonsbservlet.chemistry.PubChemFdaFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FoodAdditiveSubstanceControllerTest {
    private AutoCloseable mockCloser;

    @Mock
    private PubChemFdaFacade facadeMock;
    private FoodAdditiveSubstanceController controller;

    @BeforeEach
    void setUp() {
        mockCloser = MockitoAnnotations.openMocks(this);
        controller = new FoodAdditiveSubstanceController(facadeMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockCloser.close();
    }

    @Test
    void testGetAllAndNoData() {
        // given
        Mockito.when(facadeMock.getAll(1, 50))
            .thenReturn(List.of());

        // when
        final var responseEntity = controller.getAll(1, 50);

        // then
        Mockito.verify(facadeMock, Mockito.only()).getAll(1, 50);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        final var body = responseEntity.getBody();
        assertNotNull(body);
        assertTrue(body.isEmpty());
    }
}
