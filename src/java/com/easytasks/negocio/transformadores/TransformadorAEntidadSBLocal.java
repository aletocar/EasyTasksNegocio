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
public interface TransformadorAEntidadSBLocal {
    Contexto transformarContexto(DtoContexto c);

    Etiqueta transformarEtiqueta(DtoEtiqueta e);

    Proyecto transformarProyecto(DtoProyecto p);

    Proyecto transformarProyectoLista(DtoProyecto p);

    Tarea transformarTarea(DtoTarea t);

    Tarea transformarTareaLista(DtoTarea t);

    Usuario transformarUsuario(DtoUsuario u);
}
