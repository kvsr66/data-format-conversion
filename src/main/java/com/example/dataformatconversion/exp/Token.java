package com.example.dataformatconversion.exp;

import java.util.Map;

public class Token {

    String value;
    String origValue;
    boolean specialQuoteCase;

    Token(String s){
        this.value = s;
        this.origValue =  s;
    }

    Token(String s, String originalText){
        this.value = s;
        this.origValue =  originalText;
        specialQuoteCase  = true;

    }

    public void addVars(Map h){

    }
}
