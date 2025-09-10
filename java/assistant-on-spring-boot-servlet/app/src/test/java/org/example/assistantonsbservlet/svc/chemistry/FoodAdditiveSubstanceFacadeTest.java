package org.example.assistantonsbservlet.svc.chemistry;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.assistantonsbservlet.api.chemistry.model.FoodAdditiveSubstanceResponseDto;
import org.example.assistantonsbservlet.chemistry.PubChemFdaFacade;
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
import org.springframework.context.ApplicationContext;
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
    private ApplicationContext appContextMock;
    @Mock
    private EntityManagerFactory emfMock;
    @Mock
    private EntityManager emMock;
    @Mock
    private EntityTransaction txMock;

    @Mock
    private FoodAdditiveSubstanceRepository repositoryMock;

    private PubChemFdaFacade facade;

    @BeforeEach
    void setUp() {
        mockCloser = MockitoAnnotations.openMocks(this);
        ((DefaultConversionService) conversionService).addConverter(new FoodAdditiveSubstanceToDtoListConverter());

        Mockito.when(appContextMock.getBean(
                Mockito.eq(FoodAdditiveSubstanceRepository.class), Mockito.any(EntityManager.class)
            ))
            .thenReturn(repositoryMock);
        Mockito.when(appContextMock.getBean("appConversionService", ConversionService.class))
            .thenReturn(conversionService);

        facade = new PubChemFdaFacade(appContextMock);
        facade.setEmf(emfMock);

        Mockito.when(emfMock.createEntityManager()).thenReturn(emMock);
        Mockito.when(emMock.getTransaction()).thenReturn(txMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockCloser.close();
    }

    @Test
    void testGetAllAndNoData() {
        // given
        Mockito.when(repositoryMock.findAll(0, 50))
            .thenReturn(List.of());

        // when
        final var dataList = facade.getAll(1, 50);

        // then
        Mockito.verify(emfMock, Mockito.only()).createEntityManager();
        Mockito.verify(emMock, Mockito.atMostOnce()).getTransaction();
        Mockito.verify(txMock, Mockito.atMostOnce()).begin();
        Mockito.verify(txMock, Mockito.atMostOnce()).commit();
        Mockito.verify(repositoryMock, Mockito.only()).findAll(0, 50);
        Mockito.verify(conversionService, Mockito.never()).convert(
            Mockito.any(), Mockito.eq(FoodAdditiveSubstanceResponseDto.class)
        );
        assertNotNull(dataList);
        assertTrue(dataList.isEmpty());
    }
}
