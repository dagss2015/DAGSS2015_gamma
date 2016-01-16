/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class RecetaDAO extends GenericoDAO<Receta>{
 
    public List<Receta> buscarPorPrescripcion(long prescripcion_id) {
        Query q = em.createQuery("SELECT t FROM Receta AS t WHERE t.prescripcion.id = :prescripcion_id");
        q.setParameter("prescripcion_id", prescripcion_id);
        return q.getResultList();
    }
    
}
