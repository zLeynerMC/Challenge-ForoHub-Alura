package com.gep.foro_alura.controller;


import com.gep.foro_alura.domain.usuario.*;
import com.gep.foro_alura.domain.usuario.validaciones.actualizar.ValidarActualizarUsuario;
import com.gep.foro_alura.domain.usuario.validaciones.crear.ValidarCrearUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuario", description = "Crear topicos y se encarga de publicar respuestas")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    List<ValidarCrearUsuario> crearValidador;

    @Autowired
    List<ValidarActualizarUsuario> actualizarValidador;

    @PostMapping
    @Transactional
    @Operation(summary = "Agregamos nuevos usuarios en la BD")
    public ResponseEntity<DetallesUsuarioDTO> crearUsuario(@RequestBody @Valid CrearUsuarioDTO crearUsuarioDTO, UriComponentsBuilder uriBuilder){
        crearValidador.forEach(v -> v.validate(crearUsuarioDTO));

        String hashedPassword = passwordEncoder.encode(crearUsuarioDTO.password());
        Usuario usuario = new Usuario(crearUsuarioDTO, hashedPassword);

        repository.save(usuario);
        var uri = uriBuilder.path("/usuarios/{username}").buildAndExpand(usuario.getUsername()).toUri();
        return ResponseEntity.created(uri).body(new DetallesUsuarioDTO(usuario));
    }

    @GetMapping("/all")
    @Operation(summary = "Enumera todos los usuarios independientemente de su estado")
    public ResponseEntity<Page<DetallesUsuarioDTO>> leerTodosUsuarios(@PageableDefault(size = 5, sort = {"id"})Pageable pageable){
        var pagina = repository.findAll(pageable).map(DetallesUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista solo usuarios habilitados")
    public ResponseEntity<Page<DetallesUsuarioDTO>> leerUsuariosActivos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable){
        var pagina = repository.findAllByEnabledTrue(pageable).map(DetallesUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Lee un usuario por su nombre de usuario")
    public ResponseEntity<DetallesUsuarioDTO> leerUnUsuario(@PathVariable String username){
        Usuario usuario = (Usuario) repository.findByUsername(username);
        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Lee un unico usuario por su ID")
    public ResponseEntity<DetallesUsuarioDTO>leerUnUsuario(@PathVariable Long id){
        Usuario usuario = repository.getReferenceById(id);
        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @PutMapping("/{username}")
    @Transactional
    @Operation(summary = "Actualiza los datos del usuario incluyendo su estado")
    public ResponseEntity<DetallesUsuarioDTO> actualizarUsuario(@RequestBody @Valid ActualizarUsuarioDTO actualizarUsuarioDTO, @PathVariable String username){
        actualizarValidador.forEach(v -> v.validate(actualizarUsuarioDTO));

        Usuario usuario = (Usuario) repository.findByUsername(username);

        if (actualizarUsuarioDTO.password() != null){
            String hashedPassword = passwordEncoder.encode(actualizarUsuarioDTO.password());
            usuario.actualizarUsuarioConPassword(actualizarUsuarioDTO, hashedPassword);

        }else {
            usuario.actualizarUsuario(actualizarUsuarioDTO);
        }

        var datosUsuario = new DetallesUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @DeleteMapping("/{username}")
    @Transactional
    @Operation(summary = "Deshabilita a un usuario por su id")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String username){
        Usuario usuario = (Usuario) repository.findByUsername(username);
        usuario.eliminarUsuario();
        return ResponseEntity.noContent().build();
    }


}
