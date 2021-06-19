
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DTO.Municipios;
import Persistencia.MunicipiosJpaController;
import Persistencia.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class MunicipiosDAO {
    MunicipiosJpaController mun=new MunicipiosJpaController();
    
    
    
    public void create(Municipios m){
                mun.create(m);
    }
    
    public List<Municipios> read(){
    return mun.findMunicipiosEntities();
    }
    
    public Municipios readMun(int cod){
        return mun.findMunicipios(cod);
    }
    
    public void update(Municipios mu){
        try {
            mun.edit(mu);
        } catch (Exception ex) {
            Logger.getLogger(MunicipiosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
    
    public void delete(int cod){
        
        try {
            mun.destroy(cod);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MunicipiosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
