/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import DTO.Departamento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Municipios;
import Persistencia.exceptions.NonexistentEntityException;
import Persistencia.exceptions.PreexistingEntityException;
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
public class DepartamentoJpaController implements Serializable {
    
    private EntityManagerFactory emf = null;
    
    public DepartamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public DepartamentoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("web_app_danePU");
    }
    

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) throws PreexistingEntityException, Exception {
        if (departamento.getMunicipiosCollection() == null) {
            departamento.setMunicipiosCollection(new ArrayList<Municipios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Municipios> attachedMunicipiosCollection = new ArrayList<Municipios>();
            for (Municipios municipiosCollectionMunicipiosToAttach : departamento.getMunicipiosCollection()) {
                municipiosCollectionMunicipiosToAttach = em.getReference(municipiosCollectionMunicipiosToAttach.getClass(), municipiosCollectionMunicipiosToAttach.getIdMunicipio());
                attachedMunicipiosCollection.add(municipiosCollectionMunicipiosToAttach);
            }
            departamento.setMunicipiosCollection(attachedMunicipiosCollection);
            em.persist(departamento);
            for (Municipios municipiosCollectionMunicipios : departamento.getMunicipiosCollection()) {
                Departamento oldIdDptoOfMunicipiosCollectionMunicipios = municipiosCollectionMunicipios.getIdDpto();
                municipiosCollectionMunicipios.setIdDpto(departamento);
                municipiosCollectionMunicipios = em.merge(municipiosCollectionMunicipios);
                if (oldIdDptoOfMunicipiosCollectionMunicipios != null) {
                    oldIdDptoOfMunicipiosCollectionMunicipios.getMunicipiosCollection().remove(municipiosCollectionMunicipios);
                    oldIdDptoOfMunicipiosCollectionMunicipios = em.merge(oldIdDptoOfMunicipiosCollectionMunicipios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDepartamento(departamento.getIdDpto()) != null) {
                throw new PreexistingEntityException("Departamento " + departamento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getIdDpto());
            Collection<Municipios> municipiosCollectionOld = persistentDepartamento.getMunicipiosCollection();
            Collection<Municipios> municipiosCollectionNew = departamento.getMunicipiosCollection();
            Collection<Municipios> attachedMunicipiosCollectionNew = new ArrayList<Municipios>();
            for (Municipios municipiosCollectionNewMunicipiosToAttach : municipiosCollectionNew) {
                municipiosCollectionNewMunicipiosToAttach = em.getReference(municipiosCollectionNewMunicipiosToAttach.getClass(), municipiosCollectionNewMunicipiosToAttach.getIdMunicipio());
                attachedMunicipiosCollectionNew.add(municipiosCollectionNewMunicipiosToAttach);
            }
            municipiosCollectionNew = attachedMunicipiosCollectionNew;
            departamento.setMunicipiosCollection(municipiosCollectionNew);
            departamento = em.merge(departamento);
            for (Municipios municipiosCollectionOldMunicipios : municipiosCollectionOld) {
                if (!municipiosCollectionNew.contains(municipiosCollectionOldMunicipios)) {
                    municipiosCollectionOldMunicipios.setIdDpto(null);
                    municipiosCollectionOldMunicipios = em.merge(municipiosCollectionOldMunicipios);
                }
            }
            for (Municipios municipiosCollectionNewMunicipios : municipiosCollectionNew) {
                if (!municipiosCollectionOld.contains(municipiosCollectionNewMunicipios)) {
                    Departamento oldIdDptoOfMunicipiosCollectionNewMunicipios = municipiosCollectionNewMunicipios.getIdDpto();
                    municipiosCollectionNewMunicipios.setIdDpto(departamento);
                    municipiosCollectionNewMunicipios = em.merge(municipiosCollectionNewMunicipios);
                    if (oldIdDptoOfMunicipiosCollectionNewMunicipios != null && !oldIdDptoOfMunicipiosCollectionNewMunicipios.equals(departamento)) {
                        oldIdDptoOfMunicipiosCollectionNewMunicipios.getMunicipiosCollection().remove(municipiosCollectionNewMunicipios);
                        oldIdDptoOfMunicipiosCollectionNewMunicipios = em.merge(oldIdDptoOfMunicipiosCollectionNewMunicipios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamento.getIdDpto();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getIdDpto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            Collection<Municipios> municipiosCollection = departamento.getMunicipiosCollection();
            for (Municipios municipiosCollectionMunicipios : municipiosCollection) {
                municipiosCollectionMunicipios.setIdDpto(null);
                municipiosCollectionMunicipios = em.merge(municipiosCollectionMunicipios);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
