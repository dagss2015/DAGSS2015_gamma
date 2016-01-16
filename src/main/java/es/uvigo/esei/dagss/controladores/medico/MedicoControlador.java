/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.daos.TratamientoDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Tania Guzmán y Luis González
 */

@Named(value = "medicoControlador")
@SessionScoped
public class MedicoControlador implements Serializable {

    private Medico medicoActual;
    private String dni;
    private String numeroColegiado;
    private String password;
    
    private Tratamiento tratamientoActual;
   
    private Prescripcion prescripcionActual;
   
    private Medicamento medicamentoActual;
    
    private Receta recetaActual;
    
    private Cita citaActual;
    private List<Cita> citas;

    @Inject
    private AutenticacionControlador autenticacionControlador;
    

    @EJB
    MedicoDAO medicoDAO;
    
    @EJB
    CitaDAO citaDAO;
    
    @EJB
    TratamientoDAO tratamientoDAO;
    
    @EJB
    RecetaDAO recetaDAO;
    
    
    public MedicoControlador() {
        
    }
    
    // Getters y setters

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Medico getMedicoActual() {
        return medicoActual;
    }

    public void setMedicoActual(Medico medicoActual) {
        this.medicoActual = medicoActual;
    }
    
    public Tratamiento getTratamientoActual() {
        return tratamientoActual;
    }
    
    public void setTratamientoActual(Tratamiento tratamientoActual) {
        this.tratamientoActual = tratamientoActual;
    }
    
    public Prescripcion getPrescripcionActual() {
        return prescripcionActual;
    }
    
    public void setPrescripcionActual(Prescripcion prescripcionActual) {
        this.prescripcionActual = prescripcionActual;
    }
    
    public Medicamento getMedicamentoActual() {
        return medicamentoActual;
    }
    
    public void setMedicamentoActual(Medicamento medicamentoActual) {
        this.medicamentoActual = medicamentoActual;
    }
    
    public Receta getRecetaActual() {
        return this.recetaActual;
    }
    
    public void setRecetaActual(Receta recetaActual) {
        this.recetaActual = recetaActual;
    }
    
    public Cita getCitaActual() {
        return this.citaActual;
    }
    
    public void setCitaActual(Cita cita) {
        this.citaActual = cita;
    }
    
