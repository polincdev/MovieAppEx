package com.polinc.movieappex.data;

import java.util.Hashtable;

public class InitDataSource {

    public static int MOVIES_POPULAR=0;
    public static int MOVIES_TOPRATED=1;
    public static int MOVIES_UPCOMING=2;
    public static int MOVIES_SEARCHABLE=10;
    public Hashtable<Integer,Object> initData=new Hashtable<>();
    public static int currentMovieList=-1;
}
