/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.negocio;

import com.easytasks.dataTransferObjects.*;
import com.easytasks.negocio.excepciones.ExisteEntidadException;
import com.easytasks.negocio.excepciones.NoExisteEntidadException;
import com.easytasks.persistencia.entidades.*;
import com.easytasks.persistencia.persistencia.PersistenciaSBLocal;
import com.easytasks.persistencia.transformadores.TransformadorADtoSB;
import com.easytasks.persistencia.transformadores.TransformadorAEntidadSB;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

/**
 *
 * @author alumnoFI
 */
@Stateless
public class ABMUsuariosSB implements ABMUsuariosSBLocal {

    @EJB
    private PersistenciaSBLocal persistencia;

    @EJB
    private TransformadorADtoSB aDtoSB;

    @EJB
    private TransformadorAEntidadSB aEntidadSB;

    @Override
    public String chequeoDeVida() {
        return "estoy vivo";
    }

    @Override
    public void agregarUsuario(DtoUsuario dtoU) throws ExisteEntidadException {
        try {
            Usuario u = aEntidadSB.transformarUsuario(dtoU);
            persistencia.agregarUsuario(u);
        } catch (EntityExistsException e) {
            throw new ExisteEntidadException();
        } catch (PersistenceException p) {
            throw new ExisteEntidadException();
        } catch (Exception e) {
            throw new ExisteEntidadException();
        }

    }

    @Override
    public void modificarUsuario(DtoUsuario dtoU) throws NoExisteEntidadException {

        Usuario u2;
        try {
            Usuario u = aEntidadSB.transformarUsuario(dtoU);
            try {
                u2 = persistencia.buscarUsuario(dtoU.getNombreUsuario());
            } catch (EJBException e) {
                throw new NoExisteEntidadException(e.getMessage(), e);
            }
            Long id = u2.getId();
            u.setId(id);

            try {
                persistencia.modificarUsuario(u);

            } catch (Exception e) {
                throw new NoExisteEntidadException("WTF", e);
            }
        } catch (NoResultException e) {
            throw new NoExisteEntidadException();
        }

    }

    @Override
    public void borrarUsuario(String nombreUsuario) throws NoExisteEntidadException {
        try {
            Usuario u = persistencia.buscarUsuario(nombreUsuario);
            persistencia.borrarUsuario(u);
        } catch (EntityNotFoundException e) {
            throw new NoExisteEntidadException();
        }
    }

    @Override
    public void agregarContacto(String usuario, String contacto) throws NoExisteEntidadException {
        try {
            Usuario u = persistencia.buscarUsuario(usuario);
            Usuario c = persistencia.buscarUsuario(contacto);
            u.getContactos().add(c);
            persistencia.modificarUsuario(u);
        } catch (EntityNotFoundException e) {
            throw new NoExisteEntidadException();
        }
    }

    @Override
    public DtoUsuario buscarUsuario(String nombreusuario) throws NoExisteEntidadException {
        try {
            DtoUsuario dto = aDtoSB.transformarUsuario(persistencia.buscarUsuario(nombreusuario));
            return dto;
        } catch (EntityNotFoundException e) {
            throw new NoExisteEntidadException();
        }
    }

    @Override
    public String login(String username, String password) throws ExisteEntidadException, NoExisteEntidadException {
        String token = "";
        try {
            Usuario u = persistencia.buscarUsuario(username);
            if (u.getContraseña().equals(password)) {
                Calendar c = Calendar.getInstance();
                token = UUID.randomUUID().toString() + "-" + c.getTimeInMillis();
                Token t = new Token();
                t.setToken(token);
                persistencia.agregarToken(t);
            }
            return token;
        } catch (EJBException e) {
            throw new NoExisteEntidadException("El nombre de usuario no es correcto");
        } catch (PersistenceException p) {
            throw new ExisteEntidadException("Tuvimos un error en nuestra base de datos, por favor intente nuevamente");
        }
    }

    @Override
    public void logout(String token) throws NoExisteEntidadException {
        try {
            Token t = persistencia.buscarToken(token);
            persistencia.borrarToken(t);
        } catch (EJBException | EntityNotFoundException e) {//Cuando tira NoResultException, el Bean de peristencia tira esta excepción, por lo que es necesario capturarla aca.
            throw new NoExisteEntidadException("Ha ocurrido un error, por favor intente nuevamente");
        }
    }
    
    @Override
    public boolean estaLogueado(String token){
        try{
            Token t = persistencia.buscarToken(token);
            return true;
        }catch(EJBException e){
            return false;
        }
    }
}
