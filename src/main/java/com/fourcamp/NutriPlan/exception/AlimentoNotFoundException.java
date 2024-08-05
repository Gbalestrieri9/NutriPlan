package com.fourcamp.NutriPlan.exception;

public class AlimentoNotFoundException extends RuntimeException{

    public AlimentoNotFoundException(){
        super("Alimento não existe no nosso banco de dados");
    }

    public AlimentoNotFoundException(String message){
        super(message);
    }
}
