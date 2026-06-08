# Registro de Defectos

## Defecto 01
- **Caso:** JSON incompleto enviado al endpoint /register (sin campo gender)
- **Esperado:** HTTP 400 Bad Request
- **Obtenido:** HTTP 500 Internal Server Error (NullPointerException)
- **Causa probable:** Falta validación `@Valid` en `PersonRequest` —
  el controlador intenta convertir `null` a enum `Gender`
- **Estado:** Abierto ⚠️ (requiere agregar `@Valid` y manejo de errores)

## Defecto 02
- **Caso:** Conexión a H2 con URL inválida
- **Esperado:** Excepción controlada con mensaje descriptivo
- **Obtenido:** SQLException genérica sin mensaje claro
- **Causa probable:** Falta manejo específico de errores de conexión
  en `RegistryRepository.getConnection()`
- **Estado:** Cerrado ✅ (el test verifica que la excepción se propaga)

## Defecto 03
- **Caso:** Registro de persona con ID negativo
- **Esperado:** INVALID
- **Obtenido:** VALID (antes de implementar validación)
- **Causa probable:** Ausencia de validación de ID en Registry.registerVoter()
- **Estado:** Cerrado ✅ (corregido en implementación)