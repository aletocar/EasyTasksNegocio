/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.negocio.logica;

import com.easytasks.dataTransferObjects.DtoTarea;
import com.easytasks.negocio.excepciones.EntidadModificadaIncorrectamenteException;
import com.easytasks.negocio.excepciones.NoExisteEntidadException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author alejandrotocar
 */
@Local
public interface ManejadorTareasSBLocal {

    void agregarSubTarea(String nombreTareaPadre, String nombreTareaHijo, String nombreProyecto, String nombreUsuarioResponsable) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException;

    void agregarResponsable(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario, String nombreUsuarioAAgregar) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException;

    void delegarTarea(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuarioActual, String nombreUsuarioDelegar) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException;

    void completarTarea(String nombreTarea, String nombreProyecto, String nombreUsuarioResponsable, String nombreUsuario) throws EntidadModificadaIncorrectamenteException, NoExisteEntidadException;

    List<DtoTarea> consultarTareasRealizadas(String nombreUsuario) throws NoExisteEntidadException;

    List<DtoTarea> consultarTareasPendientes(String nombreUsuario) throws NoExisteEntidadException;

    List<DtoTarea> consultarTareasRealizadasResponsable(String nombreUsuario) throws NoExisteEntidadException;

}
