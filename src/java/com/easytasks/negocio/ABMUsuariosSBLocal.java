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

    void agregarContacto(String usuario, String contacto);

    void agregarUsuario(DtoUsuario dtoU) throws ExisteEntidadException;

    void borrarUsuario(String nombreUsuario);

    DtoUsuario buscarUsuario(String nombreusuario);

    String chequeoDeVida();

    void modificarUsuario(DtoUsuario dtoU);
    
}
