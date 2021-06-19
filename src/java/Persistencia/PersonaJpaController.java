/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Municipios;
import DTO.Persona;
import Persistencia.exceptions.NonexistentEntityException;
import Persistencia.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author USER
 */
public class PersonaJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public PersonaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("web_app_danePU");
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Municipios idMunicipio = persona.getIdMunicipio();
            if (idMunicipio != null) {
                idMunicipio = em.getReference(idMunicipio.getClass(), idMunicipio.getIdMunicipio());
                persona.setIdMunicipio(idMunicipio);
            }
            em.persist(persona);
            if (idMunicipio != null) {
                idMunicipio.getPersonaCollection().add(persona);
                idMunicipio = em.merge(idMunicipio);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersona(persona.getCedula()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getCedula());
            Municipios idMunicipioOld = persistentPersona.getIdMunicipio();
            Municipios idMunicipioNew = persona.getIdMunicipio();
            if (idMunicipioNew != null) {
                idMunicipioNew = em.getReference(idMunicipioNew.getClass(), idMunicipioNew.getIdMunicipio());
                persona.setIdMunicipio(idMunicipioNew);
            }
            persona = em.merge(persona);
            if (idMunicipioOld != null && !idMunicipioOld.equals(idMunicipioNew)) {
                idMunicipioOld.getPersonaCollection().remove(persona);
                idMunicipioOld = em.merge(idMunicipioOld);
            }
            if (idMunicipioNew != null && !idMunicipioNew.equals(idMunicipioOld)) {
                idMunicipioNew.getPersonaCollection().add(persona);
                idMunicipioNew = em.merge(idMunicipioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = persona.getCedula();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            Municipios idMunicipio = persona.getIdMunicipio();
            if (idMunicipio != null) {
                idMunicipio.getPersonaCollection().remove(persona);
                idMunicipio = em.merge(idMunicipio);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
