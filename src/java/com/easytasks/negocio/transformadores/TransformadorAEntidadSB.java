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
public class TransformadorAEntidadSB implements TransformadorAEntidadSBLocal {

    @Override
    public Usuario transformarUsuario(DtoUsuario u) {
        if (u != null) {
            Usuario ret = new Usuario();
            ret.setContraseña(u.getContraseña());
            ret.setMail(u.getMail());
            ret.setNombre(u.getNombre());
            ret.setNombreUsuario(u.getNombreUsuario());
            List<Usuario> listaContactos = new ArrayList<>();
            for (DtoUsuario usuario : u.getContactos()) {
                Usuario us = transformarUsuarioLista(usuario);
                listaContactos.add(us);
            }
            ret.setContactos(listaContactos);
            List<Usuario> listaSoyContactoDe = new ArrayList<>();
            for (DtoUsuario usuario : u.getSoyContactoDe()) {
                Usuario us = transformarUsuarioLista(usuario);
                listaSoyContactoDe.add(us);
            }
            ret.setSoyContactoDe(listaSoyContactoDe);
            return ret;
        } else {
            return null;
        }
    }

    private Usuario transformarUsuarioLista(DtoUsuario u) {
        if (u != null) {
            Usuario ret = new Usuario();
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
    public Etiqueta transformarEtiqueta(DtoEtiqueta e) {
        if (e != null) {
            Etiqueta ret = new Etiqueta();
            ret.setNombre(e.getNombre());
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Contexto transformarContexto(DtoContexto c) {
        if (c != null) {
            Contexto ret = new Contexto();
            ret.setDescripcion(c.getDescripcion());
            ret.setNombre(c.getNombre());
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Proyecto transformarProyecto(DtoProyecto p) {
        if (p != null) {
            Proyecto ret = new Proyecto();
            ret.setContexto(transformarContexto(p.getContexto()));
            ret.setFechaFin(p.getFechaFin());
            ret.setFechaInicio(p.getFechaInicio());
            ret.setNombre(p.getNombre());
            ret.setResponsable(transformarUsuarioLista(p.getResponsable()));
            List<Usuario> listaUsuarios = new ArrayList<>();
            for (DtoUsuario u : p.getUsuarios()) {
                Usuario us = transformarUsuarioLista(u);
                listaUsuarios.add(us);
            }
            ret.setUsuarios(listaUsuarios);
            List<Tarea> listaTareas = new ArrayList<>();
            for (DtoTarea t : p.getTareas()) {
                Tarea ta = transformarTareaLista(t);
                listaTareas.add(ta);
            }
            ret.setTareas(listaTareas);
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Proyecto transformarProyectoLista(DtoProyecto p) {
        if (p != null) {
            Proyecto ret = new Proyecto();
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
    public Tarea transformarTarea(DtoTarea t) {
        if (t != null) {
            Tarea ret = new Tarea();
            ret.setCompletado(t.isCompletado());
            ret.setFechaCreacion(t.getFechaCreacion());
            ret.setFechaLimite(t.getFechaLimite());
            List<Tarea> listaRealizables = new ArrayList<>();
            for (DtoTarea r : t.getSubtareas()) {
                Tarea dto = transformarTareaLista(r);
                listaRealizables.add(dto);
            }
            ret.setSubtareas(listaRealizables);
            ret.setNombre(t.getNombre());
            ret.setPrioridad(t.getPrioridad());
            Usuario real = transformarUsuarioLista(t.getRealizador());
            ret.setRealizador(real);
            List<Etiqueta> listaEtiquetas = new ArrayList<>();
            List<Usuario> listaUsuarios = new ArrayList<>();
            for (DtoEtiqueta e : t.getListaEtiquetas()) {
                Etiqueta dto = transformarEtiqueta(e);
                listaEtiquetas.add(dto);
            }
            for (DtoUsuario u : t.getListaResponsables()) {
                Usuario dto = transformarUsuarioLista(u);
                listaUsuarios.add(dto);
            }
            ret.setListaEtiquetas(listaEtiquetas);
            ret.setListaResponsables(listaUsuarios);
            Proyecto p = transformarProyectoLista(t.getProyecto());
            ret.setProyecto(p);
            return ret;
        } else {
            return null;
        }
    }

    @Override
    public Tarea transformarTareaLista(DtoTarea t) {
        if (t != null) {
            Tarea ret = new Tarea();
            ret.setCompletado(t.isCompletado());
            ret.setFechaCreacion(t.getFechaCreacion());
            ret.setFechaLimite(t.getFechaLimite());
            ret.setSubtareas(null);
            ret.setNombre(t.getNombre());
            Usuario real = transformarUsuarioLista(t.getRealizador());
            ret.setRealizador(real);
            ret.setPrioridad(t.getPrioridad());
            Proyecto p = transformarProyectoLista(t.getProyecto());
            ret.setProyecto(p);
            ret.setListaEtiquetas(null);
            ret.setListaResponsables(null);
            return ret;
        } else {
            return null;
        }
    }
}
