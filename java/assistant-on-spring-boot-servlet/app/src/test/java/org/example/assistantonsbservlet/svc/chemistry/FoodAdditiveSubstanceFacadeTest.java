package org.example.assistantonsbservlet.svc.chemistry;

import org.example.assistantonsbservlet.api.chemistry.organic.dbs.pubchem.fda.model.FoodAdditiveSubstanceResponseDto;
import org.example.assistantonsbservlet.convert.FoodAdditiveSubstanceToDtoListConverter;
import org.example.db.pubchem.fda.repo.FoodAdditiveSubstanceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FoodAdditiveSubstanceFacadeTest {
    private AutoCloseable mockCloser;

    @Spy
    private ConversionService conversionService = new DefaultConversionService();

    @Mock
    private FoodAdditiveSubstanceRepository repositoryMock;

    private PubChemFdaFacade facade;

    @BeforeEach
    void setUp() {
        mockCloser = MockitoAnnotations.openMocks(this);
        ((DefaultConversionService) conversionService).addConverter(new FoodAdditiveSubstanceToDtoListConverter());
        facade = new PubChemFdaFacade(repositoryMock, conversionService);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockCloser.close();
    }

    @Test
    void testGetAllAndNoData() {
        // given
        Mockito.when(repositoryMock.findAll(1, 50))
            .thenReturn(List.of());

        // when
        final var dataList = facade.getAll(1, 50);

        // then
        Mockito.verify(repositoryMock, Mockito.only()).findAll(1, 50);
        Mockito.verify(conversionService, Mockito.never()).convert(
            Mockito.any(), Mockito.eq(FoodAdditiveSubstanceResponseDto.class)
        );
        assertNotNull(dataList);
        assertTrue(dataList.isEmpty());
    }
}
