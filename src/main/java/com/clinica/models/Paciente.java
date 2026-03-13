package com.clinica.models;

public abstract class Paciente {
    
    private String identificacion;
    
    public Paciente(String identificacion) {
        this.identificacion = identificacion;
    }
    
    public String getIdentificacion() {
        return identificacion;
    }
    
    public abstract String getPrefijo();
}
