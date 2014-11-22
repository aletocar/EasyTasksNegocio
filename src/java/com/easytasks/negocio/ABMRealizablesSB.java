/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.easytasks.negocio;

import com.easytasks.dataTransferObjects.DtoProyecto;
import com.easytasks.dataTransferObjects.DtoTarea;
import com.easytasks.negocio.excepciones.ExisteEntidadException;
import com.easytasks.negocio.excepciones.NoExisteEntidadException;
import com.easytasks.persistencia.entidades.Contexto;
import com.easytasks.persistencia.entidades.Proyecto;
import com.easytasks.persistencia.entidades.Tarea;
import com.easytasks.persistencia.entidades.Usuario;
import com.easytasks.persistencia.persistencia.PersistenciaSBLocal;
import com.easytasks.persistencia.transformadores.TransformadorADtoSB;
import com.easytasks.persistencia.transformadores.TransformadorAEntidadSB;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

/**
 *
 * @author alejandrotocar
 */
@Stateless
public class ABMRealizablesSB implements ABMRealizablesSBLocal {

    @EJB
    private PersistenciaSBLocal persistencia;

    @EJB
    private TransformadorADtoSB aDtoSB;

    @EJB
    private TransformadorAEntidadSB aEntidadSB;
    
    
    // <editor-fold defaultstate="collapsed" desc=" Proyecto ">
    @Override
    public void agregarProyecto(DtoProyecto dtoP) throws ExisteEntidadException {
         try {
            Proyecto p = aEntidadSB.transformarProyecto(dtoP);
            //Contexto c = p.getContexto();
            Usuario u = persistencia.buscarUsuario(p.getResponsable().getNombreUsuario());
            p.setResponsable(u);
            //persistencia.agregarContexto(c);
            persistencia.agregarProyecto(p);
        } catch (EntityExistsException e) {
            throw new ExisteEntidadException();
        } catch (PersistenceException p) {
            throw new ExisteEntidadException();
        } catch (Exception e) {
            throw new ExisteEntidadException();
        }

    }

    @Override
    public void borrarProyecto(String nombreProyecto) throws NoExisteEntidadException {
         try {
            Proyecto p = persistencia.buscarProyecto(nombreProyecto);
            persistencia.borrarProyecto(p);
        } catch (EJBException | EntityNotFoundException e){
            throw new NoExisteEntidadException();
        }
    }

    @Override
    public DtoProyecto buscarProyecto(String nombreProyecto) throws NoExisteEntidadException {
        try {
            DtoProyecto dto = aDtoSB.transformarProyecto(persistencia.buscarProyecto(nombreProyecto));
            return dto;
        } catch (EntityNotFoundException e) {
            throw new NoExisteEntidadException();
        }
    }

    @Override
    public void modificarProyecto(DtoProyecto dtoP) throws NoExisteEntidadException {
       Proyecto p2;
        try {
            Proyecto p = aEntidadSB.transformarProyecto(dtoP);
            try {
                p2 = persistencia.buscarProyecto(dtoP.getNombre());
            } catch (EJBException e) {
                throw new NoExisteEntidadException(e.getMessage(), e);
            }
            Long id = p2.getId();
            p.setId(id);

            try {
                persistencia.modificarProyecto(p);

            } catch (Exception e) {
                throw new NoExisteEntidadException("WTF", e);
            }
        } catch (NoResultException e) {
            throw new NoExisteEntidadException();
        }

    }

    // </editor-fold>
    
        // <editor-fold defaultstate="collapsed" desc=" Tarea ">

    
    @Override
    public void agregarTarea(DtoTarea dtoT) throws ExisteEntidadException {
        try {
            Tarea t = aEntidadSB.transformarTarea(dtoT);
            persistencia.agregarTarea(t);
        } catch (EntityExistsException e) {
            throw new ExisteEntidadException();
        } catch (PersistenceException p) {
            throw new ExisteEntidadException();
        } catch (Exception e) {
            throw new ExisteEntidadException();
        }
    }

    @Override
    public void borrarTarea(String nombreTarea) throws NoExisteEntidadException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DtoTarea buscarTarea(String nombreTarea) throws NoExisteEntidadException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modificarTarea(DtoTarea dtoU) throws NoExisteEntidadException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // </editor-fold>
    
}
