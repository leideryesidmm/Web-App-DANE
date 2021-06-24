/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;
import DAO.*;
import DTO.*;
import java.util.ArrayList;
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
            msg+="<option value='"+d.getIdDpto()+"'>"+d.getNombre()+"</option>";
        }
        
        return msg;
    }
    public String mostrarMunicipios(int iddepartamento){
        String msg="";
        List <Municipios> mns=filtramunicipios(iddepartamento);
        for(Municipios mun: mns){
            msg+="<option value='"+mun.getIdMunicipio()+"'>"+mun.getNombre()+"</option>";
        }
        
        return msg;
    }
    
    public static List<Municipios> filtramunicipios(int iddepartamento){
        List<Municipios> munfil=new ArrayList();
        MunicipiosDAO md=new MunicipiosDAO();
        List<Municipios> mun=md.read();
        
        for(Municipios m: mun){
            if(m.getIdDpto().getIdDpto().intValue()==iddepartamento){
                munfil.add(m);
            }
        }
        return munfil;
    }
    
    public boolean validarFormulario(int cedula, String nombre,String email,String direccion){
    boolean validacion=false;
    
    if(cedula>0&&cedula<999999999){
        if(!validarBase(cedula)){
            if(!nombre.equals("ingrese nombre")&&!email.equals("ingrese email")&&!direccion.equals("ingrese direccion")){
                validacion=true;
            }
        }
    }
    return validacion;
    }
    
    public boolean validarBase(int cedula){
        PersonaDAO pDAO=new PersonaDAO();
        Persona p=pDAO.readPer(cedula);
        if(p!=null){
        return true;
        }
        return false;
    }
    
    public void registrarPersona(int cedula, String nombre,String email,String direccion,int idmunicipio){
        PersonaDAO pDAO=new PersonaDAO();
        Persona p=new Persona();
        p.setCedula(cedula);
        p.setDireccion(direccion);
        p.setNombre(nombre);
        p.setEmail(email);
        Municipios m=new Municipios(idmunicipio);
        p.setIdMunicipio(m);
        
        pDAO.create(p);
        
    }
    public  String getTabla(int iddepartamento){
        String [][] m=getmunicipiospersonas(iddepartamento);
        String msg="<table style='margin: 0 auto' border='1'>"+
                   "<tr><th>Municipio</th>"+
                   "<th>Cantidad de Personas</th></tr>";
        for(int i=0;i<m.length;i++){
            msg+="<tr><td>"+m[i][0]+"</td>"+
                     "<td>"+m[i][1]+"</td></tr>";
        }
        return msg+"</table>";
    }
    public  String[][] getmunicipiospersonas(int iddepartamento){
        List<Municipios> mun=filtramunicipios(iddepartamento);
        String [][] m=new String[mun.size()][2];
        int i=0;
        for(Municipios mu: mun){
            m[i][0]=mu.getNombre();
            m[i][1]=contar(mu)+"";
            i++;
        }
        return m;
    }
    public  int contar(Municipios m){
        PersonaDAO p=new PersonaDAO();
        List<Persona> l=p.read();
        int cont=0;
        for(Persona per:l){
            if(per.getIdMunicipio().getIdMunicipio().equals(m.getIdMunicipio())){
                cont++;
            }
        }
        return cont;
    }
    public Persona validarPersona(String nombre){
        PersonaDAO p =new PersonaDAO();
        List <Persona> pers=p.read();
        for(Persona per: pers){
            if(per.getNombre().equals(nombre)){
                return per;
            }
        }
        return null;
    }
    public String getTablaPersona(Persona p){
    String tabla="<table style='margin: 0 auto' border='1'>"+
                   "<tr><th>Nombre</th>"+
                   "<th>E-mail</th>"+
                   "<th>Dirección</th>"+
                   "<th>Nombre del Municipio</th>"+
                   "<th>Nombre del Departamento</th></tr>";
        
            tabla+="<tr><td>"+p.getNombre()+"</td>"+
                   "<td>"+p.getEmail()+"</td>"+
                   "<td>"+p.getDireccion()+"</td>"+
                   "<td>"+p.getIdMunicipio().getNombre()+"</td>"+
                   "<td>"+p.getIdMunicipio().getIdDpto().getNombre()+"</td></tr>";
        
        return tabla+"</table>";
    }
    
    public static void main(String []arg){
        
        
    }
}
