/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.easytasks.negocio;

import com.easytasks.dataTransferObjects.DtoContexto;
import com.easytasks.dataTransferObjects.DtoUsuario;
import javax.ejb.Local;

/**
 *
 * @author alejandrotocar
 */
@Local
public interface ABMUsuariosSBLocal {

    void agregarContacto(DtoUsuario usuario, DtoUsuario contacto);

    void agregarUsuario(DtoUsuario dtoU);

    void borrarUsuario(String nombreUsuario);

    DtoUsuario buscarUsuario(String nombreusuario);

    String chequeoDeVida();

    void modificarUsuario(DtoUsuario dtoU);
    
}
