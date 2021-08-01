package com.polinc.movieappex.ui.login;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.polinc.movieappex.R;
import com.polinc.movieappex.main.Consts;

public class CustomTextView extends TextView {

    private int typefaceType;
    private TypeFactory mFontFactory;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    public CustomTextView(Context context) {
        super(context);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {


        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView,
                0, 0);
        try {
            typefaceType = array.getInteger(R.styleable.CustomTextView_font_name, 0);
        } finally {
            array.recycle();
        }
        if (!isInEditMode()) {
            setTypeface(getTypeFace(typefaceType));
        }

    }

    public Typeface getTypeFace(int type) {
        if (mFontFactory == null)
            mFontFactory = new TypeFactory(getContext());

        switch (type) {
            case Consts.DEJA_BOLD:
                return mFontFactory.dejaBold;

            case Consts.DEJA_ITALICS:
                return mFontFactory.dejaItalic;

            case Consts.DEJA_REGULAR:
                return mFontFactory.dejaRegular;


            default:
                return mFontFactory.dejaRegular;
        }
    }



}