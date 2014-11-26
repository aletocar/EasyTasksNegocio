/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.negocio;

import com.easytasks.dataTransferObjects.DtoTarea;
import com.easytasks.negocio.excepciones.EntidadModificadaIncorrectamenteException;
import com.easytasks.negocio.excepciones.NoExisteEntidadException;
import com.easytasks.persistencia.entidades.Proyecto;
import com.easytasks.persistencia.entidades.Tarea;
import com.easytasks.persistencia.entidades.Usuario;
import com.easytasks.persistencia.persistencia.PersistenciaSBLocal;
import com.easytasks.persistencia.transformadores.TransformadorADtoSB;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

/**
 *
 * @author alejandrotocar
 */
@Stateless
public class ManejadorTareasSB implements ManejadorTareasSBLocal {

    @EJB
    private PersistenciaSBLocal persistencia;

    @EJB
    private TransformadorADtoSB aDtoSB;

    @Override
    public void agregarSubTarea(String nombreTareaPadre, String nombreTareaHijo, String nombreProyecto, String nombreUsuarioResponsable) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException {
        Proyecto p;
        Usuario responsable;

        if (nombreTareaHijo.equals(nombreTareaPadre)) {
            throw new EntidadModificadaIncorrectamenteException("Una tarea no puede ser subtarea de si misma");
        }
        try {
            responsable = persistencia.buscarUsuario(nombreUsuarioResponsable);
            p = persistencia.buscarProyecto(nombreProyecto, responsable);
            Tarea padre = persistencia.buscarTarea(nombreTareaPadre, p);
            Tarea hija = persistencia.buscarTarea(nombreTareaHijo, p);
            if (padre.getSubtareas().contains(hija)) {
                throw new EntidadModificadaIncorrectamenteException("La tarea " + hija.getNombre() + " ya es subtarea de esta tarea");
            } else if (hija.getSubtareas().contains(padre)) {
                throw new EntidadModificadaIncorrectamenteException("La tarea " + padre.getNombre() + " es subtarea de la tarea "
                        + hija.getNombre());
            } else {
                padre.getSubtareas().add(hija);
                persistencia.modificarTarea(padre);
                for (Usuario u : padre.getListaResponsables()) {//Hacerlo recursivo
                    if (!hija.getListaResponsables().contains(u)) {
                        try {
                            agregarResponsable(nombreTareaHijo, nombreProyecto, nombreUsuarioResponsable, nombreUsuarioResponsable, u.getNombreUsuario());
                        } catch (EntidadModificadaIncorrectamenteException | NoExisteEntidadException e) {
                            //No hacemos nada, ya que el error que puede venir aquí son validaciones para cuando agrega el usuario. Para nuestro caso, no hay nada que debamos hacer
                        }
                    }
                }

            }
        } catch (EJBException e) {
            throw new NoExisteEntidadException("No se encontró alguno de los objetos indicados");
        }
    }

    @Override
    public void agregarResponsable(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario, String nombreUsuarioAAgregar) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException {
        Proyecto p;
        Usuario responsable, uAagregar, u;

        try {
            responsable = persistencia.buscarUsuario(nombreUsuarioResponsable);
            p = persistencia.buscarProyecto(nombreProyecto, responsable);
            u = persistencia.buscarUsuario(nombreUsuario);
            uAagregar = persistencia.buscarUsuario(nombreUsuarioAAgregar);
            if (!p.getResponsable().equals(u) && !p.getUsuarios().contains(u)) {
                throw new EntidadModificadaIncorrectamenteException("El usuario que agrega un responsable a la tarea debe pertenecer al proyecto de la misma");
            } else if (!p.getResponsable().equals(uAagregar) && !p.getUsuarios().contains(uAagregar)) {
                throw new EntidadModificadaIncorrectamenteException("El usuario que es agregado como responsable a la tarea debe pertenecer al proyecto de la misma");
            } else {
                Tarea t = persistencia.buscarTarea(nombreTarea, p);
                if (t.getListaResponsables().contains(uAagregar)) {
                    throw new EntidadModificadaIncorrectamenteException("El usuario ya es responsable de esta tarea");
                } else {
                    t.getListaResponsables().add(uAagregar);
                    persistencia.modificarTarea(t);
                    for (Tarea ta : t.getSubtareas()) {
                        if (!ta.getListaResponsables().contains(uAagregar)) {
                            try {
                                agregarResponsable(ta.getNombre(), nombreProyecto, nombreUsuarioResponsable, nombreUsuario, nombreUsuarioAAgregar);
                            } catch (EntidadModificadaIncorrectamenteException | NoExisteEntidadException e) {
                                //No Hay que hacer nada, ya que si salta una excepcion es controlada por la lógica misma del método.
                            }
                        }
                    }
                }
            }
        } catch (EJBException e) {
            throw new NoExisteEntidadException("No se encontró alguno de los objetos indicados");
        }
    }

