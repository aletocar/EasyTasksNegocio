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
public class EntidadModificadaIncorrectamenteException extends Exception{
    public EntidadModificadaIncorrectamenteException() {
        super();
    }
    
    public EntidadModificadaIncorrectamenteException(String message){
        super(message);
    }
    
    public EntidadModificadaIncorrectamenteException(String message, Throwable cause){
        super(message, cause);
    }
    
    public EntidadModificadaIncorrectamenteException(Throwable cause){
        super(cause);
    }
}
