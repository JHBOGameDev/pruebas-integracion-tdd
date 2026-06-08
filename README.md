# Pruebas de Integración y Sistema

Taller de pruebas de integración y sistema para el curso
Testing y Validación de Software - Maestría en Ingeniería de Software,
Universidad de La Sabana.

## Integrantes
- Jose Haider Barreto Oliveros

## Dominio
Sistema de registro de votantes que valida: estado de vida,
rango de edad (18-120), unicidad del documento e ID válido.

## Tecnologías
- Java 21
- JUnit 4 + JUnit 5
- Mockito 5.12
- H2 Database 2.2.224
- Spring Boot 3.3.5
- JaCoCo 0.8.12
- Maven 3.9.11

## Estructura
```
src/
├── main/java/edu/unisabana/tyvs/registry/
│   ├── domain/model/
│   ├── application/usecase/ + port/out/
│   ├── infrastructure/persistence/
│   └── delivery/rest/
└── test/java/edu/unisabana/tyvs/registry/
    ├── application/usecase/
    └── delivery/rest/
```

## Ejecución
```bash
# Solo unitarias e integración
mvn test

# Todo incluyendo pruebas de sistema
mvn verify

# Reporte de cobertura
mvn clean verify jacoco:report
```

## Resultados
- 20 tests ejecutados, 0 fallos
- Cobertura global: 88%

## Documentación
Wiki del repositorio: https://github.com/JHBOGameDev/pruebas-integracion-tdd/wiki