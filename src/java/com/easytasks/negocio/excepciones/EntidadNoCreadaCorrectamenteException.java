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
public class EntidadNoCreadaCorrectamenteException extends Exception{
    public EntidadNoCreadaCorrectamenteException() {
        super();
    }
    
    public EntidadNoCreadaCorrectamenteException(String message){
        super(message);
    }
    
    public EntidadNoCreadaCorrectamenteException(String message, Throwable cause){
        super(message, cause);
    }
    
    public EntidadNoCreadaCorrectamenteException(Throwable cause){
        super(cause);
    }
}
