package com.clinica.interfaces;

import com.clinica.models.Turno;
import com.clinica.enums.EstadoTurno;
import java.util.List;

public interface IOperaria {
    
    void inicializarContador(String prefijo, int valorInicial);
    
    List<Turno> verTurnosActivos();
    
    void cambiarEstadoTurnoNumero(String turno, EstadoTurno nuevoEstado);
}
