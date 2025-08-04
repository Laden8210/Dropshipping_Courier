package com.example.dropshippingcourier.util;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Miner {

    public static String getText(TextInputLayout field){
        return field.getEditText().getText().toString();
    }

    public static String getText(TextInputEditText field){
        return field.getText().toString();
    }

}
