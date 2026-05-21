package org.example.proyectooo.Service;

import org.example.proyectooo.Domain.Usuario;
import org.example.proyectooo.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    private boolean verifyPassword(String rawPassword, String storedPassword) {
        return storedPassword != null && storedPassword.equals(rawPassword);
    }

    public Usuario authenticate(String username, String password) {
        Usuario usuario = usuarioRepository.findByNombre(username);
        if (usuario != null && verifyPassword(password, usuario.getPassword())) {
            return usuario;
        }
        return null;
    }

    @Transactional
    public Usuario registerUser(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
        }

        if (usuarioRepository.findByNombre(usuario.getNombre()) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya existe.");
        }


        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("EMPLEADO");
        }

        return usuarioRepository.save(usuario);
    }
}