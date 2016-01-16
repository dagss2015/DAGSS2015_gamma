/*
 Proyecto Java EE, DAGSS-2013
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Tratamiento;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class TratamientoDAO extends GenericoDAO<Tratamiento>{
    
    public List<Tratamiento> buscarPorIdPacienteMedico(long medico_id, long paciente_id) {
        Query q = em.createQuery("SELECT t FROM Tratamiento AS t WHERE t.medico.id = :medico_id AND t.paciente.id = :paciente_id");
        q.setParameter("paciente_id", paciente_id);
        q.setParameter("medico_id", medico_id);
        return q.getResultList();
    }
    
    public List<Tratamiento> buscarPorIdPaciente(long paciente_id) {
        Query q = em.createQuery("SELECT t FROM Tratamiento AS t WHERE t.paciente.id = :paciente_id");
        q.setParameter("paciente_id", paciente_id);
        return q.getResultList();
    }
    
    public List<Tratamiento> buscarPorIdMedico(long medico_id) {
        Query q = em.createQuery("SELECT t FROM Tratamiento AS t WHERE t.medico.id = :medico_id");
        q.setParameter("medico_id", medico_id);
        return q.getResultList();
    }
    
    public Prescripcion crearPrescripcion(Prescripcion p) {
        em.persist(p);
        return p;
    }
    
    public Prescripcion actualizarPrescripcion(Prescripcion p) {
        return em.merge(p);
    }
     
    public void eliminarPrescripcion(Prescripcion p) {
        em.remove(em.merge(p));
    }
    
    public List<Prescripcion> buscarPrescripciones(long tratamiento_id) {
        Query q = em.createQuery("SELECT p FROM Prescripcion AS p WHERE p.tratamiento.id = :tratamiento_id");
        q.setParameter("tratamiento_id", tratamiento_id);
        return q.getResultList();
    }
}
