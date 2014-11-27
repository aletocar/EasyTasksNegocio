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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author alejandrotocar
 */
@Stateless
public class TransformadorADtoSB implements TransformadorADtoSBLocal {

    @Override
    public DtoUsuario transformarUsuario(Usuario u) {
        if (u != null) {
            DtoUsuario ret = new DtoUsuario();
            ret.setContraseña(u.getContraseña());
            ret.setId(u.getId());
            ret.setMail(u.getMail());
            ret.setNombre(u.getNombre());
            ret.setNombreUsuario(u.getNombreUsuario());
            List<DtoUsuario> listaContactos = new ArrayList<>();
            for (Usuario usuario : u.getContactos()) {
                DtoUsuario dto = transformarUsuarioLista(usuario);
                listaContactos.add(dto);
            }
            ret.setContactos(listaContactos);
            List<DtoUsuario> listaSoyContactoDe = new ArrayList<>();
            for (Usuario usuario : u.getSoyContactoDe()) {
                DtoUsuario dto = transformarUsuarioLista(usuario);
                listaSoyContactoDe.add(dto);
            }
            ret.setSoyContactoDe(listaSoyContactoDe);
            return ret;
        } else {
            return null;
        }
    }

    private DtoUsuario transformarUsuarioLista(Usuario u) {
        if (u != null) {
            DtoUsuario ret = new DtoUsuario();
            ret.setId(u.getId());
            ret.setContraseña(u.getContraseña());
            ret.setMail(u.getMail());
            ret.setNombre(u.getNombre());
            ret.setNombreUsuario(u.getNombreUsuario());
            ret.setContactos(null);
            ret.setSoyContactoDe(null);
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public DtoEtiqueta transformarEtiqueta(Etiqueta e) {
        if (e != null) {
            DtoEtiqueta ret = new DtoEtiqueta();
            ret.setId(e.getId());
            ret.setNombre(e.getNombre());
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public DtoContexto transformarContexto(Contexto c) {
        if (c != null) {
            DtoContexto ret = new DtoContexto();
            ret.setId(c.getId());
            ret.setDescripcion(c.getDescripcion());
            ret.setNombre(c.getNombre());
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public DtoProyecto transformarProyecto(Proyecto p) {
        if (p != null) {
            DtoProyecto ret = new DtoProyecto();
            ret.setId(p.getId());
            ret.setContexto(transformarContexto(p.getContexto()));
            ret.setFechaFin(p.getFechaFin());
            ret.setFechaInicio(p.getFechaInicio());
            ret.setNombre(p.getNombre());
            ret.setResponsable(transformarUsuarioLista(p.getResponsable()));
            List<DtoUsuario> listaUsuarios = new ArrayList<>();
            for (Usuario u : p.getUsuarios()) {
                DtoUsuario dto = transformarUsuarioLista(u);
                listaUsuarios.add(dto);
            }
            ret.setUsuarios(listaUsuarios);
            List<DtoTarea> listaTareas = new ArrayList<>();
            for (Tarea t : p.getTareas()) {
                DtoTarea dto = transformarTareaLista(t);
                listaTareas.add(dto);
            }
            ret.setTareas(listaTareas);
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public DtoProyecto transformarProyectoLista(Proyecto p) {
        if (p != null) {
            DtoProyecto ret = new DtoProyecto();
            ret.setId(p.getId());
            ret.setContexto(transformarContexto(p.getContexto()));
            ret.setFechaFin(p.getFechaFin());
            ret.setFechaInicio(p.getFechaInicio());
            ret.setNombre(p.getNombre());
            ret.setResponsable(transformarUsuarioLista(p.getResponsable()));
            ret.setUsuarios(null);
            ret.setTareas(null);
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public DtoTarea transformarTarea(Tarea t) {
        if (t != null) {
            DtoTarea ret = new DtoTarea();
            ret.setId(t.getId());
            ret.setCompletado(t.isCompletado());
            ret.setFechaCreacion(t.getFechaCreacion());
            ret.setFechaLimite(t.getFechaLimite());
            List<DtoTarea> listaRealizables = new ArrayList<>();
            for (Tarea r : t.getSubtareas()) {
                DtoTarea dto = transformarTareaLista(r);
                listaRealizables.add(dto);
            }
            DtoUsuario real = transformarUsuarioLista(t.getRealizador());
            ret.setRealizador(real);
            ret.setSubtareas(listaRealizables);
            ret.setNombre(t.getNombre());
            ret.setPrioridad(t.getPrioridad());
            List<DtoEtiqueta> listaEtiquetas = new ArrayList<>();
            List<DtoUsuario> listaUsuarios = new ArrayList<>();
            for (Etiqueta e : t.getListaEtiquetas()) {
                DtoEtiqueta dto = transformarEtiqueta(e);
                listaEtiquetas.add(dto);
            }
            for (Usuario u : t.getListaResponsables()) {
                DtoUsuario dto = transformarUsuarioLista(u);
                listaUsuarios.add(dto);
            }
            ret.setListaEtiquetas(listaEtiquetas);
            ret.setListaResponsables(listaUsuarios);
            DtoProyecto p = transformarProyectoLista(t.getProyecto());
            ret.setProyecto(p);
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public DtoTarea transformarTareaLista(Tarea t) {
        if (t != null) {
            DtoTarea ret = new DtoTarea();
            ret.setId(t.getId());
            ret.setCompletado(t.isCompletado());
            ret.setFechaCreacion(t.getFechaCreacion());
            ret.setFechaLimite(t.getFechaLimite());
            ret.setSubtareas(null);
            DtoUsuario real = transformarUsuarioLista(t.getRealizador());
            ret.setRealizador(real);
            ret.setNombre(t.getNombre());
            ret.setPrioridad(t.getPrioridad());
            DtoProyecto p = transformarProyectoLista(t.getProyecto());
            ret.setProyecto(p);
            ret.setListaEtiquetas(null);
            ret.setListaResponsables(null);
            return ret;
        } else {
            return null;
        }
    }
}
