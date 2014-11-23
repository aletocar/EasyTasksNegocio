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
public class EntidadEliminadaIncorrectamenteException extends Exception{
    public EntidadEliminadaIncorrectamenteException() {
        super();
    }
    
    public EntidadEliminadaIncorrectamenteException(String message){
        super(message);
    }
    
    public EntidadEliminadaIncorrectamenteException(String message, Throwable cause){
        super(message, cause);
    }
    
    public EntidadEliminadaIncorrectamenteException(Throwable cause){
        super(cause);
    }
}
