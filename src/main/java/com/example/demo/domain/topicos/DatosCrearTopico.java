package com.example.demo.domain.topicos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DatosCrearTopico(
        @NotNull(message = "Usuario es obligatorio")
        Long idUsuario,
        @NotNull(message = "Título es obligatorio")
        String titulo,
        @NotNull(message = "Mensaje es obligatorio")
        String mensaje,
        @NotNull(message = "Nombre de curso es obligatorio")
        String nombreCurso,
        @NotNull(message = "Estado de tópico es obligatorio")
        @Valid
        Estado status
) {
}