    public List<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }
    
    /////////////////////////////////////////////////
    
    private boolean parametrosAccesoInvalidos() {
        return (((dni == null) && (numeroColegiado == null)) || (password == null));
    }

    private Medico recuperarDatosMedico() {
        Medico medico = null;
        if (dni != null) {
            medico = medicoDAO.buscarPorDNI(dni);
        }
        if ((medico == null) && (numeroColegiado != null)) {
            medico = medicoDAO.buscarPorNumeroColegiado(numeroColegiado);
        }
        return medico;
    }

    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un DNI ó un número de colegiado o una contraseña", ""));
        } else {
            Medico medico = recuperarDatosMedico();
            if (medico == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún médico con los datos indicados", ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(medico.getId(), password,
                        TipoUsuario.MEDICO.getEtiqueta().toLowerCase())) {
                    medicoActual = medico;
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }
            }
        }
        return destino;
    }
    
    // Búsquedas
    
    public List<Cita> filtrarPorIdMedico() {
        return citaDAO.buscarPorId(medicoActual.getId(), Calendar.getInstance().getTime());
    }
    
    public List<Tratamiento> filtrarTratamientosPorIdMedico() {
        return tratamientoDAO.buscarPorIdMedico(medicoActual.getId());
    }
    
    public List<Tratamiento> tratamientosPaciente(Paciente pacienteActual) {
        return tratamientoDAO.buscarPorIdPaciente(pacienteActual.getId());
    }
    
    public List<Prescripcion> buscarPrescripcionesTratamiento() {
        try {
            return tratamientoDAO.buscarPrescripciones(tratamientoActual.getId());
        } catch(NullPointerException e) {
            
        }
        
        return new ArrayList<>();
    }
    
    public List<Receta> doBuscarRecetasMedico() {
        List<Receta> recetas = new ArrayList<>();
        
        List<Tratamiento> tratamientos = this.filtrarTratamientosPorIdMedico();
        
        for(Tratamiento t: tratamientos) {
            List<Prescripcion> prescripciones = tratamientoDAO.buscarPrescripciones(t.getId());
            
            for(Prescripcion p: prescripciones) {
                List<Receta> recetasP = recetaDAO.buscarPorPrescripcion(p.getId());
                recetas.addAll(recetasP);
            }
        }
        
        return recetas;
    }
    
    // Acciones sobre tratamiento
   
    public void doNuevoTratamiento() {
        tratamientoActual = new Tratamiento();
    }
    
    public void doGuardarNuevoTratamiento(Date fechaInicio, Medico medico, Paciente paciente) {
        
        tratamientoActual.setFechaInicio(fechaInicio);
        tratamientoActual.setMedico(medico);
        tratamientoActual.setPaciente(paciente);
        tratamientoActual = tratamientoDAO.crear(tratamientoActual);
    }
    
    public void doEditarTratamiento(Date fechaInicio, Medico medico, Paciente paciente) {
        
        tratamientoActual.setFechaInicio(fechaInicio);
        tratamientoActual.setMedico(medico);
        tratamientoActual.setPaciente(paciente);
        tratamientoActual = tratamientoDAO.actualizar(tratamientoActual);
    }
    
    public void doEliminarTratamiento() {
        tratamientoDAO.eliminar(tratamientoActual);
        tratamientoActual = null;
    }
    
     public void doFinalizarTratamiento() {
        List<Prescripcion> prescripciones = tratamientoDAO.buscarPrescripciones(tratamientoActual.getId());
       
        for(Prescripcion p: prescripciones) {
            Receta r = new Receta();
            r.setFinValidez(tratamientoActual.getFechaFin());
            r.setInicioValidez(tratamientoActual.getFechaInicio());
            r.setPrescripcion(p);
            r.setCantidad(this.getCantidad(p.getDosis(), p.getMedicamento().getNumeroDosis()));
            recetaDAO.crear(r);
        }
    }
     
     public boolean hayTratamientos(List<Tratamiento> t) {
        return t.isEmpty();
    }
     
     public int getCantidad(int dosis, int cantidadMedicamento) {
        int toret = dosis/cantidadMedicamento;
        if(dosis % cantidadMedicamento > 0) {
            toret += 1;
        }
        
        return toret;
    }
    
    // Acciones sobre prescripcion
    
    public void doNuevaPrescripcion() {
        prescripcionActual = new Prescripcion();
    }
    
    public void doGuardarNuevaPrescripcion() {
        prescripcionActual.setMedicamento(medicamentoActual);
        prescripcionActual.setTratamiento(tratamientoActual);        
        prescripcionActual = tratamientoDAO.crearPrescripcion(prescripcionActual);
    }
    
    public void doGuardarPrescripcionEditada() {
        prescripcionActual.setMedicamento(medicamentoActual);
        prescripcionActual.setTratamiento(tratamientoActual);
        prescripcionActual = tratamientoDAO.actualizarPrescripcion(prescripcionActual);
    }
    
    public void doEliminarPrescripcion() {
        tratamientoDAO.eliminarPrescripcion(prescripcionActual);
    }
    
    // Acciones sobre cita
    
    public void doGuardarCitaEditada() {
        // Actualiza un centro de salud
        citaActual = citaDAO.actualizar(citaActual);
        // Actualiza lista de centros de salud a mostrar
        citas = citaDAO.buscarTodos();
    }
    
    public void doEditarEstado() {
        citaActual.setEstado(EstadoCita.COMPLETADA);
        this.doGuardarCitaEditada();
    }
    
    public void doEditarEstadoAusente() {
        citaActual.setEstado(EstadoCita.AUSENTE);
        this.doGuardarCitaEditada();
    }  
}
