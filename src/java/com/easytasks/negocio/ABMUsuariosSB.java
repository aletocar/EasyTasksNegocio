/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.negocio;

import com.easytasks.dataTransferObjects.*;
import com.easytasks.negocio.excepciones.ExisteEntidadException;
import com.easytasks.persistencia.entidades.*;
import com.easytasks.persistencia.persistencia.PersistenciaSB;
import com.easytasks.persistencia.persistencia.PersistenciaSBLocal;
import com.easytasks.persistencia.transformadores.TransformadorADtoSB;
import com.easytasks.persistencia.transformadores.TransformadorAEntidadSB;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
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
    public void agregarUsuario(DtoUsuario dtoU) throws ExisteEntidadException{
        try {
            Usuario u = aEntidadSB.transformarUsuario(dtoU);
            persistencia.agregarUsuario(u);
        } catch (EntityExistsException e) {
              throw new ExisteEntidadException();
        }
        catch (PersistenceException p) {
              throw new ExisteEntidadException();
        }
        catch (Exception e) {
              throw new ExisteEntidadException();
        }

    }

    @Override
    public void modificarUsuario(DtoUsuario dtoU) {
        Usuario u = aEntidadSB.transformarUsuario(dtoU);
        u.setId(persistencia.buscarUsuario(dtoU.getNombreUsuario()).getId());
        persistencia.modificarUsuario(u);
    }

    @Override
    public void borrarUsuario(String nombreUsuario) {
        Usuario u = persistencia.buscarUsuario(nombreUsuario);
        persistencia.borrarUsuario(u);
    }

    @Override
    public void agregarContacto(String usuario, String contacto) {
        Usuario u = persistencia.buscarUsuario(usuario);
        Usuario c = persistencia.buscarUsuario(contacto);
        u.getContactos().add(c);
        persistencia.modificarUsuario(u);
    }

    @Override
    public DtoUsuario buscarUsuario(String nombreusuario) {
        DtoUsuario dto = aDtoSB.transformarUsuario(persistencia.buscarUsuario(nombreusuario));
        return dto;
    }
}
