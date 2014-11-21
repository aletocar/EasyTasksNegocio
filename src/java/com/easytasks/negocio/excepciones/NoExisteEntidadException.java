/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.easytasks.negocio.excepciones;

/**
 *
 * @author alejandrotocar
 */
public class NoExisteEntidadException extends Exception {

    public NoExisteEntidadException() {
        super();
    }
    
    public NoExisteEntidadException(String message){
        super(message);
    }
    
    public NoExisteEntidadException(String message, Throwable cause){
        super(message, cause);
    }
    
    public NoExisteEntidadException(Throwable cause){
        super(cause);
    }
}
