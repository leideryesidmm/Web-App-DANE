/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;
import DAO.*;
import DTO.*;
import java.util.List;
/**
 *
 * @author USER
 */
public class WebappDANE {

    public WebappDANE() {
    }
    
    public String mostrarDepartamentos(){
        String msg="";
        DepartamentoDAO dt=new DepartamentoDAO();
        List <Departamento> dts=dt.read();
        for(Departamento d: dts){
            msg+="<option value='id"+d.getIdDpto()+"'>"+d.getNombre()+"</option>";
        }
        
        return msg;
    }
    
    public static void main(String []arg){
        //System.out.println(mostrarDepartamentos());
    }
}
