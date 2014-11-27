/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.negocio.logica;

import com.easytasks.negocio.excepciones.*;
import com.easytasks.dataTransferObjects.DtoUsuario;
import javax.ejb.Local;

/**
 *
 * @author alejandrotocar
 */
@Local
public interface ABMUsuariosSBLocal {

    void agregarContacto(String usuario, String contacto) throws NoExisteEntidadException, ExisteEntidadException, EntidadModificadaIncorrectamenteException;

    void agregarUsuario(DtoUsuario dtoU) throws ExisteEntidadException, EntidadNoCreadaCorrectamenteException;

    void borrarUsuario(String nombreUsuario) throws NoExisteEntidadException;

    DtoUsuario buscarUsuario(String nombreusuario) throws NoExisteEntidadException;

    void modificarUsuario(DtoUsuario dtoU) throws NoExisteEntidadException, EntidadModificadaIncorrectamenteException;

    String login(String username, String password) throws ExisteEntidadException, NoExisteEntidadException;

    void logout(String token) throws NoExisteEntidadException;

    boolean estaLogueado(String token, String username);

    String conectar(String nombreUsuario,String redSocial);

    void ingresarPin(String nombreUsuario,String pin);

    void postear(String nombreUsuario,String post, String redSocial) ;
    
    String desconectar(String nombreUsuario, String redSocial);

}
