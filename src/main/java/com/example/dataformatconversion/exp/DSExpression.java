package com.example.dataformatconversion.exp;

import java.util.Map;

public class DSExpression {

    private Token value;
    DSExpression(String expressionName){

    }

    public void parse(String s){

    }


    public Map addVars(Map h){
        if ( this.value == null)
            return null;
        else{
            this.value.addVars(h);
            return h;
        }
    }

    public String evaluate(Map h , boolean logdebugMsg ){
        return  "";
    }

}
