/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.easytasks.negocio;

import com.easytasks.negocio.excepciones.*;
import com.easytasks.dataTransferObjects.DtoUsuario;
import javax.ejb.Local;

/**
 *
 * @author alejandrotocar
 */
@Local
public interface ABMUsuariosSBLocal {

    void agregarContacto(String usuario, String contacto) throws NoExisteEntidadException, ExisteEntidadException;

    void agregarUsuario(DtoUsuario dtoU) throws ExisteEntidadException;

    void borrarUsuario(String nombreUsuario) throws NoExisteEntidadException;

    DtoUsuario buscarUsuario(String nombreusuario) throws NoExisteEntidadException;

    void modificarUsuario(DtoUsuario dtoU) throws NoExisteEntidadException;
    
    String login(String username, String password) throws ExisteEntidadException, NoExisteEntidadException;
    
    void logout(String token) throws NoExisteEntidadException;

    public boolean estaLogueado(String token, String username);
    
}
