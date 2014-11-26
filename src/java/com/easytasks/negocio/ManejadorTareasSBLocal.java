/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.easytasks.negocio;

import com.easytasks.negocio.excepciones.EntidadModificadaIncorrectamenteException;
import com.easytasks.negocio.excepciones.NoExisteEntidadException;
import com.easytasks.persistencia.entidades.Tarea;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author alejandrotocar
 */
@Local
public interface ManejadorTareasSBLocal {
    
    void agregarSubTarea(String nombreTareaPadre, String nombreTareaHijo, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario) throws EntidadModificadaIncorrectamenteException;
    
    void agregarResponsable(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario) throws EntidadModificadaIncorrectamenteException;
    
    void delegarTarea(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario, String nombreUsuarioDelegar) throws EntidadModificadaIncorrectamenteException;
    
    void completarTarea(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException;
    
    List<Tarea> consultarTareasRealizadas(String nombreUsuario) throws NoExisteEntidadException;

    List<Tarea> consultarTareasPendientes(String nombreUsuario) throws NoExisteEntidadException;

}
