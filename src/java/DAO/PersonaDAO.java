/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.Persona;
import Persistencia.PersonaJpaController;
import Persistencia.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class PersonaDAO {
    PersonaJpaController per=new PersonaJpaController();
    
    
    
    public void create(Persona p){
        try {
            per.create(p);
        } catch (Exception ex) {
            Logger.getLogger(PersonaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Persona> read(){
    return per.findPersonaEntities();
    }
    
    public Persona readPer(int cod){
        return per.findPersona(cod);
    }
    
    public void update(Persona mu){
        try {
            per.edit(mu);
        } catch (Exception ex) {
            Logger.getLogger(PersonaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public void delete(int cod){
        try {        
            per.destroy(cod);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PersonaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