    @Override
    public void delegarTarea(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuarioActual, String nombreUsuarioDelegar) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException {

        Proyecto p;
        Usuario uActual;
        Usuario uDelegado;
        Usuario responsable;
        Tarea t;
        try {
            responsable = persistencia.buscarUsuario(nombreUsuarioResponsable);
            p = persistencia.buscarProyecto(nombreProyecto, responsable);
            uActual = persistencia.buscarUsuario(nombreUsuarioActual);
            uDelegado = persistencia.buscarUsuario(nombreUsuarioDelegar);

            if (!p.getUsuarios().contains(uActual) && !p.getResponsable().equals(uActual)) {
                throw new EntidadModificadaIncorrectamenteException("El usuario que delega no pertenece al proyecto de la tarea");
            } else if (!p.getUsuarios().contains(uDelegado) && !p.getResponsable().equals(uDelegado)) {
                throw new EntidadModificadaIncorrectamenteException("El usuario a delegar no pertenece al proyecto de la tarea");
            }
            t = persistencia.buscarTarea(nombreTarea, p);

            if (!t.getListaResponsables().contains(uActual) && !p.getResponsable().equals(uActual)) {
                throw new EntidadModificadaIncorrectamenteException("El usuario que delega la tarea debe pertenecer al grupo de responsables de la misma");
            } else if (t.getListaResponsables().contains(uDelegado)) {
                throw new EntidadModificadaIncorrectamenteException("El usuario delegado ya es responsable de la tarea");
            } else {
                if (t.isCompletado()) {
                    throw new EntidadModificadaIncorrectamenteException("No se puede delegar una tarea que ya haya sido marcada como finalizada");
                } else {
                    if (t.getListaResponsables().contains(uActual)) {
                        t.getListaResponsables().remove(uActual);
                    }
                    t.getListaResponsables().add(uDelegado);
                    persistencia.modificarTarea(t);
                    for (Tarea ta : t.getSubtareas()) {
                        try {
                            ta.getListaResponsables().remove(uDelegado);//Le saco el que le voy a agregar ahora, para que no tire la excepcion
                            persistencia.modificarTarea(ta);
                            delegarTarea(ta.getNombre(), nombreProyecto, nombreUsuarioResponsable, nombreUsuarioActual, nombreUsuarioDelegar);
                        } catch (EntidadModificadaIncorrectamenteException | NoExisteEntidadException e) {
                            //No Hay que hacer nada, ya que si salta una excepcion es controlada por la lógica misma del método.
                        }
                    }
                }
            }
        } catch (EJBException e) {
            throw new NoExisteEntidadException("No se encontró alguno de los objetos indicados");
        }
    }

    @Override
    public void completarTarea(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException {
        Proyecto p;
        Usuario u;
        Usuario responsable;

        try {
            responsable = persistencia.buscarUsuario(nombreUsuarioResponsable);
            p = persistencia.buscarProyecto(nombreProyecto, responsable);
            u = persistencia.buscarUsuario(nombreUsuario);
            if (!p.getResponsable().equals(u) && !p.getUsuarios().contains(u)) {
                throw new EntidadModificadaIncorrectamenteException("El usuario que completa la tarea debe pertenecer al proyecto de la misma");
            } else {
                Tarea t = persistencia.buscarTarea(nombreTarea, p);
                if (t.isCompletado()) {
                    throw new EntidadModificadaIncorrectamenteException("La tarea ya ha sido marcada como finalizada");
                } else {
                    t.setCompletado(true);
                    t.setRealizador(u);
                    persistencia.modificarTarea(t);
                    for (Tarea ta : t.getSubtareas()) {//TODO: Revisar tema responsables
                        if (!ta.isCompletado()) {
                            try {
                                completarTarea(ta.getNombre(), nombreProyecto, nombreUsuarioResponsable, nombreUsuario);
                            } catch (EntidadModificadaIncorrectamenteException | NoExisteEntidadException e) {
                                //No Hay que hacer nada, ya que si salta una excepcion es controlada por la lógica misma del método.
                            }
                        }
                    }
                    //persistencia.modificarTarea(t);
                }
            }
        } catch (EJBException e) {
            throw new NoExisteEntidadException("No se encontró alguno de los objetos indicados");
        }
    }

    @Override
    public List<DtoTarea> consultarTareasRealizadas(String nombreUsuario) throws NoExisteEntidadException {
        Usuario u;

        try {
            u = persistencia.buscarUsuario(nombreUsuario);
            List<Tarea> tareasCompletadas = persistencia.buscarTareasCompletadasPorUsuario(u);
            List<DtoTarea> retorno = new ArrayList<>();
            for (Tarea t : tareasCompletadas) {
                assert (t.isCompletado());
                DtoTarea dto = aDtoSB.transformarTarea(t);
                retorno.add(dto);
            }
            return retorno;
        } catch (EJBException e) {
            return null;
        }
    }

    @Override
    public List<DtoTarea> consultarTareasPendientes(String nombreUsuario) throws NoExisteEntidadException {
        //No se implementa para esta version
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DtoTarea> consultarTareasRealizadasResponsable(String nombreUsuario) throws NoExisteEntidadException {
        Usuario u;

        try {
            u = persistencia.buscarUsuario(nombreUsuario);
            List<Tarea> tareasCompletadas = persistencia.buscarTareasCompletadasResponsable(u);
            List<DtoTarea> retorno = new ArrayList<>();
            for (Tarea t : tareasCompletadas) {
                assert (t.isCompletado());
                DtoTarea dto = aDtoSB.transformarTarea(t);
                retorno.add(dto);
            }
            return retorno;
        } catch (EJBException e) {
            return null;
        }
    }

}
