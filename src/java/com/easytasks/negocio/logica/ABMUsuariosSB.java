/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.negocio.logica;

import com.easytasks.dataTransferObjects.*;
import com.easytasks.negocio.excepciones.EntidadModificadaIncorrectamenteException;
import com.easytasks.negocio.excepciones.EntidadNoCreadaCorrectamenteException;
import com.easytasks.negocio.excepciones.ExisteEntidadException;
import com.easytasks.negocio.excepciones.NoExisteEntidadException;
import com.easytasks.persistencia.entidades.*;
import com.easytasks.persistencia.persistencia.PersistenciaSBLocal;
import com.easytasks.negocio.transformadores.TransformadorADtoSBLocal;
import com.easytasks.negocio.transformadores.TransformadorAEntidadSBLocal;
import com.easytasks.social.interfaces.SocialSBLocal;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
    private TransformadorADtoSBLocal aDtoSB;

    @EJB
    private TransformadorAEntidadSBLocal aEntidadSB;

    @EJB(beanName = "TwitterSB")//Estamos obligados a setear el bean por defecto
    private SocialSBLocal social;

    @Override
    public void agregarUsuario(DtoUsuario dtoU) throws ExisteEntidadException, EntidadNoCreadaCorrectamenteException {
        if (dtoU.getContactos().size() > 0) {
            throw new EntidadNoCreadaCorrectamenteException("No se puede agregar un usuario con contactos. Utilice la función correspondiente");
        } else {
            try {
                Usuario u = aEntidadSB.transformarUsuario(dtoU);
                persistencia.agregarUsuario(u);
            } catch (EntityExistsException e) {
                throw new ExisteEntidadException("Ya existe un usuario con ese nombre de usuario.", e);
            } catch (PersistenceException e) {
                throw new ExisteEntidadException("Ocurrió un problema al agregar el usuario. Por favor intente nuevamente", e);
            } catch (Exception e) {
                throw new ExisteEntidadException("Ocurrió un problema inesperado al agregar el usuario. Por favor intente nuevamente", e);
            }
        }
    }

    @Override
    public void modificarUsuario(DtoUsuario dtoU) throws NoExisteEntidadException, EntidadModificadaIncorrectamenteException {
        if (dtoU.getContactos().size() > 0) {
            throw new EntidadModificadaIncorrectamenteException("no se puede modificar los contactos en un usuario. Utilice la funcion correspondiente");
        } else {
            Usuario u2;
            try {
                Usuario u = aEntidadSB.transformarUsuario(dtoU);
                try {
                    u2 = persistencia.buscarUsuario(dtoU.getNombreUsuario());
                    List<Usuario> listaContactos = persistencia.buscarContactos(u2);
                    u.setContactos(listaContactos);
                } catch (EJBException e) {
                    throw new NoExisteEntidadException(e.getMessage(), e);
                }
                Long id = u2.getId();
                u.setId(id);

                try {
                    persistencia.modificarUsuario(u);

                } catch (Exception e) {
                    throw new NoExisteEntidadException("No se pudo modificar el usuario. Por favor intente nuevamente", e);
                }
            } catch (NoResultException e) {
                throw new NoExisteEntidadException("No se encontró el usuario a modificar", e);
            }
        }
    }

    @Override
    public void borrarUsuario(String nombreUsuario) throws NoExisteEntidadException {
        try {
            Usuario u = persistencia.buscarUsuario(nombreUsuario);
            if(u!=null){
            persistencia.borrarUsuario(u);
            }else{
                throw new NoExisteEntidadException("No se encontró el usuario a borrar");
            }
        } catch (EJBException | EntityNotFoundException e) {
            throw new NoExisteEntidadException("Ocurrió un error al borrar el usuario. Por favor intente nuevamente", e);
        }
    }

    @Override
    public void agregarContacto(String usuario, String contacto) throws NoExisteEntidadException, ExisteEntidadException, EntidadModificadaIncorrectamenteException {
        if (usuario.equals(contacto)) {
            throw new EntidadModificadaIncorrectamenteException("No puede agregarse a ud mismo como contacto.");
        }
        try {
            Usuario u = persistencia.buscarUsuario(usuario);
            Usuario c = persistencia.buscarUsuario(contacto);
            if (u.getContactos().contains(c)) {
                throw new ExisteEntidadException("Ya existe el contacto");
            }
            u.getContactos().add(c);
            persistencia.modificarUsuario(u);
        } catch (EntityNotFoundException e) {
            throw new NoExisteEntidadException("No se encontró el usuario.", e);
        } catch(EJBException e){
            throw new EntidadModificadaIncorrectamenteException("No se pudo agregar el contacto. Por favor, intente nuevamente", e);
        }
    }

    @Override
    public DtoUsuario buscarUsuario(String nombreusuario) throws NoExisteEntidadException {
        try {
            DtoUsuario dto = aDtoSB.transformarUsuario(persistencia.buscarUsuario(nombreusuario));
            return dto;
        } catch (EntityNotFoundException e) {
            throw new NoExisteEntidadException("No se encontró el usuario");
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
                t.setUsuario(u);
                persistencia.agregarToken(t);
            }
            else{
                throw new ExisteEntidadException("La contraseña ingresada no corresponde con el nombre de usuario. Por favor intente nuevamente");
            }
            return token;
        } catch (EJBException e) {
            throw new NoExisteEntidadException("El nombre de usuario no es correcto", e);
        } catch (PersistenceException p) {//Se da cuando no se puede agregar el token
            throw new ExisteEntidadException("Tuvimos un error en nuestra base de datos, por favor intente nuevamente", p);
        }
    }

    @Override
    public void logout(String token) throws NoExisteEntidadException {
        try {
            Token t = persistencia.buscarToken(token);
            persistencia.borrarToken(t);
        } catch (EJBException | EntityNotFoundException e) {
            throw new NoExisteEntidadException("Ha ocurrido un error, por favor intente nuevamente");
        }
    }

    @Override
    public boolean estaLogueado(String token, String username) {
        try {
            Token t = persistencia.buscarToken(token);
            return t.getUsuario().getNombreUsuario().equals(username);
        } catch (EJBException e) {
            return false;
        }
    }

    @Override
    public String conectar(String nombreUsuario, String redSocial) {
        try {
            social = (SocialSBLocal) InitialContext.doLookup("java:global/EasyTasks/EasyTasksSocial/" + redSocial + "SB");
        } catch (NamingException ex) {
            return "Error en la conexion con la red social. Parece no estar disponible para nuestro sistema";
        }
        return social.connect(nombreUsuario);
    }

    @Override
    public void ingresarPin(String nombreUsuario, String pin){
        
        social.ingresarPin(nombreUsuario, pin);
        postear(nombreUsuario, pin, "He conectado mi cuenta a Easy Tasks! La mejor manera de administrar mis tareas");
        

    }

    @Override
    public void postear(String nombreUsuario, String post, 
            String redSocial){
        try {
            social = (SocialSBLocal) InitialContext.doLookup("java:global/EasyTasks/EasyTasksSocial/" + redSocial + "SB");
            social.post(nombreUsuario, post);
        } catch (NamingException ex) {
           //No hay nada que podamos hacer para corregirlo.
        }
        
    }
    
    @Override
    public String desconectar(String nombreUsuario, String redSocial) {
        
        try {
            social = (SocialSBLocal) InitialContext.doLookup("java:global/EasyTasks/EasyTasksSocial/" + redSocial + "SB");
        } catch (NamingException ex) {
            return "Error al querer desconectar " + redSocial + " del sistema";
        }
        return social.disconnect(nombreUsuario);
        
    }
    
    
}
