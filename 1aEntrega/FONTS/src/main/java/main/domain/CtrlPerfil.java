package main.domain;

import main.persistence.CtrlPersistencia;

import java.io.IOException;
import java.lang.IllegalStateException;

public class CtrlPerfil {

    private Perfil perfilCargado;

    public CtrlPerfil () {
        this.perfilCargado = null;
    }

    public Perfil getPerfilCargado() { return this.perfilCargado; }

    public void crearPerfil(String email, String nombre, String contrasena) {
        Perfil p = new Perfil(email, nombre, contrasena);
        CtrlPersistencia.getInstance().guardarPerfil(p);
        // Y carga el perfil creado si guardarPerfil no genera error
        this.perfilCargado = p;
    }

    public boolean existePerfil(String email) {
        try {
            Perfil p = CtrlPersistencia.getInstance().cargarPerfil(email);
            return p != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void cargarPerfil(String email) {
        this.perfilCargado = CtrlPersistencia.getInstance().cargarPerfil(email);
    }

    // Borra un perfil de la persistencia cualquiera
    public void borrarPerfil(String email) {
        CtrlPersistencia.getInstance().borrarPerfil(email);
    }

    // Borra el perfil cargado actualmente de la persistencia y lo desasigna
    public void borrarPerfilCargado() throws IllegalStateException {
        if (this.perfilCargado == null) throw new IllegalStateException("No hay ningun perfil cargado");
        CtrlPersistencia.getInstance().borrarPerfil(this.perfilCargado.getEmail());
        this.perfilCargado = null;
    }

    // Cada vez que se crea/borra una encuesta/respuesta, hay que guardar el cambio en la persistencia

    public void addEncuestaCreada (Encuesta enc) throws IllegalStateException {
        if (this.perfilCargado == null) throw new IllegalStateException("No hay ningun perfil cargado");
        else {
            try {
                this.perfilCargado.addEncuestaCreada(enc);
                CtrlPersistencia.getInstance().guardarPerfil(this.perfilCargado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.perfilCargado.removeEncuestaCreada(enc);
            }
        }
    }

    public void removeEncuestaCreada (Encuesta enc) throws IllegalStateException {
        if (this.perfilCargado == null) throw new IllegalStateException("No hay ningun perfil cargado");
        else {
            try {
                this.perfilCargado.removeEncuestaCreada(enc);
                CtrlPersistencia.getInstance().guardarPerfil(this.perfilCargado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.perfilCargado.addEncuestaCreada(enc);
            }
        }
    }

    public void addRespuestaHecha (RespuestaEncuesta res) throws IllegalStateException {
        if (this.perfilCargado == null) throw new IllegalStateException("No hay ningun perfil cargado");
        else {
            try {
                this.perfilCargado.addRespuestaHecha(res);
                CtrlPersistencia.getInstance().guardarPerfil(this.perfilCargado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.perfilCargado.removeRespuestaHecha(res);
            }
        }
    }

    public void removeRespuestaHecha (RespuestaEncuesta res) throws IllegalStateException {
        if (this.perfilCargado == null) throw new IllegalStateException("No hay ningun perfil cargado");
        else {
            try {
                this.perfilCargado.removeRespuestaHecha(res);
                CtrlPersistencia.getInstance().guardarPerfil(this.perfilCargado);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.perfilCargado.addRespuestaHecha(res);
            }
        }
    }
}
