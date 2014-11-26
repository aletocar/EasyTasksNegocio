/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.easytasks.negocio;

import com.easytasks.negocio.excepciones.EntidadModificadaIncorrectamenteException;
import com.easytasks.negocio.excepciones.NoExisteEntidadException;
import com.easytasks.persistencia.entidades.Proyecto;
import com.easytasks.persistencia.entidades.Tarea;
import com.easytasks.persistencia.entidades.Usuario;
import com.easytasks.persistencia.persistencia.PersistenciaSBLocal;
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

    @Override
    public void agregarSubTarea(String nombreTareaPadre, String nombreTareaHijo, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario) throws EntidadModificadaIncorrectamenteException {
        
    }

    @Override
    public void agregarResponsable(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario) throws EntidadModificadaIncorrectamenteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delegarTarea(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario, String nombreUsuarioDelegar) throws EntidadModificadaIncorrectamenteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void completarTarea(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException {
        Proyecto p;
        Usuario u;
        Usuario responsable;
        
        try{
            responsable = persistencia.buscarUsuario(nombreUsuarioResponsable);
            p = persistencia.buscarProyecto(nombreProyecto, responsable);
            u = persistencia.buscarUsuario(nombreUsuario);
            if(!p.getResponsable().equals(u) &&!p.getUsuarios().contains(u) ){
                throw new EntidadModificadaIncorrectamenteException("El usuario que completa la tarea debe pertenecer al proyecto de la misma");
            }else{
                Tarea t = persistencia.buscarTarea(nombreTarea, p);
                if(t.isCompletado()){
                   throw new EntidadModificadaIncorrectamenteException("La tarea ya ha sido marcada como finalizada");
                }else{
                    t.setCompletado(true);
                    t.setRealizador(u);
                    persistencia.modificarTarea(t);
                }
            }
        }catch(EJBException e){
            throw new NoExisteEntidadException("No se encontró alguno de los objetos indicados");
        }
    }

    @Override
    public List<Tarea> consultarTareasRealizadas(String nombreUsuario) throws NoExisteEntidadException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tarea> consultarTareasPendientes(String nombreUsuario) throws NoExisteEntidadException {
        //No se implementa para esta version
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
