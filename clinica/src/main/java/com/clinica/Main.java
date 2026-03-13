package com.clinica;

import com.clinica.gestor.GestorTurnos;
import com.clinica.models.PacienteMayor;
import com.clinica.models.PacienteEmbarazada;
import com.clinica.models.PacienteRegular;
import com.clinica.models.Turno;
import com.clinica.enums.EstadoTurno;

public class Main {

    public static void main(String[] args) {

        // usamos singleton para tener una sola instancia del gestor
        GestorTurnos gestor = GestorTurnos.getInstance();

        // contadores arrancan en 0 para cada tipo
        gestor.inicializarContador("PM", 0);
        gestor.inicializarContador("PE", 0);
        gestor.inicializarContador("PR", 0);

        // un paciente de cada tipo con documentos distintos
        PacienteMayor paciente1 = new PacienteMayor("123456789");
        PacienteEmbarazada paciente2 = new PacienteEmbarazada("987654321");
        PacienteRegular paciente3 = new PacienteRegular("456123789");

        // caso 1: pedimos los tres turnos y mostramos los numeros asignados
        System.out.println("--------Caso 1: solicitar turno (mayor, embarazada y regular)");
        Turno turno1 = gestor.solicitarTurno(paciente1);
        Turno turno2 = gestor.solicitarTurno(paciente2);
        Turno turno3 = gestor.solicitarTurno(paciente3);
        System.out.println("turno1 -> " + turno1.getNumeroTurno() + " | estado: " + turno1.getEstado());
        System.out.println("turno2 -> " + turno2.getNumeroTurno() + " | estado: " + turno2.getEstado());
        System.out.println("turno3 -> " + turno3.getNumeroTurno() + " | estado: " + turno3.getEstado());

        // caso 2: los tres deben aparecer en la lista de activos
        System.out.println("--------Caso 2: ver turnos activos");
        gestor.verTurnosActivos().forEach(t ->
            System.out.println(t.getNumeroTurno() + " | " + t.getPaciente().getIdentificacion() + " | " + t.getEstado())
        );

        // caso 3: marcamos el turno1 como atendido y verificamos el cambio
        System.out.println("--------Caso 3: cambiar estado del turno1 a ATENDIDO");
        gestor.cambiarEstadoTurnoNumero(turno1.getNumeroTurno(), EstadoTurno.ATENDIDO);
        System.out.println(turno1.getNumeroTurno() + " -> " + turno1.getEstado());

        // caso 4: turno1 ya no deberia aparecer porque fue atendido
        System.out.println("--------Caso 4: turnos activos despues de atender turno1");
        gestor.verTurnosActivos().forEach(t ->
            System.out.println(t.getNumeroTurno() + " | " + t.getPaciente().getIdentificacion() + " | " + t.getEstado())
        );

        // caso 5: el paciente cancela su propio turno usando su cedula
        System.out.println("--------Caso 5: cancelar turno2 con la cedula del paciente");
        boolean cancelado = gestor.cancelarTurnoNumero(turno2.getNumeroTurno(),
                                                       paciente2.getIdentificacion());
        System.out.println(turno2.getNumeroTurno() + " cancelado: " + cancelado);
    }
}
