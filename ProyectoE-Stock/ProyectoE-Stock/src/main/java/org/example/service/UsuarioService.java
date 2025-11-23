package org.example.service;

import org.example.config.HibernateUtil;
import org.example.dao.UsuarioDao;
import org.example.dao.UsuarioDaoImpl;
import org.example.domain.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UsuarioService {
    private final SessionFactory ss;
    private final UsuarioDao usuarioDAO;

    public UsuarioService() {
        this.ss = HibernateUtil.getSessionFactory();
        this.usuarioDAO = new UsuarioDaoImpl();
    }

    private boolean verifyPassword(String rawPassword, String storedPassword) {
        return storedPassword != null && storedPassword.equals(rawPassword);
    }
    public Usuario registerUser(Usuario usuario){

        try(Session session = ss.getCurrentSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                String nombreUsuario = usuario.getNombre();
                if (nombreUsuario == null || nombreUsuario.isEmpty()) {
                    throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
                }

                if (usuarioDAO.findByUsername(session, nombreUsuario) != null) {
                    throw new IllegalArgumentException("El nombre de usuario ya existe.");
                }

                usuarioDAO.create(session, usuario);
                tx.commit();

                return usuario;
            } catch (RuntimeException e){
                if (tx != null) tx.rollback();
                throw e;
            }

        }
    }
    public Usuario authenticate(String username, String password) {
        try (Session session = ss.getCurrentSession()) {
            Usuario usuario = usuarioDAO.findByUsername(session, username);

            if (usuario != null && verifyPassword(password, usuario.getPassword())) {
                return usuario; // contraseña correcta
            }

            return null; // usuario no encontrado o contraseña incorrecta
        }
    }
    public Usuario findById(Long id){
        try(Session session = ss.getCurrentSession()) {
            return usuarioDAO.findById(session,id);

        }
    }


}
