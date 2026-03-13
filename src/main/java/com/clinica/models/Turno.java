package com.clinica.models;

import com.clinica.enums.EstadoTurno;
import java.time.LocalDateTime;

public class Turno {
    
    private String numeroTurno;
    private Paciente paciente;
    private LocalDateTime horaSolicitud;
    private EstadoTurno estado;
    
    public Turno(String numeroTurno, Paciente paciente, LocalDateTime hora) {
        this.numeroTurno = numeroTurno;
        this.paciente = paciente;
        this.horaSolicitud = hora;
        this.estado = EstadoTurno.PENDIENTE;
    }
    
    public String getNumeroTurno() {
        return numeroTurno;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public LocalDateTime getHoraSolicitud() {
        return horaSolicitud;
    }
    
    public EstadoTurno getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }
}
