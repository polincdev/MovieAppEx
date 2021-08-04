package com.polinc.movieappex.ui.login;

import android.content.Context;
import android.graphics.Typeface;

public class TypeFactory {

    private String DEJA_BOLD= "DejaVuSansBold.ttf";
    private String DEJA_ITALIC= "DejaVuSansItalics.ttf";
    private String DEJA_REGULAR= "DejaVuSansRegular.ttf";

    Typeface dejaBold;
    Typeface dejaItalic;
    Typeface dejaRegular;


    public TypeFactory(Context context){
        dejaBold = Typeface.createFromAsset(context.getAssets(),DEJA_BOLD);
        dejaItalic = Typeface.createFromAsset(context.getAssets(),DEJA_ITALIC);
        dejaRegular = Typeface.createFromAsset(context.getAssets(),DEJA_REGULAR);

    }

}