package com.clinica.models;

public class PacienteRegular extends Paciente {
    
    public PacienteRegular(String identificacion) {
        super(identificacion);
    }
    
    @Override
    public String getPrefijo() {
        return "PR";
    }
}
