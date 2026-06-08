package edu.unisabana.tyvs.registry.application.usecase;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;
import edu.unisabana.tyvs.registry.domain.model.Person;

import edu.unisabana.tyvs.registry.infrastructure.persistence.RegistryRecord;
import java.util.Optional;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementación falsa del repositorio usando HashMap en memoria.
 * No usa Mockito ni base de datos real.
 * Útil para pruebas rápidas y aisladas.
 */
public class FakeRegistryRepository implements RegistryRepositoryPort {

    private final Map<Integer, String> storage = new HashMap<>();

    @Override
    public boolean existsById(int id) {
        return storage.containsKey(id);
    }

    @Override
    public void save(int id, String name, int age, boolean alive) {
        storage.put(id, name);
    }

    @Override
    public Optional<RegistryRecord> findById(int id) {
        // Retorna vacío si no existe — implementación mínima
        return Optional.empty();
    }

    @Override
    public void initSchema() {
        // No necesita crear tabla — usa HashMap
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }
}