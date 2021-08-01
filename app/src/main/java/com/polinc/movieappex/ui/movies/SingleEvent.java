package com.polinc.movieappex.ui.movies;

public class SingleEvent<T> {

    private T content;
    boolean hasBeenHandled = false;


    public SingleEvent(T content){
        this.content=content;
    }

 public T getContentIfNotHandled() {
         if (hasBeenHandled) {
             return  null;
        } else {
            hasBeenHandled = true;
             return content;
        }
    }

    public T peekContent(){return content;}

}
