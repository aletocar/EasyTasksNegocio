/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.negocio;

import com.easytasks.dataTransferObjects.DtoProyecto;
import com.easytasks.dataTransferObjects.DtoTarea;
import com.easytasks.negocio.excepciones.EntidadEliminadaIncorrectamenteException;
import com.easytasks.negocio.excepciones.EntidadModificadaIncorrectamenteException;
import com.easytasks.negocio.excepciones.EntidadNoCreadaCorrectamenteException;
import com.easytasks.negocio.excepciones.ExisteEntidadException;
import com.easytasks.negocio.excepciones.NoExisteEntidadException;
import com.easytasks.persistencia.entidades.Contexto;
import com.easytasks.persistencia.entidades.Proyecto;
import com.easytasks.persistencia.entidades.Tarea;
import com.easytasks.persistencia.entidades.Usuario;
import com.easytasks.persistencia.persistencia.PersistenciaSBLocal;
import com.easytasks.persistencia.transformadores.TransformadorADtoSB;
import com.easytasks.persistencia.transformadores.TransformadorAEntidadSB;
import java.util.List;
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
    public void agregarProyecto(DtoProyecto dtoP) throws ExisteEntidadException, EntidadNoCreadaCorrectamenteException {
        if (dtoP.getTareas().size() > 0) {
            throw new EntidadNoCreadaCorrectamenteException("Un proyecto debe crearse sin tareas asignadas.");
        } else if (dtoP.getUsuarios().size() > 0) {
            throw new EntidadNoCreadaCorrectamenteException("Un proyecto debe crearse sin usuarios asignados.");
        } else {
            try {
                Proyecto p = aEntidadSB.transformarProyecto(dtoP);
                Usuario u = persistencia.buscarUsuario(p.getResponsable().getNombreUsuario());
                Contexto c = persistencia.buscarContexto(p.getContexto().getNombre());
                if (c != null) {
                    p.setContexto(c);
                }
                p.setResponsable(u);
                persistencia.agregarProyecto(p);
            } catch (EntityExistsException e) {
                throw new ExisteEntidadException();
            } catch (PersistenceException p) {
                throw new ExisteEntidadException();
            } catch (Exception e) {
                throw new ExisteEntidadException();
            }
        }
    }

    @Override
    public void borrarProyecto(String nombreProyecto, String nombreResponsable) throws NoExisteEntidadException {
        try {
            Usuario responsable = persistencia.buscarUsuario(nombreResponsable);
            Proyecto p = persistencia.buscarProyecto(nombreProyecto, responsable);
            persistencia.borrarProyecto(p);
        } catch (EJBException | EntityNotFoundException e) {
            throw new NoExisteEntidadException();
        }
    }

    @Override
    public DtoProyecto buscarProyecto(String nombreProyecto, Usuario responsable) throws NoExisteEntidadException {
        try {
            responsable = persistencia.buscarUsuario(responsable.getNombreUsuario());
            DtoProyecto dto = aDtoSB.transformarProyecto(persistencia.buscarProyecto(nombreProyecto, responsable));
            return dto;
        } catch (EntityNotFoundException e) {
            throw new NoExisteEntidadException();
        }
    }

    @Override
    public void modificarProyecto(DtoProyecto dtoP) throws NoExisteEntidadException, EntidadModificadaIncorrectamenteException {
        Proyecto p2;
        if (dtoP.getTareas().size() > 0) {
            throw new EntidadModificadaIncorrectamenteException("Un proyecto debe crearse sin tareas asignadas.");
        } else if (dtoP.getUsuarios().size() > 0) {
            throw new EntidadModificadaIncorrectamenteException("Un proyecto debe crearse sin usuarios asignados.");
        } else {
            try {
                Proyecto p = aEntidadSB.transformarProyecto(dtoP);
                Usuario responsable = persistencia.buscarUsuario(p.getResponsable().getNombreUsuario());
                p.setResponsable(responsable);
                try {
                    p2 = persistencia.buscarProyecto(dtoP.getNombre(), responsable);
                } catch (EJBException e) {
                    throw new NoExisteEntidadException(e.getMessage(), e);
                }
                try {
                    List<Usuario> listaUsuarios = persistencia.buscarUsuariosDeProyecto(p2.getNombre(), p2.getResponsable());
                    p.setUsuarios(listaUsuarios);
                    List<Tarea> listaTareas = persistencia.buscarTareasDeProyecto(p2);
                    p.setTareas(listaTareas);
                } catch (EJBException e) {
                    throw new NoExisteEntidadException(e.getMessage());
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
    }

    @Override
    public void asignarUsuarioAProyecto(String nombreProyecto, String nombreResponsable, String nombreUsuario) throws NoExisteEntidadException {
        try {
            Usuario u = persistencia.buscarUsuario(nombreUsuario);
            Usuario r = persistencia.buscarUsuario(nombreResponsable);
            Proyecto p = persistencia.buscarProyecto(nombreProyecto, r);
            List<Usuario> listaUsuarios = persistencia.buscarUsuariosDeProyecto(nombreProyecto, r);
            listaUsuarios.add(u);
            p.setUsuarios(listaUsuarios);
            persistencia.modificarProyecto(p);
        } catch (EJBException e) {
            throw new NoExisteEntidadException(e.getMessage());
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Tarea ">
    @Override
    public void agregarTarea(DtoTarea dtoT) throws ExisteEntidadException, EntidadNoCreadaCorrectamenteException {
        if (dtoT.getSubtareas().size() > 0) {
            throw new EntidadNoCreadaCorrectamenteException("Una tarea debe crearse sin subtareas asignadas.");
        } else if (dtoT.getListaResponsables().size() > 0) {
            throw new EntidadNoCreadaCorrectamenteException("Una tarea debe crearse sin responsables asignados.");
        } else {
            try {
                Tarea t = aEntidadSB.transformarTarea(dtoT);
                Usuario u = persistencia.buscarUsuario(t.getProyecto().getResponsable().getNombreUsuario());
                Proyecto p = persistencia.buscarProyecto(t.getProyecto().getNombre(), u);
                if (p == null) {
                    throw new EntidadNoCreadaCorrectamenteException("No se asignó proyecto a la tarea. Por favor asígnelo antes de crearla");
                }
                t.setProyecto(p);
                persistencia.agregarTarea(t);
            } catch (EntityExistsException e) {
                throw new ExisteEntidadException("Ya existe una tarea con este nombre para este proyecto.");
            } catch (PersistenceException p) {
                throw new ExisteEntidadException();
            } catch (Exception e) {
                throw new ExisteEntidadException();
            }
        }
    }

    @Override
    public void borrarTarea(String nombreTarea, String nombreProyecto, String nombreResponsable, String nombreEliminador) throws NoExisteEntidadException, EntidadEliminadaIncorrectamenteException {
        try {
            Usuario responsable = persistencia.buscarUsuario(nombreResponsable);
            Proyecto p = persistencia.buscarProyecto(nombreProyecto, responsable);
            Usuario eliminador = persistencia.buscarUsuario(nombreEliminador);
            Tarea t = persistencia.buscarTarea(nombreTarea, p);
            if (t.getListaResponsables().contains(eliminador)) {
                persistencia.borrarTarea(t);
            } else {
                throw new EntidadEliminadaIncorrectamenteException("El usuario que elimina la tarea debe ser responsable de ella");
            }
        } catch (EJBException | EntityNotFoundException e) {
            throw new NoExisteEntidadException("No se encontró la tarea a borrar");
        }
    }

    @Override
    public DtoTarea buscarTarea(String nombreTarea) throws NoExisteEntidadException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modificarTarea(DtoTarea dtoT, String nombreUsuarioModificador) throws NoExisteEntidadException, EntidadModificadaIncorrectamenteException {
        Tarea t2;
        if (dtoT.getSubtareas().size() > 0) {
            throw new EntidadModificadaIncorrectamenteException("No se puede modificar las subtareas. Utilice el método correspondiente.");
        } else if (dtoT.getListaResponsables().size() > 0) {
            throw new EntidadModificadaIncorrectamenteException("No se puede modificar la lista de responsables. Utilice el método correspondiente.");
        } else {
            try {
                Tarea t = aEntidadSB.transformarTarea(dtoT);
                Usuario u = persistencia.buscarUsuario(t.getProyecto().getResponsable().getNombreUsuario());
                Proyecto p = persistencia.buscarProyecto(t.getProyecto().getNombre(), u);
                t.setProyecto(p);
                try {
                    t2 = persistencia.buscarTarea(dtoT.getNombre(), p);
                } catch (EJBException e) {
                    throw new NoExisteEntidadException(e.getMessage(), e);
                }
                try {
                    List<Tarea> listaSubTareas = persistencia.buscarSubtareasDeTarea(t2.getNombre(), p);
                    t.setSubtareas(listaSubTareas);
                    List<Usuario> listaResponsables = persistencia.buscarResponsablesDeTarea(t2.getNombre(), p);
                    t.setListaResponsables(listaResponsables);
                } catch (EJBException e) {
                    throw new NoExisteEntidadException(e.getMessage());
                }
                Long id = t2.getId();
                t.setId(id);

                Usuario modificador = persistencia.buscarUsuario(nombreUsuarioModificador);
                if (t.getListaResponsables().contains(modificador)) {
                    try {
                        persistencia.modificarTarea(t);

                    } catch (Exception e) {
                        throw new NoExisteEntidadException("WTF", e);
                    }
                } else {
                    throw new EntidadModificadaIncorrectamenteException("El usuario que modifica debe ser responsable de la tarea");
                }
            } catch (NoResultException e) {
                throw new NoExisteEntidadException();
            }
        }
    }

    // </editor-fold>
}
