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
import com.easytasks.persistencia.entidades.Proyecto;
import com.easytasks.persistencia.entidades.Usuario;
import javax.ejb.Local;

/**
 *
 * @author alejandrotocar
 */
@Local
public interface ABMRealizablesSBLocal {

    void agregarProyecto(DtoProyecto dtoP) throws ExisteEntidadException, EntidadNoCreadaCorrectamenteException;

    void borrarProyecto(String nombreProyecto, String nombreResponsable) throws NoExisteEntidadException;

    DtoProyecto buscarProyecto(String nombreProyecto, Usuario responsable) throws NoExisteEntidadException;

    void modificarProyecto(DtoProyecto dtoP) throws NoExisteEntidadException, EntidadModificadaIncorrectamenteException;

    void agregarTarea(DtoTarea dtoU) throws ExisteEntidadException, EntidadNoCreadaCorrectamenteException;

    void borrarTarea(String nombreTarea, String nombreProyecto, String nombreResponsable, String nombreEliminador) throws NoExisteEntidadException, EntidadEliminadaIncorrectamenteException;

    DtoTarea buscarTarea(String nombreTarea) throws NoExisteEntidadException;

    void modificarTarea(DtoTarea dtoT, String nombreUsuarioModificador) throws NoExisteEntidadException, EntidadModificadaIncorrectamenteException;

    void asignarUsuarioAProyecto(String nombreProyecto, String nombreResponsable, String nombreUsuario) throws NoExisteEntidadException;

}
