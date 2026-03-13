package com.clinica.gestor;

import com.clinica.models.Paciente;
import com.clinica.models.Turno;
import com.clinica.enums.EstadoTurno;
import com.clinica.interfaces.IUsuario;
import com.clinica.interfaces.IOperaria;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestorTurnos implements IUsuario, IOperaria {
    
    private static GestorTurnos instancia;
    private List<Turno> turnos;
    private Map<String, Paciente> pacientesRegistrados;
    private Map<String, Integer> contadores;
    
    private GestorTurnos() {
        this.turnos = new ArrayList<>();
        this.pacientesRegistrados = new HashMap<>();
        this.contadores = new HashMap<>();
    }
    
    public static synchronized GestorTurnos getInstance() {
        if (instancia == null) {
            instancia = new GestorTurnos();
        }
        return instancia;
    }
    
    public void reset() {
        this.turnos.clear();
        this.pacientesRegistrados.clear();
        this.contadores.clear();
    }
    
    @Override

    public Turno solicitarTurno(Paciente paciente) {
        validarConsistenciaPaciente(paciente);
        validarExistenciaTurnoActivo(paciente.getIdentificacion());
        
        String numeroTurno = generarNumeroTurno(paciente.getPrefijo());
        Turno turno = new Turno(numeroTurno, paciente, LocalDateTime.now());
        
        turnos.add(turno);
        pacientesRegistrados.put(paciente.getIdentificacion(), paciente);
        
        return turno;
    }
    
    @Override

    public boolean cancelarTurnoNumero(String turno, String identificacion) {
        return turnos.stream()
            .filter(t -> t.getNumeroTurno().equals(turno) && 
                    t.getPaciente().getIdentificacion().equals(identificacion) &&
                    t.getEstado() == EstadoTurno.PENDIENTE)
            .findFirst()
            .map(t -> {
                t.setEstado(EstadoTurno.CANCELADO);
                return true;
            })
            .orElse(false);
    }
    
    @Override

    public void inicializarContador(String prefijo, int valorInicial) {
        contadores.put(prefijo, valorInicial);
    }
    
    @Override
    public List<Turno> verTurnosActivos() {
        return turnos.stream()
            .filter(t -> t.getEstado() == EstadoTurno.PENDIENTE)
            .collect(Collectors.toList());
    }
    
    public List<Turno> obtenerTodosTurnos() {
        return new ArrayList<>(turnos);
    }
    
    @Override
    public void cambiarEstadoTurnoNumero(String turno, EstadoTurno nuevoEstado) {
        turnos.stream()
            .filter(t -> t.getNumeroTurno().equals(turno))
            .findFirst()
            .ifPresent(t -> t.setEstado(nuevoEstado));
    }
    
    private String generarNumeroTurno(String prefijo) {
        contadores.putIfAbsent(prefijo, 0);
        int contador = contadores.get(prefijo) + 1;
        contadores.put(prefijo, contador);
        return prefijo + "-" + contador;
    }
    
    private void validarExistenciaTurnoActivo(String identificacion) {
        boolean tieneturnoActivo = turnos.stream()
            .anyMatch(t -> t.getPaciente().getIdentificacion().equals(identificacion) &&
                    t.getEstado() == EstadoTurno.PENDIENTE);
        
        if (tieneturnoActivo) {
            throw new IllegalArgumentException(
                "El paciente ya tiene un turno activo");
        }
    }
    
    private void validarConsistenciaPaciente(Paciente paciente) {
        if (paciente == null || paciente.getIdentificacion() == null || 
            paciente.getIdentificacion().isEmpty()) {
            throw new IllegalArgumentException(
                "Paciente inválido o identificación vacía");
        }
    }
}
