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
import javax.ejb.Local;

/**
 *
 * @author alejandrotocar
 */
@Local
public interface ABMRealizablesSBLocal {
    
    void agregarProyecto(DtoProyecto dtoP) throws ExisteEntidadException;

    void borrarProyecto(String nombreProyecto) throws NoExisteEntidadException;

    DtoProyecto buscarProyecto(String nombreProyecto) throws NoExisteEntidadException;

    void modificarProyecto(DtoProyecto dtoP) throws NoExisteEntidadException;
    
    void agregarTarea(DtoTarea dtoU) throws ExisteEntidadException;

    void borrarTarea(String nombreTarea) throws NoExisteEntidadException;

    DtoTarea buscarTarea(String nombreTarea) throws NoExisteEntidadException;

    void modificarTarea(DtoTarea dtoU) throws NoExisteEntidadException;
    
}
