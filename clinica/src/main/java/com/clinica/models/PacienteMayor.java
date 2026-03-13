package com.clinica.models;

public class PacienteMayor extends Paciente {
    
    public PacienteMayor(String identificacion) {
        super(identificacion);
    }
    
    @Override
    public String getPrefijo() {
        return "PM";
    }
}
