package com.example.demo.controller;

import com.example.demo.domain.topicos.*;
import com.example.demo.domain.usuarios.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {
    @Autowired
    private CrudTopicoService crudTopicoService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    @Transactional
    @Operation(
            summary = "Crea un tópico",
            description = "Requiere autor, título...",
            tags = {"post"}
    )
    public ResponseEntity crear(@RequestBody @Valid DatosCrearTopico datos,
                                UriComponentsBuilder uriComponentsBuilder){
        var response = crudTopicoService.crear(datos);

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(url).body(datos);

    }

    @GetMapping
    @Operation(
            summary = "Devuelve todos los tópicos",
            description = "Trae todos los tópicos de la base de datos ordenados por fecha.",
            tags = {"get"}
    )
    public ResponseEntity<Page<DatosListadoTopico>> listar(@PageableDefault(size=10, sort = "fecha", direction = Sort.Direction.ASC) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAll(paginacion).map(DatosListadoTopico::new));
    }

    @GetMapping("/filtrar")
    @Operation(
            summary = "Devuelve todos los tópicos ordenados por curso",
            description = "Trae todos los tópicos de la base de datos ordenados por curso.",
            tags = {"get"}
    )
    public ResponseEntity<Page<DatosListadoTopico>> listarPorNombreDeCurso(@PageableDefault(size=10, sort = "nombreCurso", direction = Sort.Direction.ASC) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAll(paginacion).map(DatosListadoTopico::new));
    }

    @GetMapping("/filtrar2")
    @Operation(
            summary = "Devuelve todos los tópicos ordenados por año",
            description = "Trae todos los tópicos de la base de datos ordenados por curso.",
            tags = {"get"}
    )
    public ResponseEntity<Page<DatosListadoTopico>> listarPorAnio(
            @RequestParam int year,
            @PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.ASC) Pageable paginacion) {
        Page<Topico> topicos = topicoRepository.findByFechaYear(year, paginacion);
        Page<DatosListadoTopico> datosListadoTopicos = topicos.map(DatosListadoTopico::new);
        return ResponseEntity.ok(datosListadoTopicos);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Devuelve un tópico por id",
            description = "Trae el tópico indicado.",
            tags = {"getById"}
    )
    public ResponseEntity<DatosRespuestaTopico> retornaDatosTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        if(topico!=null){
            throw new ValidationException("Id de tópico no existe en la base de datos.");
        }
        var datosTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                topico.getAutorx().getUsername(),
                topico.getNombreCurso(),
                topico.getFecha());
        return ResponseEntity.ok(datosTopico);
    }

    @PutMapping
    @Transactional
    @Operation(
            summary = "Actualiza un tópico",
            description = "Permite actualizar título, mensaje y/o nombre del curso",
            tags = {"put"}
    )
    public ResponseEntity actualizarTopico(@RequestBody @Valid DatosActualizarTopico datosActualizarTopico){
        Boolean topico = Optional.of(topicoRepository.getReferenceById(datosActualizarTopico.id())).isPresent();
        if(!topico){
            throw new ValidationException("El id del tópico no existe en la base de datos");
        }
        Topico topic = topicoRepository.getReferenceById(datosActualizarTopico.id());
        topic.actualizarDatos(datosActualizarTopico);
        return ResponseEntity.ok(
                new DatosRespuestaTopico(
                        topic.getId(),
                        topic.getTitulo(),
                        topic.getMensaje(),
                        topic.getStatus(),
                        topic.getAutorx().getUsername(),
                        topic.getNombreCurso(),
                        topic.getFecha()
                )
        );
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    @Operation(
            summary = "Cancela un tópico",
            tags = {"delete"}
    )
    public ResponseEntity eliminar(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        if(topico!=null){
            throw new ValidationException("Id de tópico no existe en la base de datos.");
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
