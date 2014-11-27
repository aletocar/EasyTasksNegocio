/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.negocio.transformadores;

import com.easytasks.dataTransferObjects.DtoContexto;
import com.easytasks.dataTransferObjects.DtoEtiqueta;
import com.easytasks.dataTransferObjects.DtoProyecto;
import com.easytasks.dataTransferObjects.DtoTarea;
import com.easytasks.dataTransferObjects.DtoUsuario;
import com.easytasks.persistencia.entidades.Contexto;
import com.easytasks.persistencia.entidades.Etiqueta;
import com.easytasks.persistencia.entidades.Proyecto;
import com.easytasks.persistencia.entidades.Tarea;
import com.easytasks.persistencia.entidades.Usuario;
import javax.ejb.Local;

/**
 *
 * @author alejandrotocar
 */
@Local
public interface TransformadorADtoSBLocal {

    DtoContexto transformarContexto(Contexto c);

    DtoEtiqueta transformarEtiqueta(Etiqueta e);

    DtoProyecto transformarProyecto(Proyecto p);

    DtoProyecto transformarProyectoLista(Proyecto p);

    DtoTarea transformarTarea(Tarea t);

    DtoTarea transformarTareaLista(Tarea t);

    DtoUsuario transformarUsuario(Usuario u);
    
}
