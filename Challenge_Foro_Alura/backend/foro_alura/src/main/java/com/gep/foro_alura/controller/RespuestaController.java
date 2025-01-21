package com.gep.foro_alura.controller;


import com.gep.foro_alura.domain.respuesta.*;
import com.gep.foro_alura.domain.respuesta.validaciones.actualizar.ValidarRespuestaActualizada;
import com.gep.foro_alura.domain.respuesta.validaciones.crear.ValidarRespuestaCreada;
import com.gep.foro_alura.domain.topico.Estado;
import com.gep.foro_alura.domain.topico.Topico;
import com.gep.foro_alura.domain.topico.TopicoRepository;
import com.gep.foro_alura.domain.usuario.Usuario;
import com.gep.foro_alura.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Respuesta", description = "Sólamente una puede ser la respuesta correcta de un tema.")
public class RespuestaController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    List<ValidarRespuestaCreada> crearValidadores;

    @Autowired
    List<ValidarRespuestaActualizada> actualizarValidadores;

    @PostMapping
    @Transactional
    @Operation(summary = "Guardamos una nueva respuesta en la base de datos, vinculada a un usuario y tema.")
    public ResponseEntity<DetalleRespuestaDTO> crearRespuesta(@RequestBody @Valid CrearRespuestaDTO crearRespuestaDTO, UriComponentsBuilder uriBuilder){

        crearValidadores.forEach(v -> v.validate(crearRespuestaDTO));

        Usuario usuario = usuarioRepository.getReferenceById(crearRespuestaDTO.usuarioId());
        Topico topico = topicoRepository.findById(crearRespuestaDTO.topicoId()).get();

        var respuesta = new Respuesta(crearRespuestaDTO, usuario, topico);
        respuestaRepository.save(respuesta);

        var uri = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalleRespuestaDTO(respuesta));

    }


    @GetMapping("/topico/{topicoId}")
    @Operation(summary = "Lista todas las respuestas de un topico en especifico")
    public ResponseEntity<Page<DetalleRespuestaDTO>> leerRespuestaDeTopico(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Direction.ASC)Pageable pageable, @PathVariable Long topicoId){
        var pagina = respuestaRepository.findAllByTopicoId(topicoId, pageable).map(DetalleRespuestaDTO::new);
       return ResponseEntity.ok(pagina);
    }

    @GetMapping("/usuario/{nombreUsuario}")
    @Operation(summary = "Muestra todas las respuestas de un usuario especificado por su nombre.")
    public ResponseEntity<Page<DetalleRespuestaDTO>> leerRespuestasDeUsuarios(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Direction.ASC) Pageable pageable, @PathVariable Long usuarioId){
        var pagina = respuestaRepository.findAllByUsuarioId(usuarioId, pageable).map(DetalleRespuestaDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Devuelve una unica respuesta especificada por su ID")
    public ResponseEntity<DetalleRespuestaDTO> leerUnaRespuesta(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceById(id);

        var datosRespuesta = new DetalleRespuestaDTO(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUltimaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getBorrado(),
                respuesta.getUsuario().getId(),
                respuesta.getUsuario().getUsername(),
                respuesta.getTopico().getId(),
                respuesta.getTopico().getTitulo()
        );
        return ResponseEntity.ok(datosRespuesta);
    }


    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza el estado, el mensaje de la solucion de la respuesta.")
    public ResponseEntity<DetalleRespuestaDTO> actualizarRespuesta(@RequestBody @Valid ActualizarRespuestaDTO actualizarRespuestaDTO, @PathVariable Long id){
        actualizarValidadores.forEach(v -> v.validate(actualizarRespuestaDTO, id));
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.actualizarRespuesta(actualizarRespuestaDTO);

        if(actualizarRespuestaDTO.solucion()){
            var temaResuelto = topicoRepository.getReferenceById(respuesta.getTopico().getId());
            temaResuelto.setEstado(Estado.CLOSED);
        }

        var datosRespuesta = new DetalleRespuestaDTO(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUltimaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getBorrado(),
                respuesta.getUsuario().getId(),
                respuesta.getUsuario().getUsername(),
                respuesta.getTopico().getId(),
                respuesta.getTopico().getTitulo()
        );
        return ResponseEntity.ok(datosRespuesta);
    }


    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Eliminamos respuestas por su Id")
    public ResponseEntity<?> borrarRespuesta(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.eliminarRespuesta();
        return ResponseEntity.noContent().build();
    }

}



