/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.easytasks.negocio;

import com.easytasks.dataTransferObjects.*;
import com.easytasks.persistencia.entidades.*;
import com.easytasks.persistencia.persistencia.PersistenciaSB;
import com.easytasks.persistencia.transformadores.TransformadorADtoSB;
import com.easytasks.persistencia.transformadores.TransformadorAEntidadSB;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Local;

/**
 *
 * @author alumnoFI
 */
@Stateless
public class ABMUsuariosSB implements ABMUsuariosSBLocal {

    @EJB
    private PersistenciaSB persistencia;
    
    @EJB 
    private TransformadorADtoSB aDtoSB;
    
    @EJB
    private TransformadorAEntidadSB aEntidadSB;
    
    @Override
    public String chequeoDeVida(){
        return "estoy vivo";
    }
    
    @Override
    public void agregarUsuario(DtoUsuario dtoU){
        Usuario u = aEntidadSB.transformarUsuario(dtoU);
        persistencia.agregarUsuario(u);
        //TODO: Validar_????
    }
    
    @Override
    public void modificarUsuario(DtoUsuario dtoU){
        Usuario u = aEntidadSB.transformarUsuario(dtoU);
        u.setId(persistencia.buscarUsuario(dtoU.getNombreUsuario()).getId());
        persistencia.modificarUsuario(u);
    }
    
    @Override
    public void borrarUsuario(DtoUsuario dtoU){
        Usuario u = aEntidadSB.transformarUsuario(dtoU);
        persistencia.borrarUsuario(u);
    }
    
    @Override
    public void agregarContacto(DtoUsuario usuario, DtoUsuario contacto){
        Usuario u = aEntidadSB.transformarUsuario(usuario);
        u.getContactos().add(aEntidadSB.transformarUsuario(contacto));
        persistencia.modificarUsuario(u);
    }
    
    @Override
    public DtoUsuario buscarUsuario(String nombreusuario){
        DtoUsuario dto = aDtoSB.transformarUsuario(persistencia.buscarUsuario(nombreusuario));
        return dto;
    }
}
