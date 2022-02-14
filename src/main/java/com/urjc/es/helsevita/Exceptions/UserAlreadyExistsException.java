package com.urjc.es.helsevita.Exceptions;


public class UserAlreadyExistsException extends RuntimeException{

    final String username;
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String username) {
        super("El usuario " + username + "ya existe");
        this.username = username;
    }


    public String getUsername() {
        return this.username;
    }

}





