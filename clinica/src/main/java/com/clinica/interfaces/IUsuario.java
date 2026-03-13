package com.clinica.interfaces;

import com.clinica.models.Paciente;
import com.clinica.models.Turno;

public interface IUsuario {
    
    Turno solicitarTurno(Paciente paciente);
    
    boolean cancelarTurnoNumero(String turno, String identificacion);
}
