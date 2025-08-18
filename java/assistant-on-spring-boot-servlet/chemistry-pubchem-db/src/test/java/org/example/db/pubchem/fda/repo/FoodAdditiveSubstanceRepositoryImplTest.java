package org.example.db.pubchem.fda.repo;

import org.example.db.pubchem.fda.model.FoodAdditiveSubstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FoodAdditiveSubstanceRepositoryImplTest {
    private AutoCloseable mockCloser;

    @Mock
    private EntityManager emMock;
    @Mock
    private TypedQuery<FoodAdditiveSubstance> additiveSubstanceTypedQuery;

    private FoodAdditiveSubstanceRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        mockCloser = MockitoAnnotations.openMocks(this);
        repository = new FoodAdditiveSubstanceRepositoryImpl(emMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockCloser.close();
    }

    @Test
    void testFindAllAndNoData() {
        // given
        Mockito.when(emMock.createQuery(Mockito.anyString(), Mockito.eq(FoodAdditiveSubstance.class)))
            .thenReturn(additiveSubstanceTypedQuery);

        Mockito.when(additiveSubstanceTypedQuery.setFirstResult(Mockito.anyInt()))
            .thenReturn(additiveSubstanceTypedQuery);
        Mockito.when(additiveSubstanceTypedQuery.setMaxResults(Mockito.anyInt()))
            .thenReturn(additiveSubstanceTypedQuery);

        // when
        final var dataList = repository.findAll(0, 50);

        // then
        Mockito.verify(emMock, Mockito.only()).createQuery(
            "SELECT f FROM " + FoodAdditiveSubstance.class.getSimpleName() + " f",
            FoodAdditiveSubstance.class
        );
        assertNotNull(dataList);
        assertTrue(dataList.isEmpty());
    }
}
