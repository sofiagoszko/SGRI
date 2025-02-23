package com.api.sgri.controller;

import java.io.IOException;
import java.util.List;

import com.api.sgri.dto.UsuarioUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.sgri.dto.AuthDTO;
import com.api.sgri.dto.UsuarioEmpresaDTO;
import com.api.sgri.exception.BadRequestException;
import com.api.sgri.exception.DuplicateUserException;
import com.api.sgri.exception.NotFoundException;
import com.api.sgri.exception.UnauthorizedException;
import com.api.sgri.mapper.UsuarioEmpresaMapper;
import com.api.sgri.model.UsuarioEmpresa;
import com.api.sgri.response.HttpBodyResponse;
import com.api.sgri.response.ResponseFactory;
import com.api.sgri.service.AuthService;
import com.api.sgri.service.UsuarioEmpresaService;

@RestController
@RequestMapping("api/usuario-empresa")
@CrossOrigin(origins = "*")
public class UsuarioEmpresaController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ResponseFactory responseFactory;

    @Autowired
    private UsuarioEmpresaService usuarioEmpresaService;

    @Autowired
    private UsuarioEmpresaMapper usuarioEmpresaMapper;


    @GetMapping("/")
    public ResponseEntity<Object> publicRoute() {
        return ResponseEntity.ok("Ruta publica...");
    }

    @GetMapping("/usuarios")
    public ResponseEntity<Object> getUsuarios(@RequestParam(required = false) String param) {
        try {
            List<UsuarioEmpresaDTO> usuarios = usuarioEmpresaService.getUsuarioEmpresas();

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Usuarios obtenidos con éxito")
                    .data(usuarios)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Object> getUsuario(@PathVariable Long id) {
        try {
            UsuarioEmpresaDTO usuario = usuarioEmpresaService.getUsuarioEmpresaById(id);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Usuario obtenido con éxito")
                    .data(usuario)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        } catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe usuario con id: " + id);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @GetMapping("/usuarios/email/{email}")
    public ResponseEntity<Object> getUsuarioByEmail(@PathVariable String email) throws NotFoundException {
        try {
            UsuarioEmpresaDTO usuario = usuarioEmpresaMapper.toDTO(usuarioEmpresaService.getUsuarioEmpresaByEmail(email));

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Usuario obtenido con éxito")
                    .data(usuario)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        } catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe usuario con email: " + email);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @PostMapping("/credenciales")
    public ResponseEntity<Object> loginByCredentials(@RequestBody AuthDTO authDTO) {

        String userName = authDTO.getUserName();
        String password = authDTO.getPassword();

        try {

            if (userName == null || password == null)
                throw new BadRequestException("usuario y password son requeridos");

            //genera el token JWT
            String authToken = authService.loginByCredentials(userName, password);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("usuario autenticado")
                    .data(authToken)
                    .userFriendlyMessage("login exitoso")
                    .build();

            return ResponseEntity
                    .status(data.getStatusCode())
                    .body(data);

        } catch (BadRequestException e) {
            return responseFactory.badRequest(e.getMessage());
        } catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe usuario con nombre de usuario: " + userName);
        } catch (UnauthorizedException e) {
            return responseFactory.unauthorizedError();
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @PostMapping("/registracion")
    public ResponseEntity<Object> createUsuario(@RequestBody UsuarioEmpresaDTO usuarioEmpresaDTO) {
        try {
            UsuarioEmpresa usuarioEmpresa = usuarioEmpresaService.crearUsuarioEmpresa(usuarioEmpresaDTO);


            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Se ha registrado el usuario")
                    .status("Success")
                    .statusCode(200)
                    .userFriendlyMessage("Se ha registrado el usuario")
                    .data(usuarioEmpresa)
                    .build();

            return ResponseEntity
                    .status(data.getStatusCode())
                    .body(data);

        }catch(DuplicateUserException e){
            return responseFactory.duplicateUserError();
        }catch (Exception e) {

            return ResponseEntity
                    .status(400)
                    .body("Ha ocurrido un error");
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Object> updateUsuario(@PathVariable Long id, @RequestBody UsuarioEmpresaDTO usuario) {
        try {
            UsuarioEmpresa usuarioEmpresa = usuarioEmpresaService.getUsuarioEmpresaByIdEntity(id);

            UsuarioEmpresaDTO usuarioActualizado = usuarioEmpresaService.updateUsuarioEmpresa(usuarioEmpresa, usuario);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Usuario actualizado con éxito")
                    .data(usuarioActualizado)
                    .build();

            return ResponseEntity.status(data.getStatusCode()).body(data);
        }catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe usuario con id: " + id);
        } catch (DuplicateUserException e) {
            return responseFactory.duplicateUserError();
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable Long id) {

        try {
            UsuarioEmpresa usuarioEliminado = usuarioEmpresaService.deleteUsuarioEmpresa(id);

            HttpBodyResponse data = new HttpBodyResponse.Builder()
                    .message("Usuario eliminado con éxito")
                    .data(usuarioEliminado)
                    .build();
    
            return ResponseEntity.status(data.getStatusCode()).body(data);
        } catch (NotFoundException e) {
            return responseFactory.errorNotFound("No existe usuario con id: " + id);
        } catch (Exception e) {
            return responseFactory.internalServerError();
        }
    }

    @PostMapping("/usuarios/{id}/fotoPerfil")
    public ResponseEntity<Object> actualizarFotoPerfil(@PathVariable Long id, 
                                                       @RequestParam("archivo") MultipartFile archivo) {
        try {
            // Actualizar la foto en el servicio
            UsuarioEmpresa usuarioActualizado = usuarioEmpresaService.actualizarFotoPerfil(id, archivo);
            // Convertir a DTO usando el mapper
            UsuarioEmpresaDTO usuarioDTO = usuarioEmpresaMapper.toDTO(usuarioActualizado);
            
            // Construir la respuesta usando el builder de HttpBodyResponse
            HttpBodyResponse response = new HttpBodyResponse.Builder()
                    .message("Foto de perfil actualizada correctamente")
                    .data(usuarioDTO)
                    .build();
            
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al guardar el archivo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }

}
