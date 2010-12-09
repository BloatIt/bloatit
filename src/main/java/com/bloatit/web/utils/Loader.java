package com.bloatit.web.utils;


public abstract class Loader<T> {
    
    public abstract T fromString(String data);
    
    public String toString(T data){
        return data.toString();
    }
    
}
