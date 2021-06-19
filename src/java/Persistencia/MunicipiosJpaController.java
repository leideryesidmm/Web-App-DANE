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
import DTO.Departamento;
import DTO.Municipios;
import DTO.Persona;
import Persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author USER
 */
public class MunicipiosJpaController implements Serializable {
    
    private EntityManagerFactory emf = null;
    
    public MunicipiosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public MunicipiosJpaController() {
        this.emf = Persistence.createEntityManagerFactory("web_app_danePU");
    }


    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Municipios municipios) {
        if (municipios.getPersonaCollection() == null) {
            municipios.setPersonaCollection(new ArrayList<Persona>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento idDpto = municipios.getIdDpto();
            if (idDpto != null) {
                idDpto = em.getReference(idDpto.getClass(), idDpto.getIdDpto());
                municipios.setIdDpto(idDpto);
            }
            Collection<Persona> attachedPersonaCollection = new ArrayList<Persona>();
            for (Persona personaCollectionPersonaToAttach : municipios.getPersonaCollection()) {
                personaCollectionPersonaToAttach = em.getReference(personaCollectionPersonaToAttach.getClass(), personaCollectionPersonaToAttach.getCedula());
                attachedPersonaCollection.add(personaCollectionPersonaToAttach);
            }
            municipios.setPersonaCollection(attachedPersonaCollection);
            em.persist(municipios);
            if (idDpto != null) {
                idDpto.getMunicipiosCollection().add(municipios);
                idDpto = em.merge(idDpto);
            }
            for (Persona personaCollectionPersona : municipios.getPersonaCollection()) {
                Municipios oldIdMunicipioOfPersonaCollectionPersona = personaCollectionPersona.getIdMunicipio();
                personaCollectionPersona.setIdMunicipio(municipios);
                personaCollectionPersona = em.merge(personaCollectionPersona);
                if (oldIdMunicipioOfPersonaCollectionPersona != null) {
                    oldIdMunicipioOfPersonaCollectionPersona.getPersonaCollection().remove(personaCollectionPersona);
                    oldIdMunicipioOfPersonaCollectionPersona = em.merge(oldIdMunicipioOfPersonaCollectionPersona);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Municipios municipios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Municipios persistentMunicipios = em.find(Municipios.class, municipios.getIdMunicipio());
            Departamento idDptoOld = persistentMunicipios.getIdDpto();
            Departamento idDptoNew = municipios.getIdDpto();
            Collection<Persona> personaCollectionOld = persistentMunicipios.getPersonaCollection();
            Collection<Persona> personaCollectionNew = municipios.getPersonaCollection();
            if (idDptoNew != null) {
                idDptoNew = em.getReference(idDptoNew.getClass(), idDptoNew.getIdDpto());
                municipios.setIdDpto(idDptoNew);
            }
            Collection<Persona> attachedPersonaCollectionNew = new ArrayList<Persona>();
            for (Persona personaCollectionNewPersonaToAttach : personaCollectionNew) {
                personaCollectionNewPersonaToAttach = em.getReference(personaCollectionNewPersonaToAttach.getClass(), personaCollectionNewPersonaToAttach.getCedula());
                attachedPersonaCollectionNew.add(personaCollectionNewPersonaToAttach);
            }
            personaCollectionNew = attachedPersonaCollectionNew;
            municipios.setPersonaCollection(personaCollectionNew);
            municipios = em.merge(municipios);
            if (idDptoOld != null && !idDptoOld.equals(idDptoNew)) {
                idDptoOld.getMunicipiosCollection().remove(municipios);
                idDptoOld = em.merge(idDptoOld);
            }
            if (idDptoNew != null && !idDptoNew.equals(idDptoOld)) {
                idDptoNew.getMunicipiosCollection().add(municipios);
                idDptoNew = em.merge(idDptoNew);
            }
            for (Persona personaCollectionOldPersona : personaCollectionOld) {
                if (!personaCollectionNew.contains(personaCollectionOldPersona)) {
                    personaCollectionOldPersona.setIdMunicipio(null);
                    personaCollectionOldPersona = em.merge(personaCollectionOldPersona);
                }
            }
            for (Persona personaCollectionNewPersona : personaCollectionNew) {
                if (!personaCollectionOld.contains(personaCollectionNewPersona)) {
                    Municipios oldIdMunicipioOfPersonaCollectionNewPersona = personaCollectionNewPersona.getIdMunicipio();
                    personaCollectionNewPersona.setIdMunicipio(municipios);
                    personaCollectionNewPersona = em.merge(personaCollectionNewPersona);
                    if (oldIdMunicipioOfPersonaCollectionNewPersona != null && !oldIdMunicipioOfPersonaCollectionNewPersona.equals(municipios)) {
                        oldIdMunicipioOfPersonaCollectionNewPersona.getPersonaCollection().remove(personaCollectionNewPersona);
                        oldIdMunicipioOfPersonaCollectionNewPersona = em.merge(oldIdMunicipioOfPersonaCollectionNewPersona);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = municipios.getIdMunicipio();
                if (findMunicipios(id) == null) {
                    throw new NonexistentEntityException("The municipios with id " + id + " no longer exists.");
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
            Municipios municipios;
            try {
                municipios = em.getReference(Municipios.class, id);
                municipios.getIdMunicipio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The municipios with id " + id + " no longer exists.", enfe);
            }
            Departamento idDpto = municipios.getIdDpto();
            if (idDpto != null) {
                idDpto.getMunicipiosCollection().remove(municipios);
                idDpto = em.merge(idDpto);
            }
            Collection<Persona> personaCollection = municipios.getPersonaCollection();
            for (Persona personaCollectionPersona : personaCollection) {
                personaCollectionPersona.setIdMunicipio(null);
                personaCollectionPersona = em.merge(personaCollectionPersona);
            }
            em.remove(municipios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Municipios> findMunicipiosEntities() {
        return findMunicipiosEntities(true, -1, -1);
    }

    public List<Municipios> findMunicipiosEntities(int maxResults, int firstResult) {
        return findMunicipiosEntities(false, maxResults, firstResult);
    }

    private List<Municipios> findMunicipiosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Municipios.class));
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

    public Municipios findMunicipios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Municipios.class, id);
        } finally {
            em.close();
        }
    }

    public int getMunicipiosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Municipios> rt = cq.from(Municipios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
