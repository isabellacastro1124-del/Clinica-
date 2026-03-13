package com.clinica.models;

public class PacienteEmbarazada extends Paciente {
    
    public PacienteEmbarazada(String identificacion) {
        super(identificacion);
    }
    
    @Override
    public String getPrefijo() {
        return "PE";
    }
}
