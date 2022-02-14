package com.urjc.es.helsevita.Exceptions;

//It executes when user don't exists

public class UserNotFoundException extends RuntimeException {

    final String username;
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String username) {
        super("User not found: " + username);
        this.username = username;
    }


    public String getUsername() {
        return this.username;
    }

}

