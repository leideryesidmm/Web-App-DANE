/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.Departamento;
import Persistencia.DepartamentoJpaController;
import Persistencia.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class DepartamentoDAO {
    DepartamentoJpaController dp=new DepartamentoJpaController();
    
    
    
    public void create(Departamento d){
        try {
            dp.create(d);
        } catch (Exception ex) {
            Logger.getLogger(DepartamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Departamento> read(){
    return dp.findDepartamentoEntities();
    }
    
    public Departamento readDpt(int cod){
        return dp.findDepartamento(cod);
    }
    
    public void update(Departamento dpar){
        try {
            dp.edit(dpar);
        } catch (Exception ex) {
            Logger.getLogger(DepartamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public void delete(int cod){
        try {
            dp.destroy(cod);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DepartamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
