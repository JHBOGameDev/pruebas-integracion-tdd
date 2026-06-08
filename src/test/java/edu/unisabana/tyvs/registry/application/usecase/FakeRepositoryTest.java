package edu.unisabana.tyvs.registry.application.usecase;

import edu.unisabana.tyvs.registry.domain.model.Gender;
import edu.unisabana.tyvs.registry.domain.model.Person;
import edu.unisabana.tyvs.registry.domain.model.RegisterResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Pruebas usando FakeRepository (HashMap) sin Mockito ni H2.
 * Demuestra que el caso de uso funciona con cualquier implementación
 * del puerto de persistencia.
 */
public class FakeRepositoryTest {

    private FakeRegistryRepository fakeRepo;
    private Registry registry;

    @Before
    public void setUp() {
        // Arrange común: usar repositorio falso
        fakeRepo = new FakeRegistryRepository();
        registry = new Registry(fakeRepo);
    }

    // Given: persona válida
    // When: intento registrarla con FakeRepository
    // Then: resultado debe ser VALID y debe existir en el fake
    @Test
    public void shouldRegisterValidPersonWithFakeRepo() throws Exception {
        // Arrange
        Person person = new Person("Ana", 200, 30, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.VALID, result);
        assertTrue(fakeRepo.existsById(200));
    }

    // Given: misma persona registrada dos veces con FakeRepository
    // When: intento registrarla por segunda vez
    // Then: resultado debe ser DUPLICATED
    @Test
    public void shouldReturnDuplicatedWithFakeRepo() throws Exception {
        // Arrange
        Person person = new Person("Carlos", 201, 25, Gender.MALE, true);
        registry.registerVoter(person);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.DUPLICATED, result);
    }

    // Given: FakeRepository limpio después de deleteAll
    // When: verifico si existe un ID previamente guardado
    // Then: no debe existir
    @Test
    public void shouldClearDataWithDeleteAll() throws Exception {
        // Arrange
        Person person = new Person("Maria", 202, 28, Gender.FEMALE, true);
        registry.registerVoter(person);

        // Act
        fakeRepo.deleteAll();

        // Assert
        assertFalse(fakeRepo.existsById(202));
    }
}