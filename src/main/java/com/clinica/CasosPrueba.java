package com.clinica;

import com.clinica.gestor.GestorTurnos;
import com.clinica.models.PacienteMayor;
import com.clinica.models.PacienteEmbarazada;
import com.clinica.models.PacienteRegular;
import com.clinica.models.Turno;
import java.util.List;

public class CasosPrueba {

    public static void main(String[] args) {
        caso1_ListaTurnos();
        caso2_DosturnosUsuario();
        caso3_CancelacionTurno();
        caso4_PacientesDistintosTipoMismoDocumento();
    }

    // verifica que se puedan pedir turnos para los tres tipos de paciente
    // y que todos queden registrados como activos
    private static void caso1_ListaTurnos() {
        System.out.println("--------Caso 1: dos turnos por cada tipo de paciente (mayor, embarazada, regular)");

        GestorTurnos gestor = GestorTurnos.getInstance();
        gestor.reset();
        gestor.inicializarContador("PM", 0);
        gestor.inicializarContador("PE", 0);
        gestor.inicializarContador("PR", 0);

        // dos pacientes por cada tipo
        PacienteMayor p1_mayor = new PacienteMayor("100001");
        PacienteMayor p2_mayor = new PacienteMayor("100002");
        PacienteEmbarazada p1_embarazada = new PacienteEmbarazada("200001");
        PacienteEmbarazada p2_embarazada = new PacienteEmbarazada("200002");
        PacienteRegular p1_regular = new PacienteRegular("300001");
        PacienteRegular p2_regular = new PacienteRegular("300002");

        // pedimos todos los turnos antes de imprimir para no mezclar salida con logica
        Turno t1_mayor = gestor.solicitarTurno(p1_mayor);
        Turno t2_mayor = gestor.solicitarTurno(p2_mayor);
        Turno t1_emb   = gestor.solicitarTurno(p1_embarazada);
        Turno t2_emb   = gestor.solicitarTurno(p2_embarazada);
        Turno t1_reg   = gestor.solicitarTurno(p1_regular);
        Turno t2_reg   = gestor.solicitarTurno(p2_regular);

        // listamos lo que quedo en el gestor
        List<Turno> turnos = gestor.verTurnosActivos();
        System.out.println("turnos activos: " + turnos.size());
        turnos.forEach(t -> {
            String tipo = t.getPaciente().getPrefijo();
            String tipoNombre = tipo.equals("PM") ? "mayor" : tipo.equals("PE") ? "embarazada" : "regular";
            System.out.println(t.getNumeroTurno() + " | " + t.getPaciente().getIdentificacion() + " | " + tipoNombre + " | " + t.getEstado());
        });
    }

    // un paciente no puede tener dos turnos activos al mismo tiempo
    // el segundo intento debe lanzar una excepcion
    private static void caso2_DosturnosUsuario() {
        System.out.println("--------Caso 2: segundo turno para el mismo paciente debe lanzar excepcion (doc 123456, contador desde 100)");

        GestorTurnos gestor = GestorTurnos.getInstance();
        gestor.reset();
        gestor.inicializarContador("PM", 100);

        PacienteMayor paciente = new PacienteMayor("123456");

        // primer turno: debe crearse sin problema
        try {
            Turno turno1 = gestor.solicitarTurno(paciente);
            System.out.println("turno1 ok -> " + turno1.getNumeroTurno() + " | doc: " + turno1.getPaciente().getIdentificacion() + " | estado: " + turno1.getEstado() + " | hora: " + turno1.getHoraSolicitud());
        } catch (IllegalArgumentException e) {
            System.out.println("error turno1: " + e.getMessage());
        }

        // segundo turno con el mismo paciente: el gestor lo rechaza
        try {
            Turno turno2 = gestor.solicitarTurno(paciente);
            System.out.println("turno2 ok (no deberia llegar aqui)");
        } catch (IllegalArgumentException e) {
            System.out.println("excepcion esperada en turno2: " + e.getMessage());
        }
    }

    // crea un turno, lo cancela y luego intenta cancelarlo de nuevo
    // la segunda cancelacion debe fallar porque el turno ya no esta en PENDIENTE
    private static void caso3_CancelacionTurno() {
        System.out.println("--------Caso 3: crear turno, cancelarlo y verificar que no se puede cancelar dos veces (embarazada 555666, contador desde 500)");

        GestorTurnos gestor = GestorTurnos.getInstance();
        gestor.reset();
        gestor.inicializarContador("PE", 500);

        PacienteEmbarazada paciente = new PacienteEmbarazada("555666");
        Turno turno = gestor.solicitarTurno(paciente);

        System.out.println("turno creado -> " + turno.getNumeroTurno() + " | doc: " + paciente.getIdentificacion() + " | estado: " + turno.getEstado() + " | hora: " + turno.getHoraSolicitud());

        // primera cancelacion: exito esperado
        boolean cancelado1 = gestor.cancelarTurnoNumero(turno.getNumeroTurno(), paciente.getIdentificacion());
        System.out.println("primer cancel -> " + cancelado1 + " | estado: " + turno.getEstado());

        // segunda cancelacion: debe devolver false
        boolean cancelado2 = gestor.cancelarTurnoNumero(turno.getNumeroTurno(), paciente.getIdentificacion());
        System.out.println("segundo cancel -> " + cancelado2 + " (solo se pueden cancelar turnos en estado PENDIENTE)");
    }

    // el sistema identifica a los pacientes por documento, no por tipo
    // si el documento ya tiene un turno activo no importa que sea otro tipo de paciente
    private static void caso4_PacientesDistintosTipoMismoDocumento() {
        System.out.println("--------Caso 4: mismo documento en distinto tipo de paciente debe lanzar excepcion (doc 123456)");

        GestorTurnos gestor = GestorTurnos.getInstance();
        gestor.reset();
        gestor.inicializarContador("PM", 200);
        gestor.inicializarContador("PR", 300);

        // turno para persona mayor con doc 123456
        PacienteMayor pacienteMayor = new PacienteMayor("123456");

        try {
            Turno turnoMayor = gestor.solicitarTurno(pacienteMayor);
            System.out.println("turno mayor ok -> " + turnoMayor.getNumeroTurno() + " | estado: " + turnoMayor.getEstado());
        } catch (IllegalArgumentException e) {
            System.out.println("error mayor: " + e.getMessage());
        }

        // intento con paciente regular usando el mismo doc 123456
        PacienteRegular pacienteRegular = new PacienteRegular("123456");

        try {
            Turno turnoRegular = gestor.solicitarTurno(pacienteRegular);
            System.out.println("turno regular ok (no deberia llegar aqui)");
        } catch (IllegalArgumentException e) {
            System.out.println("excepcion esperada en regular: " + e.getMessage());
            System.out.println("el sistema valida por documento sin importar el tipo de paciente");
        }
    }
}
