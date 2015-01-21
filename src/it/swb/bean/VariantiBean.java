package it.swb.bean;

import it.swb.log.Log;
import it.swb.model.Variante_Articolo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CloseEvent;

@ManagedBean(name = "variantiBean")
@SessionScoped
public class VariantiBean  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static VariantiBean instance;
	
	public static synchronized VariantiBean getInstance() {
		if (instance == null)
		{
			synchronized(VariantiBean.class) {      //1
				VariantiBean inst = instance;         //2
				if (inst == null)
				{
					synchronized(VariantiBean.class) {  //3
						instance = new VariantiBean();
					}
					//instance = inst;               //5
				}
			}
		}
		return instance;
	}
	
	private List<Variante_Articolo> varianti;
	
	private boolean mostraVariante1 = false;
	private boolean mostraVariante2 = false;
	private boolean mostraVariante3 = false;
	private boolean mostraVariante4 = false;
	private boolean mostraVariante5 = false;
	private boolean mostraVariante6 = false;
	private boolean mostraVariante7 = false;
	private boolean mostraVariante8 = false;
	private boolean mostraVariante9 = false;
	private boolean mostraVariante10 = false;
	private boolean mostraVariante11 = false;
	private boolean mostraVariante12 = false;
	private boolean mostraVariante13 = false;
	private boolean mostraVariante14 = false;
	private boolean mostraVariante15 = false;
	private boolean mostraVariante16 = false;
	private boolean mostraVariante17 = false;
	private boolean mostraVariante18 = false;
	private boolean mostraVariante19 = false;
	private boolean mostraVariante20 = false;
	private boolean mostraVariante21 = false;
	private boolean mostraVariante22 = false;
	private boolean mostraVariante23 = false;
	private boolean mostraVariante24 = false;
	private boolean mostraVariante25 = false;
	private boolean mostraVariante26 = false;
	private boolean mostraVariante27 = false;
	private boolean mostraVariante28 = false;
	private boolean mostraVariante29 = false;
	private boolean mostraVariante30 = false;
	
	private String tipoVariante1;
	private String tipoVariante2;	
	private String tipoVariante3;
	private String tipoVariante4;
	private String tipoVariante5;
	private String tipoVariante6;
	private String tipoVariante7;
	private String tipoVariante8;
	private String tipoVariante9;
	private String tipoVariante10;
	private String tipoVariante11;
	private String tipoVariante12;
	private String tipoVariante13;
	private String tipoVariante14;
	private String tipoVariante15;
	private String tipoVariante16;
	private String tipoVariante17;
	private String tipoVariante18;
	private String tipoVariante19;
	private String tipoVariante20;
	private String tipoVariante21;
	private String tipoVariante22;
	private String tipoVariante23;
	private String tipoVariante24;
	private String tipoVariante25;
	private String tipoVariante26;
	private String tipoVariante27;
	private String tipoVariante28;
	private String tipoVariante29;
	private String tipoVariante30;
	
	private String valoreVariante1;
	private String valoreVariante2;
	private String valoreVariante3;
	private String valoreVariante4;
	private String valoreVariante5;
	private String valoreVariante6;
	private String valoreVariante7;
	private String valoreVariante8;
	private String valoreVariante9;
	private String valoreVariante10;
	private String valoreVariante11;
	private String valoreVariante12;
	private String valoreVariante13;
	private String valoreVariante14;
	private String valoreVariante15;
	private String valoreVariante16;
	private String valoreVariante17;
	private String valoreVariante18;
	private String valoreVariante19;
	private String valoreVariante20;
	private String valoreVariante21;
	private String valoreVariante22;
	private String valoreVariante23;
	private String valoreVariante24;
	private String valoreVariante25;
	private String valoreVariante26;
	private String valoreVariante27;
	private String valoreVariante28;
	private String valoreVariante29;
	private String valoreVariante30;
	
    private String immagineVariante1;
    private String immagineVariante2;
    private String immagineVariante3;
    private String immagineVariante4;
    private String immagineVariante5;
    private String immagineVariante6;
    private String immagineVariante7;
    private String immagineVariante8;
    private String immagineVariante9;
    private String immagineVariante10;
    private String immagineVariante11;
    private String immagineVariante12;
    private String immagineVariante13;
    private String immagineVariante14;
    private String immagineVariante15;
    private String immagineVariante16;
    private String immagineVariante17;
    private String immagineVariante18;
    private String immagineVariante19;
    private String immagineVariante20;
    private String immagineVariante21;
    private String immagineVariante22;
    private String immagineVariante23;
    private String immagineVariante24;
    private String immagineVariante25;
    private String immagineVariante26;
    private String immagineVariante27;
    private String immagineVariante28;
    private String immagineVariante29;
    private String immagineVariante30;
    
    private int quantitaVariante1;
    private int quantitaVariante2;
    private int quantitaVariante3;
    private int quantitaVariante4;
    private int quantitaVariante5;
    private int quantitaVariante6;
    private int quantitaVariante7;
    private int quantitaVariante8;
    private int quantitaVariante9;
    private int quantitaVariante10;
    private int quantitaVariante11;
    private int quantitaVariante12;
    private int quantitaVariante13;
    private int quantitaVariante14;
    private int quantitaVariante15;
    private int quantitaVariante16;
    private int quantitaVariante17;
    private int quantitaVariante18;
    private int quantitaVariante19;
    private int quantitaVariante20;
    private int quantitaVariante21;
    private int quantitaVariante22;
    private int quantitaVariante23;
    private int quantitaVariante24;
    private int quantitaVariante25;
    private int quantitaVariante26;
    private int quantitaVariante27;
    private int quantitaVariante28;
    private int quantitaVariante29;
    private int quantitaVariante30;
    
    private String codiceBarreVariante1;
    private String codiceBarreVariante2;
    private String codiceBarreVariante3;
    private String codiceBarreVariante4;
    private String codiceBarreVariante5;
    private String codiceBarreVariante6;
    private String codiceBarreVariante7;
    private String codiceBarreVariante8;
    private String codiceBarreVariante9;
    private String codiceBarreVariante10;
    private String codiceBarreVariante11;
    private String codiceBarreVariante12;
    private String codiceBarreVariante13;
    private String codiceBarreVariante14;
    private String codiceBarreVariante15;
    private String codiceBarreVariante16;
    private String codiceBarreVariante17;
    private String codiceBarreVariante18;
    private String codiceBarreVariante19;
    private String codiceBarreVariante20;
    private String codiceBarreVariante21;
    private String codiceBarreVariante22;
    private String codiceBarreVariante23;
    private String codiceBarreVariante24;
    private String codiceBarreVariante25;
    private String codiceBarreVariante26;
    private String codiceBarreVariante27;
    private String codiceBarreVariante28;
    private String codiceBarreVariante29;
    private String codiceBarreVariante30;
    
    private String dimensioniVariante1;
    private String dimensioniVariante2;
    private String dimensioniVariante3;
    private String dimensioniVariante4;
    private String dimensioniVariante5;
    private String dimensioniVariante6;
    private String dimensioniVariante7;
    private String dimensioniVariante8;
    private String dimensioniVariante9;
    private String dimensioniVariante10;
    private String dimensioniVariante11;
    private String dimensioniVariante12;
    private String dimensioniVariante13;
    private String dimensioniVariante14;
    private String dimensioniVariante15;
    private String dimensioniVariante16;
    private String dimensioniVariante17;
    private String dimensioniVariante18;
    private String dimensioniVariante19;
    private String dimensioniVariante20;
    private String dimensioniVariante21;
    private String dimensioniVariante22;
    private String dimensioniVariante23;
    private String dimensioniVariante24;
    private String dimensioniVariante25;
    private String dimensioniVariante26;
    private String dimensioniVariante27;
    private String dimensioniVariante28;
    private String dimensioniVariante29;
    private String dimensioniVariante30;
    
    public void handleClose(CloseEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");            
        addMessage(message);  
    }  
            
    private void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
    
    public void popolaVarianti(){
    	Log.debug("popolaVarianti ");
    	hideAll();
    	
    	if (varianti!=null && !varianti.isEmpty()){    		
    		for (int i=0;i<varianti.size();i++){
    			if (i==0){
	    			tipoVariante1 = varianti.get(i).getTipo();
	    			valoreVariante1 = varianti.get(i).getValore();
	    			immagineVariante1 = varianti.get(i).getImmagine();
	    			quantitaVariante1 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante1 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante1 = varianti.get(i).getCodiceBarre();
	    			mostraVariante1 = true;
    			} else if (i==1){
	    			tipoVariante2 = varianti.get(i).getTipo();
	    			valoreVariante2 = varianti.get(i).getValore();
	    			immagineVariante2 = varianti.get(i).getImmagine();
	    			quantitaVariante2 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante2 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante2 = varianti.get(i).getCodiceBarre();
	    			mostraVariante2 = true;
    			} else if (i==2){
	    			tipoVariante3 = varianti.get(i).getTipo();
	    			valoreVariante3 = varianti.get(i).getValore();
	    			immagineVariante3 = varianti.get(i).getImmagine();
	    			quantitaVariante3 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante3 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante3 = varianti.get(i).getCodiceBarre();
	    			mostraVariante3 = true;
    			} else if (i==3){
	    			tipoVariante4 = varianti.get(i).getTipo();
	    			valoreVariante4 = varianti.get(i).getValore();
	    			immagineVariante4 = varianti.get(i).getImmagine();
	    			quantitaVariante4 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante4 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante4 = varianti.get(i).getCodiceBarre();
	    			mostraVariante4 = true;
    			} else if (i==4){
	    			tipoVariante5 = varianti.get(i).getTipo();
	    			valoreVariante5 = varianti.get(i).getValore();
	    			immagineVariante5 = varianti.get(i).getImmagine();
	    			quantitaVariante5 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante5 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante5 = varianti.get(i).getCodiceBarre();
	    			mostraVariante5 = true;
    			} else if (i==5){
	    			tipoVariante6 = varianti.get(i).getTipo();
	    			valoreVariante6 = varianti.get(i).getValore();
	    			immagineVariante6 = varianti.get(i).getImmagine();
	    			quantitaVariante6 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante6 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante6 = varianti.get(i).getCodiceBarre();
	    			mostraVariante6 = true;
    			} else if (i==6){
	    			tipoVariante7 = varianti.get(i).getTipo();
	    			valoreVariante7 = varianti.get(i).getValore();
	    			immagineVariante7 = varianti.get(i).getImmagine();
	    			quantitaVariante7 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante7 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante7 = varianti.get(i).getCodiceBarre();
	    			mostraVariante7 = true;
    			} else if (i==7){
	    			tipoVariante8 = varianti.get(i).getTipo();
	    			valoreVariante8 = varianti.get(i).getValore();
	    			immagineVariante8 = varianti.get(i).getImmagine();
	    			quantitaVariante8 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante8 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante8 = varianti.get(i).getCodiceBarre();
	    			mostraVariante8 = true;
    			} else if (i==8){
	    			tipoVariante9 = varianti.get(i).getTipo();
	    			valoreVariante9 = varianti.get(i).getValore();
	    			immagineVariante9 = varianti.get(i).getImmagine();
	    			quantitaVariante9 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante9 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante9 = varianti.get(i).getCodiceBarre();
	    			mostraVariante9 = true;
    			} else if (i==9){
	    			tipoVariante10 = varianti.get(i).getTipo();
	    			valoreVariante10 = varianti.get(i).getValore();
	    			immagineVariante10 = varianti.get(i).getImmagine();
	    			quantitaVariante10 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante10 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante10 = varianti.get(i).getCodiceBarre();
	    			mostraVariante10 = true;
    			} else if (i==10){
	    			tipoVariante11 = varianti.get(i).getTipo();
	    			valoreVariante11 = varianti.get(i).getValore();
	    			immagineVariante11 = varianti.get(i).getImmagine();
	    			quantitaVariante11 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante11 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante11 = varianti.get(i).getCodiceBarre();
	    			mostraVariante11 = true;
    			} else if (i==11){
	    			tipoVariante12 = varianti.get(i).getTipo();
	    			valoreVariante12 = varianti.get(i).getValore();
	    			immagineVariante12 = varianti.get(i).getImmagine();
	    			quantitaVariante12 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante12 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante12 = varianti.get(i).getCodiceBarre();
	    			mostraVariante12 = true;
    			} else if (i==12){
	    			tipoVariante13 = varianti.get(i).getTipo();
	    			valoreVariante13 = varianti.get(i).getValore();
	    			immagineVariante13 = varianti.get(i).getImmagine();
	    			quantitaVariante13 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante13 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante13 = varianti.get(i).getCodiceBarre();
	    			mostraVariante13 = true;
    			} else if (i==13){
	    			tipoVariante14 = varianti.get(i).getTipo();
	    			valoreVariante14 = varianti.get(i).getValore();
	    			immagineVariante14 = varianti.get(i).getImmagine();
	    			quantitaVariante14 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante14 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante14 = varianti.get(i).getCodiceBarre();
	    			mostraVariante14 = true;
    			} else if (i==14){
	    			tipoVariante15 = varianti.get(i).getTipo();
	    			valoreVariante15 = varianti.get(i).getValore();
	    			immagineVariante15 = varianti.get(i).getImmagine();
	    			quantitaVariante15 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante15 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante15 = varianti.get(i).getCodiceBarre();
	    			mostraVariante15 = true;
    			} else if (i==15){
	    			tipoVariante16 = varianti.get(i).getTipo();
	    			valoreVariante16 = varianti.get(i).getValore();
	    			immagineVariante16 = varianti.get(i).getImmagine();
	    			quantitaVariante16 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante16 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante16 = varianti.get(i).getCodiceBarre();
	    			mostraVariante16 = true;
    			} else if (i==16){
	    			tipoVariante17 = varianti.get(i).getTipo();
	    			valoreVariante17 = varianti.get(i).getValore();
	    			immagineVariante17 = varianti.get(i).getImmagine();
	    			quantitaVariante17 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante17 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante17 = varianti.get(i).getCodiceBarre();
	    			mostraVariante17 = true;
    			} else if (i==17){
	    			tipoVariante18 = varianti.get(i).getTipo();
	    			valoreVariante18 = varianti.get(i).getValore();
	    			immagineVariante18 = varianti.get(i).getImmagine();
	    			quantitaVariante18 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante18 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante18 = varianti.get(i).getCodiceBarre();
	    			mostraVariante18 = true;
    			} else if (i==18){
	    			tipoVariante19 = varianti.get(i).getTipo();
	    			valoreVariante19 = varianti.get(i).getValore();
	    			immagineVariante19 = varianti.get(i).getImmagine();
	    			quantitaVariante19 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante19 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante19 = varianti.get(i).getCodiceBarre();
	    			mostraVariante19 = true;
    			} else if (i==19){
	    			tipoVariante20 = varianti.get(i).getTipo();
	    			valoreVariante20 = varianti.get(i).getValore();
	    			immagineVariante20 = varianti.get(i).getImmagine();
	    			quantitaVariante20 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante20 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante20 = varianti.get(i).getCodiceBarre();
	    			mostraVariante20 = true;
    			} else if (i==20){
	    			tipoVariante21 = varianti.get(i).getTipo();
	    			valoreVariante21 = varianti.get(i).getValore();
	    			immagineVariante21 = varianti.get(i).getImmagine();
	    			quantitaVariante21 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante21 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante21 = varianti.get(i).getCodiceBarre();
	    			mostraVariante21 = true;
    			} else if (i==21){
	    			tipoVariante22 = varianti.get(i).getTipo();
	    			valoreVariante22 = varianti.get(i).getValore();
	    			immagineVariante22 = varianti.get(i).getImmagine();
	    			quantitaVariante22 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante22 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante22 = varianti.get(i).getCodiceBarre();
	    			mostraVariante22 = true;
    			} else if (i==22){
	    			tipoVariante23 = varianti.get(i).getTipo();
	    			valoreVariante23 = varianti.get(i).getValore();
	    			immagineVariante23 = varianti.get(i).getImmagine();
	    			quantitaVariante23 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante23 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante23 = varianti.get(i).getCodiceBarre();
	    			mostraVariante23 = true;
    			} else if (i==23){
	    			tipoVariante24 = varianti.get(i).getTipo();
	    			valoreVariante24 = varianti.get(i).getValore();
	    			immagineVariante24 = varianti.get(i).getImmagine();
	    			quantitaVariante24 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante24 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante24 = varianti.get(i).getCodiceBarre();
	    			mostraVariante24 = true;
    			} else if (i==24){
	    			tipoVariante25 = varianti.get(i).getTipo();
	    			valoreVariante25 = varianti.get(i).getValore();
	    			immagineVariante25 = varianti.get(i).getImmagine();
	    			quantitaVariante25 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante25 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante25 = varianti.get(i).getCodiceBarre();
	    			mostraVariante25 = true;
    			} else if (i==25){
	    			tipoVariante26 = varianti.get(i).getTipo();
	    			valoreVariante26 = varianti.get(i).getValore();
	    			immagineVariante26 = varianti.get(i).getImmagine();
	    			quantitaVariante26 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante26 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante26 = varianti.get(i).getCodiceBarre();
	    			mostraVariante26 = true;
    			} else if (i==26){
	    			tipoVariante27 = varianti.get(i).getTipo();
	    			valoreVariante27 = varianti.get(i).getValore();
	    			immagineVariante27 = varianti.get(i).getImmagine();
	    			quantitaVariante27 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante27 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante27 = varianti.get(i).getCodiceBarre();
	    			mostraVariante27 = true;
    			} else if (i==27){
	    			tipoVariante28 = varianti.get(i).getTipo();
	    			valoreVariante28 = varianti.get(i).getValore();
	    			immagineVariante28 = varianti.get(i).getImmagine();
	    			quantitaVariante28 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante28 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante28 = varianti.get(i).getCodiceBarre();
	    			mostraVariante28 = true;
    			} else if (i==28){
	    			tipoVariante29 = varianti.get(i).getTipo();
	    			valoreVariante29 = varianti.get(i).getValore();
	    			immagineVariante29 = varianti.get(i).getImmagine();
	    			quantitaVariante29 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante29 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante29 = varianti.get(i).getCodiceBarre();
	    			mostraVariante29 = true;
    			} else if (i==29){
	    			tipoVariante30 = varianti.get(i).getTipo();
	    			valoreVariante30 = varianti.get(i).getValore();
	    			immagineVariante30 = varianti.get(i).getImmagine();
	    			quantitaVariante30 =  varianti.get(i).getQuantita();
	    			codiceBarreVariante30 = varianti.get(i).getCodiceBarre();
	    			dimensioniVariante30 = varianti.get(i).getCodiceBarre();
	    			mostraVariante30 = true;
    			}
    		}
    	}
    }
    
    
    public void creaVarianti(){
    	Log.debug("creaVarianti");
    	
    	if (varianti!=null) varianti.clear();
    	else varianti = new ArrayList<Variante_Articolo>();
    	
    	if (valoreVariante1!=null && !valoreVariante1.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante1,valoreVariante1,immagineVariante1,quantitaVariante1,codiceBarreVariante1,dimensioniVariante1);   		
    		varianti.add(v);
    		System.out.println("var 1 ok");
    	} else System.out.println("var 1 NO");
    	if (valoreVariante2!=null && !valoreVariante2.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante2,valoreVariante2,immagineVariante2,quantitaVariante2,codiceBarreVariante2,dimensioniVariante2);
    		varianti.add(v);
    		System.out.println("var 2 ok");
    	} else System.out.println("var 2 NO");
    	if (valoreVariante3!=null && !valoreVariante3.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante3,valoreVariante3,immagineVariante3,quantitaVariante3,codiceBarreVariante3,dimensioniVariante3);
    		varianti.add(v);
    	}
    	if (valoreVariante4!=null && !valoreVariante4.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante4,valoreVariante4,immagineVariante4,quantitaVariante4,codiceBarreVariante4,dimensioniVariante4);
    		varianti.add(v);
    	}
    	if (valoreVariante5!=null && !valoreVariante5.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante5,valoreVariante5,immagineVariante5,quantitaVariante5,codiceBarreVariante5,dimensioniVariante5);
    		varianti.add(v);
    	}
    	if (valoreVariante6!=null && !valoreVariante6.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante6,valoreVariante6,immagineVariante6,quantitaVariante6,codiceBarreVariante6,dimensioniVariante6);
    		varianti.add(v);
    	}
    	if (valoreVariante7!=null && !valoreVariante7.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante7,valoreVariante7,immagineVariante7,quantitaVariante7,codiceBarreVariante7,dimensioniVariante7);
    		varianti.add(v);
    	}
    	if (valoreVariante8!=null && !valoreVariante8.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante8,valoreVariante8,immagineVariante8,quantitaVariante8,codiceBarreVariante8,dimensioniVariante8);
    		varianti.add(v);
    	}
    	if (valoreVariante9!=null && !valoreVariante9.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante9,valoreVariante9,immagineVariante9,quantitaVariante9,codiceBarreVariante9,dimensioniVariante9);
    		varianti.add(v);
    	}
    	if (valoreVariante10!=null && !valoreVariante10.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante10,valoreVariante10,immagineVariante10,quantitaVariante10,codiceBarreVariante10,dimensioniVariante10);
    		varianti.add(v);
    	}
    	if (valoreVariante11!=null && !valoreVariante11.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante11,valoreVariante11,immagineVariante11,quantitaVariante11,codiceBarreVariante11,dimensioniVariante11);
    		varianti.add(v);
    	}
    	if (valoreVariante12!=null && !valoreVariante12.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante12,valoreVariante12,immagineVariante12,quantitaVariante12,codiceBarreVariante12,dimensioniVariante12);
    		varianti.add(v);
    	}
    	if (valoreVariante13!=null && !valoreVariante13.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante13,valoreVariante13,immagineVariante13,quantitaVariante13,codiceBarreVariante13,dimensioniVariante13);
    		varianti.add(v);
    	}
    	if (valoreVariante14!=null && !valoreVariante14.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante14,valoreVariante14,immagineVariante14,quantitaVariante14,codiceBarreVariante14,dimensioniVariante14);
    		varianti.add(v);
    	}
    	if (valoreVariante15!=null && !valoreVariante15.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante15,valoreVariante15,immagineVariante15,quantitaVariante15,codiceBarreVariante15,dimensioniVariante15);
    		varianti.add(v);
    	}
    	if (valoreVariante16!=null && !valoreVariante16.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante16,valoreVariante16,immagineVariante16,quantitaVariante16,codiceBarreVariante16,dimensioniVariante16);
    		varianti.add(v);
    	}
    	if (valoreVariante17!=null && !valoreVariante17.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante17,valoreVariante17,immagineVariante17,quantitaVariante17,codiceBarreVariante17,dimensioniVariante17);
    		varianti.add(v);
    	}
    	if (valoreVariante18!=null && !valoreVariante18.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante18,valoreVariante18,immagineVariante18,quantitaVariante18,codiceBarreVariante18,dimensioniVariante18);
    		varianti.add(v);
    	}
    	if (valoreVariante19!=null && !valoreVariante19.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante19,valoreVariante19,immagineVariante19,quantitaVariante19,codiceBarreVariante19,dimensioniVariante19);
    		varianti.add(v);
    	}
    	if (valoreVariante20!=null && !valoreVariante20.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante20,valoreVariante20,immagineVariante20,quantitaVariante20,codiceBarreVariante20,dimensioniVariante20);
    		varianti.add(v);
    	}
    	if (valoreVariante21!=null && !valoreVariante21.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante21,valoreVariante21,immagineVariante21,quantitaVariante21,codiceBarreVariante21,dimensioniVariante21);
    		varianti.add(v);
    	}
    	if (valoreVariante22!=null && !valoreVariante22.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante22,valoreVariante22,immagineVariante22,quantitaVariante22,codiceBarreVariante22,dimensioniVariante22);
    		varianti.add(v);
    	}
    	if (valoreVariante23!=null && !valoreVariante23.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante23,valoreVariante23,immagineVariante23,quantitaVariante23,codiceBarreVariante23,dimensioniVariante23);
    		varianti.add(v);
    	}
    	if (valoreVariante24!=null && !valoreVariante24.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante24,valoreVariante24,immagineVariante24,quantitaVariante24,codiceBarreVariante24,dimensioniVariante24);
    		varianti.add(v);
    	}
    	if (valoreVariante25!=null && !valoreVariante25.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante25,valoreVariante25,immagineVariante25,quantitaVariante25,codiceBarreVariante25,dimensioniVariante25);
    		varianti.add(v);
    	}
    	if (valoreVariante26!=null && !valoreVariante26.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante26,valoreVariante26,immagineVariante26,quantitaVariante26,codiceBarreVariante26,dimensioniVariante26);
    		varianti.add(v);
    	}
    	if (valoreVariante27!=null && !valoreVariante27.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante27,valoreVariante27,immagineVariante27,quantitaVariante27,codiceBarreVariante27,dimensioniVariante27);
    		varianti.add(v);
    	}
    	if (valoreVariante28!=null && !valoreVariante28.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante28,valoreVariante28,immagineVariante28,quantitaVariante28,codiceBarreVariante28,dimensioniVariante28);
    		varianti.add(v);
    	}
    	if (valoreVariante29!=null && !valoreVariante29.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante29,valoreVariante29,immagineVariante29,quantitaVariante29,codiceBarreVariante29,dimensioniVariante29);
    		varianti.add(v);
    	}
    	if (valoreVariante30!=null && !valoreVariante30.trim().isEmpty()){
    		Variante_Articolo v = new Variante_Articolo(tipoVariante30,valoreVariante30,immagineVariante30,quantitaVariante30,codiceBarreVariante30,dimensioniVariante30);
    		varianti.add(v);
    	}
    	
//    	FacesContext facesContext = FacesContext.getCurrentInstance();
//    	HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true); 	  	
//    	session.removeAttribute("variantiArticolo");  
//    	session.setAttribute("variantiArticolo", varianti);   
    }
    
    
    public void hideAll(){
    	hideVariante1();
    	hideVariante2();
    	hideVariante3();
    	hideVariante4();
    	hideVariante5();
    	hideVariante6();
    	hideVariante7();
    	hideVariante8();
    	hideVariante9();
    	hideVariante10();
    	hideVariante11();
    	hideVariante12();
    	hideVariante13();
    	hideVariante14();
    	hideVariante15();
    	hideVariante16();
    	hideVariante17();
    	hideVariante18();
    	hideVariante19();
    	hideVariante20();
    	hideVariante21();
    	hideVariante22();
    	hideVariante23();
    	hideVariante24();
    	hideVariante25();
    	hideVariante26();
    	hideVariante27();
    	hideVariante28();
    	hideVariante29();
    	hideVariante30();    	
    }
    
	public void hideVariante(CloseEvent event) {
		if(event!=null){
			String var = event.getComponent().getId();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Variante Eliminata", "id:'" + var + "'");            
	        addMessage(message); 
	        int numVar = Integer.valueOf(var.replace("panelVariante", ""));
			hideVar(numVar);
		}				
	}
	
	private void hideVar(int n){
		System.out.println("hideVar n="+n);
		if (n==1){
			System.out.println("hidevar 1");
			mostraVariante1 = false;
			tipoVariante1 = null;
			valoreVariante1 = null;
			immagineVariante1 = null;
			quantitaVariante1 = 0;
			codiceBarreVariante1 = null;
			dimensioniVariante1 = null;	
		}
		else if (n==2){
			System.out.println("hidevar 2");
			mostraVariante2 = false;
			tipoVariante2 = null;
			valoreVariante2 = null;
			immagineVariante2 = null;
			quantitaVariante2 = 0;
			codiceBarreVariante2 = null;
			dimensioniVariante2 = null;	
		}
		else if (n==3){
			System.out.println("hidevar 3");
			mostraVariante3 = false;
			tipoVariante3 = null;
			valoreVariante3 = null;
			immagineVariante3 = null;
			quantitaVariante3 = 0;
			codiceBarreVariante3 = null;
			dimensioniVariante3 = null;	
		}
		else if (n==4){
			mostraVariante4 = false;
			tipoVariante4 = "";
			valoreVariante4 = "";
			immagineVariante4 = "";
			quantitaVariante4 = 0;
			codiceBarreVariante4 = "";
			dimensioniVariante4 = "";	
		}
		else if (n==5){
			mostraVariante5 = false;
			tipoVariante5 = "";
			valoreVariante5 = "";
			immagineVariante5 = "";
			quantitaVariante5 = 0;
			codiceBarreVariante5 = "";
			dimensioniVariante5 = "";	
		}
		else if (n==6){
			mostraVariante6 = false;
			tipoVariante6 = "";
			valoreVariante6 = "";
			immagineVariante6 = "";
			quantitaVariante6 = 0;
			codiceBarreVariante6 = "";
			dimensioniVariante6 = "";	
		}
		else if (n==7){
			mostraVariante7 = false;
			tipoVariante7 = "";
			valoreVariante7 = "";
			immagineVariante7 = "";
			quantitaVariante7 = 0;
			codiceBarreVariante7 = "";
			dimensioniVariante7 = "";	
		}
		else if (n==8){
			mostraVariante8 = false;
			tipoVariante8 = "";
			valoreVariante8 = "";
			immagineVariante8 = "";
			quantitaVariante8 = 0;
			codiceBarreVariante8 = "";
			dimensioniVariante8 = "";	
		}
		else if (n==9){
			mostraVariante9 = false;
			tipoVariante9 = "";
			valoreVariante9 = "";
			immagineVariante9 = "";
			quantitaVariante9 = 0;
			codiceBarreVariante9 = "";
			dimensioniVariante9 = "";	
		}
		else if (n==10){
			mostraVariante10 = false;
			tipoVariante10 = "";
			valoreVariante10 = "";
			immagineVariante10 = "";
			quantitaVariante10 = 0;
			codiceBarreVariante10 = "";
			dimensioniVariante10 = "";	
		}
	}
	
	public void showVariante1() {
		Log.debug("showVariante1");
		mostraVariante1 = true;
	}
    
	public void hideVariante1() {
		//Log.debug("hideVariante1");
		mostraVariante1 = false;
		tipoVariante1 = "";
		valoreVariante1 = "";
		immagineVariante1 = "";
		quantitaVariante1 = 0;
		codiceBarreVariante1 = "";
		dimensioniVariante1 = "";
	}
	
	public void showVariante2() {
		Log.debug("showVariante2");
		mostraVariante2 = true;
	}
	
	public void hideVariante2() {
		//Log.debug("hideVariante2");
		mostraVariante2 = false;
		tipoVariante2 = "";
		valoreVariante2 = "";
		immagineVariante2 = "";
		quantitaVariante2 = 0;
		codiceBarreVariante2 = "";
		dimensioniVariante2 = "";
	}
	
	public void showVariante3() {
		Log.debug("showVariante3");
		mostraVariante3 = true;
	}
	
	public void hideVariante3() {
		//Log.debug("hideVariante3");
		mostraVariante3 = false;
		tipoVariante3 = "";
		valoreVariante3 = "";
		immagineVariante3 = "";
		quantitaVariante3 = 0;
		codiceBarreVariante3 = "";
		dimensioniVariante3 = "";
	}
	
	public void showVariante4() {
		Log.debug("showVariante4");
		mostraVariante4 = true;
	}
	
	public void hideVariante4() {
		//Log.debug("hideVariante4");
		mostraVariante4 = false;
		tipoVariante4 = "";
		valoreVariante4 = "";
		immagineVariante4 = "";
		quantitaVariante4 = 0;
		codiceBarreVariante4 = "";
		dimensioniVariante4 = "";
	}
	
	public void showVariante5() {
		Log.debug("showVariante5");
		mostraVariante5 = true;
	}
	
	public void hideVariante5() {
		//Log.debug("hideVariante5");
		mostraVariante5 = false;
		tipoVariante5 = "";
		valoreVariante5 = "";
		immagineVariante5 = "";
		quantitaVariante5 = 0;
		codiceBarreVariante5 = "";
		dimensioniVariante5 = "";
	}
	
	public void showVariante6() {
		Log.debug("showVariante6");
		mostraVariante6 = true;
	}
	
	public void hideVariante6() {
		//Log.debug("hideVariante6");
		mostraVariante6 = false;
		tipoVariante6 = "";
		valoreVariante6 = "";
		immagineVariante6 = "";
		quantitaVariante6 = 0;
		codiceBarreVariante6 = "";
		dimensioniVariante6 = "";
	}
	
	public void showVariante7() {
		Log.debug("showVariante7");
		mostraVariante7 = true;
	}
	
	public void hideVariante7() {
		//Log.debug("hideVariante7");
		mostraVariante7 = false;
		tipoVariante7 = "";
		valoreVariante7 = "";
		immagineVariante7 = "";
		quantitaVariante7 = 0;
		codiceBarreVariante7 = "";
		dimensioniVariante7 = "";
	}
	
	public void showVariante8() {
		Log.debug("showVariante8");
		mostraVariante8 = true;
	}
	
	public void hideVariante8() {
		//Log.debug("hideVariante8");
		mostraVariante8 = false;
		tipoVariante8 = "";
		valoreVariante8 = "";
		immagineVariante8 = "";
		quantitaVariante8 = 0;
		codiceBarreVariante8 = "";
		dimensioniVariante8 = "";
	}
	
	public void showVariante9() {
		Log.debug("showVariante9");
		mostraVariante9 = true;
	}
	
	public void hideVariante9() {
		//Log.debug("hideVariante9");
		mostraVariante9 = false;
		tipoVariante9 = "";
		valoreVariante9 = "";
		immagineVariante9 = "";
		quantitaVariante9 = 0;
		codiceBarreVariante9 = "";
		dimensioniVariante9 = "";
	}
	
	public void showVariante10() {
		Log.debug("showVariante10");
		mostraVariante10 = true;
	}
	
	public void hideVariante10() {
		//Log.debug("hideVariante10");
		mostraVariante10 = false;
		tipoVariante10 = "";
		valoreVariante10 = "";
		immagineVariante10 = "";
		quantitaVariante10 = 0;
		codiceBarreVariante10 = "";
		dimensioniVariante10 = "";
	}
	
	public void showVariante11() {
		Log.debug("showVariante11");
		mostraVariante11 = true;
	}
	
	public void hideVariante11() {
		//Log.debug("hideVariante11");
		mostraVariante11 = false;
		tipoVariante11 = "";
		valoreVariante11 = "";
		immagineVariante11 = "";
		quantitaVariante11 = 0;
		codiceBarreVariante11 = "";
		dimensioniVariante11 = "";
	}
	
	public void showVariante12() {
		Log.debug("showVariante12");
		mostraVariante12 = true;
	}
	
	public void hideVariante12() {
		//Log.debug("hideVariante12");
		mostraVariante12 = false;
		tipoVariante12 = "";
		valoreVariante12 = "";
		immagineVariante12 = "";
		quantitaVariante12 = 0;
		codiceBarreVariante12 = "";
		dimensioniVariante12 = "";
	}
	
	public void showVariante13() {
		Log.debug("showVariante13");
		mostraVariante13 = true;
	}
	
	public void hideVariante13() {
		//Log.debug("hideVariante13");
		mostraVariante13 = false;
		tipoVariante13 = "";
		valoreVariante13 = "";
		immagineVariante13 = "";
		quantitaVariante13 = 0;
		codiceBarreVariante13 = "";
		dimensioniVariante13 = "";
	}
	
	public void showVariante14() {
		Log.debug("showVariante14");
		mostraVariante14 = true;
	}
	
	public void hideVariante14() {
		//Log.debug("hideVariante14");
		mostraVariante14 = false;
		tipoVariante14 = "";
		valoreVariante14 = "";
		immagineVariante14 = "";
		quantitaVariante14 = 0;
		codiceBarreVariante14 = "";
		dimensioniVariante14 = "";
	}
	
	public void showVariante15() {
		Log.debug("showVariante15");
		mostraVariante15 = true;
	}
	
	public void hideVariante15() {
		//Log.debug("hideVariante15");
		mostraVariante15 = false;
		tipoVariante15 = "";
		valoreVariante15 = "";
		immagineVariante15 = "";
		quantitaVariante15 = 0;
		codiceBarreVariante15 = "";
		dimensioniVariante15 = "";
	}
	
	public void showVariante16() {
		Log.debug("showVariante16");
		mostraVariante16 = true;
	}
	
	public void hideVariante16() {
		//Log.debug("hideVariante16");
		mostraVariante16 = false;
		tipoVariante16 = "";
		valoreVariante16 = "";
		immagineVariante16 = "";
		quantitaVariante16 = 0;
		codiceBarreVariante16 = "";
		dimensioniVariante16 = "";
	}
	
	public void showVariante17() {
		Log.debug("showVariante17");
		mostraVariante17 = true;
	}
	
	public void hideVariante17() {
		//Log.debug("hideVariante17");
		mostraVariante17 = false;
		tipoVariante17 = "";
		valoreVariante17 = "";
		immagineVariante17 = "";
		quantitaVariante17 = 0;
		codiceBarreVariante17 = "";
		dimensioniVariante17 = "";
	}
	
	public void showVariante18() {
		Log.debug("showVariante18");
		mostraVariante18 = true;
	}
	
	public void hideVariante18() {
		//Log.debug("hideVariante18");
		mostraVariante18 = false;
		tipoVariante18 = "";
		valoreVariante18 = "";
		immagineVariante18 = "";
		quantitaVariante18 = 0;
		codiceBarreVariante18 = "";
		dimensioniVariante18 = "";
	}
	
	public void showVariante19() {
		Log.debug("showVariante19");
		mostraVariante19 = true;
	}
	
	public void hideVariante19() {
		//Log.debug("hideVariante19");
		mostraVariante19 = false;
		tipoVariante19 = "";
		valoreVariante19 = "";
		immagineVariante19 = "";
		quantitaVariante19 = 0;
		codiceBarreVariante19 = "";
		dimensioniVariante19 = "";
	}
	
	public void showVariante20() {
		Log.debug("showVariante20");
		mostraVariante20 = true;
	}
	
	public void hideVariante20() {
		//Log.debug("hideVariante20");
		mostraVariante20 = false;
		tipoVariante20 = "";
		valoreVariante20 = "";
		immagineVariante20 = "";
		quantitaVariante20 = 0;
		codiceBarreVariante20 = "";
		dimensioniVariante20 = "";
	}
	
	public void showVariante21() {
		Log.debug("showVariante21");
		mostraVariante21 = true;
	}
	
	public void hideVariante21() {
		//Log.debug("hideVariante21");
		mostraVariante21 = false;
		tipoVariante21 = "";
		valoreVariante21 = "";
		immagineVariante21 = "";
		quantitaVariante21 = 0;
		codiceBarreVariante21 = "";
		dimensioniVariante21 = "";
	}
	
	public void showVariante22() {
		Log.debug("showVariante22");
		mostraVariante22 = true;
	}
	
	public void hideVariante22() {
		//Log.debug("hideVariante22");
		mostraVariante22 = false;
		tipoVariante22 = "";
		valoreVariante22 = "";
		immagineVariante22 = "";
		quantitaVariante22 = 0;
		codiceBarreVariante22 = "";
		dimensioniVariante22 = "";
	}
	
	public void showVariante23() {
		Log.debug("showVariante23");
		mostraVariante23 = true;
	}
	
	public void hideVariante23() {
		//Log.debug("hideVariante23");
		mostraVariante23 = false;
		tipoVariante23 = "";
		valoreVariante23 = "";
		immagineVariante23 = "";
		quantitaVariante23 = 0;
		codiceBarreVariante23 = "";
		dimensioniVariante23 = "";
	}
	
	public void showVariante24() {
		Log.debug("showVariante24");
		mostraVariante24 = true;
	}
	
	public void hideVariante24() {
		//Log.debug("hideVariante24");
		mostraVariante24 = false;
		tipoVariante24 = "";
		valoreVariante24 = "";
		immagineVariante24 = "";
		quantitaVariante24 = 0;
		codiceBarreVariante24 = "";
		dimensioniVariante24 = "";
	}
	
	public void showVariante25() {
		Log.debug("showVariante25");
		mostraVariante25 = true;
	}
	
	public void hideVariante25() {
		//Log.debug("hideVariante25");
		mostraVariante25 = false;
		tipoVariante25 = "";
		valoreVariante25 = "";
		immagineVariante25 = "";
		quantitaVariante25 = 0;
		codiceBarreVariante25 = "";
		dimensioniVariante25 = "";
	}
	
	public void showVariante26() {
		Log.debug("showVariante26");
		mostraVariante26 = true;
	}
	
	public void hideVariante26() {
		//Log.debug("hideVariante26");
		mostraVariante26 = false;
		tipoVariante26 = "";
		valoreVariante26 = "";
		immagineVariante26 = "";
		quantitaVariante26 = 0;
		codiceBarreVariante26 = "";
		dimensioniVariante26 = "";
	}
	
	public void showVariante27() {
		Log.debug("showVariante27");
		mostraVariante27 = true;
	}
	
	public void hideVariante27() {
		//Log.debug("hideVariante27");
		mostraVariante27 = false;
		tipoVariante27 = "";
		valoreVariante27 = "";
		immagineVariante27 = "";
		quantitaVariante27 = 0;
		codiceBarreVariante27 = "";
		dimensioniVariante27 = "";
	}
	
	public void showVariante28() {
		Log.debug("showVariante28");
		mostraVariante28 = true;
	}
	
	public void hideVariante28() {
		//Log.debug("hideVariante28");
		mostraVariante28 = false;
		tipoVariante28 = "";
		valoreVariante28 = "";
		immagineVariante28 = "";
		quantitaVariante28 = 0;
		codiceBarreVariante28 = "";
		dimensioniVariante28 = "";
	}
	
	public void showVariante29() {
		Log.debug("showVariante29");
		mostraVariante29 = true;
	}
	
	public void hideVariante29() {
		//Log.debug("hideVariante29");
		mostraVariante29 = false;
		tipoVariante29 = "";
		valoreVariante29 = "";
		immagineVariante29 = "";
		quantitaVariante29 = 0;
		codiceBarreVariante29 = "";
		dimensioniVariante29 = "";
	}
	
	public void showVariante30() {
		Log.debug("showVariante30");
		mostraVariante30 = true;
	}
	
	public void hideVariante30() {
		//Log.debug("hideVariante30");
		mostraVariante30 = false;
		tipoVariante30 = "";
		valoreVariante30 = "";
		immagineVariante30 = "";
		quantitaVariante30 = 0;
		codiceBarreVariante30 = "";
		dimensioniVariante30 = "";
	}

	public boolean isMostraVariante2() {
		return mostraVariante2;
	}

	public void setMostraVariante2(boolean mostraVariante2) {
		this.mostraVariante2 = mostraVariante2;
	}

	public boolean isMostraVariante3() {
		return mostraVariante3;
	}

	public void setMostraVariante3(boolean mostraVariante3) {
		this.mostraVariante3 = mostraVariante3;
	}

	public boolean isMostraVariante4() {
		return mostraVariante4;
	}

	public void setMostraVariante4(boolean mostraVariante4) {
		this.mostraVariante4 = mostraVariante4;
	}

	public boolean isMostraVariante5() {
		return mostraVariante5;
	}

	public void setMostraVariante5(boolean mostraVariante5) {
		this.mostraVariante5 = mostraVariante5;
	}

	public boolean isMostraVariante6() {
		return mostraVariante6;
	}

	public void setMostraVariante6(boolean mostraVariante6) {
		this.mostraVariante6 = mostraVariante6;
	}

	public boolean isMostraVariante7() {
		return mostraVariante7;
	}

	public void setMostraVariante7(boolean mostraVariante7) {
		this.mostraVariante7 = mostraVariante7;
	}

	public boolean isMostraVariante8() {
		return mostraVariante8;
	}

	public void setMostraVariante8(boolean mostraVariante8) {
		this.mostraVariante8 = mostraVariante8;
	}

	public boolean isMostraVariante9() {
		return mostraVariante9;
	}

	public void setMostraVariante9(boolean mostraVariante9) {
		this.mostraVariante9 = mostraVariante9;
	}

	public boolean isMostraVariante10() {
		return mostraVariante10;
	}

	public void setMostraVariante10(boolean mostraVariante10) {
		this.mostraVariante10 = mostraVariante10;
	}

	public boolean isMostraVariante11() {
		return mostraVariante11;
	}

	public void setMostraVariante11(boolean mostraVariante11) {
		this.mostraVariante11 = mostraVariante11;
	}

	public boolean isMostraVariante12() {
		return mostraVariante12;
	}

	public void setMostraVariante12(boolean mostraVariante12) {
		this.mostraVariante12 = mostraVariante12;
	}

	public boolean isMostraVariante13() {
		return mostraVariante13;
	}

	public void setMostraVariante13(boolean mostraVariante13) {
		this.mostraVariante13 = mostraVariante13;
	}

	public boolean isMostraVariante14() {
		return mostraVariante14;
	}

	public void setMostraVariante14(boolean mostraVariante14) {
		this.mostraVariante14 = mostraVariante14;
	}

	public boolean isMostraVariante15() {
		return mostraVariante15;
	}

	public void setMostraVariante15(boolean mostraVariante15) {
		this.mostraVariante15 = mostraVariante15;
	}

	public boolean isMostraVariante16() {
		return mostraVariante16;
	}

	public void setMostraVariante16(boolean mostraVariante16) {
		this.mostraVariante16 = mostraVariante16;
	}

	public boolean isMostraVariante17() {
		return mostraVariante17;
	}

	public void setMostraVariante17(boolean mostraVariante17) {
		this.mostraVariante17 = mostraVariante17;
	}

	public boolean isMostraVariante18() {
		return mostraVariante18;
	}

	public void setMostraVariante18(boolean mostraVariante18) {
		this.mostraVariante18 = mostraVariante18;
	}

	public boolean isMostraVariante19() {
		return mostraVariante19;
	}

	public void setMostraVariante19(boolean mostraVariante19) {
		this.mostraVariante19 = mostraVariante19;
	}

	public boolean isMostraVariante20() {
		return mostraVariante20;
	}

	public void setMostraVariante20(boolean mostraVariante20) {
		this.mostraVariante20 = mostraVariante20;
	}

	public boolean isMostraVariante21() {
		return mostraVariante21;
	}

	public void setMostraVariante21(boolean mostraVariante21) {
		this.mostraVariante21 = mostraVariante21;
	}

	public boolean isMostraVariante22() {
		return mostraVariante22;
	}

	public void setMostraVariante22(boolean mostraVariante22) {
		this.mostraVariante22 = mostraVariante22;
	}

	public boolean isMostraVariante23() {
		return mostraVariante23;
	}

	public void setMostraVariante23(boolean mostraVariante23) {
		this.mostraVariante23 = mostraVariante23;
	}

	public boolean isMostraVariante24() {
		return mostraVariante24;
	}

	public void setMostraVariante24(boolean mostraVariante24) {
		this.mostraVariante24 = mostraVariante24;
	}

	public boolean isMostraVariante25() {
		return mostraVariante25;
	}

	public void setMostraVariante25(boolean mostraVariante25) {
		this.mostraVariante25 = mostraVariante25;
	}

	public boolean isMostraVariante26() {
		return mostraVariante26;
	}

	public void setMostraVariante26(boolean mostraVariante26) {
		this.mostraVariante26 = mostraVariante26;
	}

	public boolean isMostraVariante27() {
		return mostraVariante27;
	}

	public void setMostraVariante27(boolean mostraVariante27) {
		this.mostraVariante27 = mostraVariante27;
	}

	public boolean isMostraVariante28() {
		return mostraVariante28;
	}

	public void setMostraVariante28(boolean mostraVariante28) {
		this.mostraVariante28 = mostraVariante28;
	}

	public boolean isMostraVariante29() {
		return mostraVariante29;
	}

	public void setMostraVariante29(boolean mostraVariante29) {
		this.mostraVariante29 = mostraVariante29;
	}

	public boolean isMostraVariante30() {
		return mostraVariante30;
	}

	public void setMostraVariante30(boolean mostraVariante30) {
		this.mostraVariante30 = mostraVariante30;
	}

	public String getTipoVariante1() {
		return tipoVariante1;
	}

	public void setTipoVariante1(String tipoVariante1) {
		this.tipoVariante1 = tipoVariante1;
	}

	public String getValoreVariante1() {
		return valoreVariante1;
	}

	public void setValoreVariante1(String valoreVariante1) {
		this.valoreVariante1 = valoreVariante1;
	}

	public String getImmagineVariante1() {
		return immagineVariante1;
	}

	public void setImmagineVariante1(String immagineVariante1) {
		this.immagineVariante1 = immagineVariante1;
	}

	public String getTipoVariante2() {
		return tipoVariante2;
	}

	public void setTipoVariante2(String tipoVariante2) {
		this.tipoVariante2 = tipoVariante2;
	}

	public String getValoreVariante2() {
		return valoreVariante2;
	}

	public void setValoreVariante2(String valoreVariante2) {
		this.valoreVariante2 = valoreVariante2;
	}

	public String getImmagineVariante2() {
		return immagineVariante2;
	}

	public void setImmagineVariante2(String immagineVariante2) {
		this.immagineVariante2 = immagineVariante2;
	}

	public String getTipoVariante3() {
		return tipoVariante3;
	}

	public void setTipoVariante3(String tipoVariante3) {
		this.tipoVariante3 = tipoVariante3;
	}

	public String getValoreVariante3() {
		return valoreVariante3;
	}

	public void setValoreVariante3(String valoreVariante3) {
		this.valoreVariante3 = valoreVariante3;
	}

	public String getImmagineVariante3() {
		return immagineVariante3;
	}

	public void setImmagineVariante3(String immagineVariante3) {
		this.immagineVariante3 = immagineVariante3;
	}

	public String getTipoVariante4() {
		return tipoVariante4;
	}

	public void setTipoVariante4(String tipoVariante4) {
		this.tipoVariante4 = tipoVariante4;
	}

	public String getValoreVariante4() {
		return valoreVariante4;
	}

	public void setValoreVariante4(String valoreVariante4) {
		this.valoreVariante4 = valoreVariante4;
	}

	public String getImmagineVariante4() {
		return immagineVariante4;
	}

	public void setImmagineVariante4(String immagineVariante4) {
		this.immagineVariante4 = immagineVariante4;
	}

	public String getTipoVariante5() {
		return tipoVariante5;
	}

	public void setTipoVariante5(String tipoVariante5) {
		this.tipoVariante5 = tipoVariante5;
	}

	public String getValoreVariante5() {
		return valoreVariante5;
	}

	public void setValoreVariante5(String valoreVariante5) {
		this.valoreVariante5 = valoreVariante5;
	}

	public String getImmagineVariante5() {
		return immagineVariante5;
	}

	public void setImmagineVariante5(String immagineVariante5) {
		this.immagineVariante5 = immagineVariante5;
	}

	public String getTipoVariante6() {
		return tipoVariante6;
	}

	public void setTipoVariante6(String tipoVariante6) {
		this.tipoVariante6 = tipoVariante6;
	}

	public String getValoreVariante6() {
		return valoreVariante6;
	}

	public void setValoreVariante6(String valoreVariante6) {
		this.valoreVariante6 = valoreVariante6;
	}

	public String getImmagineVariante6() {
		return immagineVariante6;
	}

	public void setImmagineVariante6(String immagineVariante6) {
		this.immagineVariante6 = immagineVariante6;
	}

	public String getTipoVariante7() {
		return tipoVariante7;
	}

	public void setTipoVariante7(String tipoVariante7) {
		this.tipoVariante7 = tipoVariante7;
	}

	public String getValoreVariante7() {
		return valoreVariante7;
	}

	public void setValoreVariante7(String valoreVariante7) {
		this.valoreVariante7 = valoreVariante7;
	}

	public String getImmagineVariante7() {
		return immagineVariante7;
	}

	public void setImmagineVariante7(String immagineVariante7) {
		this.immagineVariante7 = immagineVariante7;
	}

	public String getTipoVariante8() {
		return tipoVariante8;
	}

	public void setTipoVariante8(String tipoVariante8) {
		this.tipoVariante8 = tipoVariante8;
	}

	public String getValoreVariante8() {
		return valoreVariante8;
	}

	public void setValoreVariante8(String valoreVariante8) {
		this.valoreVariante8 = valoreVariante8;
	}

	public String getImmagineVariante8() {
		return immagineVariante8;
	}

	public void setImmagineVariante8(String immagineVariante8) {
		this.immagineVariante8 = immagineVariante8;
	}

	public String getTipoVariante9() {
		return tipoVariante9;
	}

	public void setTipoVariante9(String tipoVariante9) {
		this.tipoVariante9 = tipoVariante9;
	}

	public String getValoreVariante9() {
		return valoreVariante9;
	}

	public void setValoreVariante9(String valoreVariante9) {
		this.valoreVariante9 = valoreVariante9;
	}

	public String getImmagineVariante9() {
		return immagineVariante9;
	}

	public void setImmagineVariante9(String immagineVariante9) {
		this.immagineVariante9 = immagineVariante9;
	}

	public String getTipoVariante10() {
		return tipoVariante10;
	}

	public void setTipoVariante10(String tipoVariante10) {
		this.tipoVariante10 = tipoVariante10;
	}

	public String getValoreVariante10() {
		return valoreVariante10;
	}

	public void setValoreVariante10(String valoreVariante10) {
		this.valoreVariante10 = valoreVariante10;
	}

	public String getImmagineVariante10() {
		return immagineVariante10;
	}

	public void setImmagineVariante10(String immagineVariante10) {
		this.immagineVariante10 = immagineVariante10;
	}

	public String getTipoVariante11() {
		return tipoVariante11;
	}

	public void setTipoVariante11(String tipoVariante11) {
		this.tipoVariante11 = tipoVariante11;
	}

	public String getValoreVariante11() {
		return valoreVariante11;
	}

	public void setValoreVariante11(String valoreVariante11) {
		this.valoreVariante11 = valoreVariante11;
	}

	public String getImmagineVariante11() {
		return immagineVariante11;
	}

	public void setImmagineVariante11(String immagineVariante11) {
		this.immagineVariante11 = immagineVariante11;
	}

	public String getTipoVariante12() {
		return tipoVariante12;
	}

	public void setTipoVariante12(String tipoVariante12) {
		this.tipoVariante12 = tipoVariante12;
	}

	public String getValoreVariante12() {
		return valoreVariante12;
	}

	public void setValoreVariante12(String valoreVariante12) {
		this.valoreVariante12 = valoreVariante12;
	}

	public String getImmagineVariante12() {
		return immagineVariante12;
	}

	public void setImmagineVariante12(String immagineVariante12) {
		this.immagineVariante12 = immagineVariante12;
	}

	public String getTipoVariante13() {
		return tipoVariante13;
	}

	public void setTipoVariante13(String tipoVariante13) {
		this.tipoVariante13 = tipoVariante13;
	}

	public String getValoreVariante13() {
		return valoreVariante13;
	}

	public void setValoreVariante13(String valoreVariante13) {
		this.valoreVariante13 = valoreVariante13;
	}

	public String getImmagineVariante13() {
		return immagineVariante13;
	}

	public void setImmagineVariante13(String immagineVariante13) {
		this.immagineVariante13 = immagineVariante13;
	}

	public String getTipoVariante14() {
		return tipoVariante14;
	}

	public void setTipoVariante14(String tipoVariante14) {
		this.tipoVariante14 = tipoVariante14;
	}

	public String getValoreVariante14() {
		return valoreVariante14;
	}

	public void setValoreVariante14(String valoreVariante14) {
		this.valoreVariante14 = valoreVariante14;
	}

	public String getImmagineVariante14() {
		return immagineVariante14;
	}

	public void setImmagineVariante14(String immagineVariante14) {
		this.immagineVariante14 = immagineVariante14;
	}

	public String getTipoVariante15() {
		return tipoVariante15;
	}

	public void setTipoVariante15(String tipoVariante15) {
		this.tipoVariante15 = tipoVariante15;
	}

	public String getValoreVariante15() {
		return valoreVariante15;
	}

	public void setValoreVariante15(String valoreVariante15) {
		this.valoreVariante15 = valoreVariante15;
	}

	public String getImmagineVariante15() {
		return immagineVariante15;
	}

	public void setImmagineVariante15(String immagineVariante15) {
		this.immagineVariante15 = immagineVariante15;
	}

	public String getTipoVariante16() {
		return tipoVariante16;
	}

	public void setTipoVariante16(String tipoVariante16) {
		this.tipoVariante16 = tipoVariante16;
	}

	public String getValoreVariante16() {
		return valoreVariante16;
	}

	public void setValoreVariante16(String valoreVariante16) {
		this.valoreVariante16 = valoreVariante16;
	}

	public String getImmagineVariante16() {
		return immagineVariante16;
	}

	public void setImmagineVariante16(String immagineVariante16) {
		this.immagineVariante16 = immagineVariante16;
	}

	public String getTipoVariante17() {
		return tipoVariante17;
	}

	public void setTipoVariante17(String tipoVariante17) {
		this.tipoVariante17 = tipoVariante17;
	}

	public String getValoreVariante17() {
		return valoreVariante17;
	}

	public void setValoreVariante17(String valoreVariante17) {
		this.valoreVariante17 = valoreVariante17;
	}

	public String getImmagineVariante17() {
		return immagineVariante17;
	}

	public void setImmagineVariante17(String immagineVariante17) {
		this.immagineVariante17 = immagineVariante17;
	}

	public String getTipoVariante18() {
		return tipoVariante18;
	}

	public void setTipoVariante18(String tipoVariante18) {
		this.tipoVariante18 = tipoVariante18;
	}

	public String getValoreVariante18() {
		return valoreVariante18;
	}

	public void setValoreVariante18(String valoreVariante18) {
		this.valoreVariante18 = valoreVariante18;
	}

	public String getImmagineVariante18() {
		return immagineVariante18;
	}

	public void setImmagineVariante18(String immagineVariante18) {
		this.immagineVariante18 = immagineVariante18;
	}

	public String getTipoVariante19() {
		return tipoVariante19;
	}

	public void setTipoVariante19(String tipoVariante19) {
		this.tipoVariante19 = tipoVariante19;
	}

	public String getValoreVariante19() {
		return valoreVariante19;
	}

	public void setValoreVariante19(String valoreVariante19) {
		this.valoreVariante19 = valoreVariante19;
	}

	public String getImmagineVariante19() {
		return immagineVariante19;
	}

	public void setImmagineVariante19(String immagineVariante19) {
		this.immagineVariante19 = immagineVariante19;
	}

	public String getTipoVariante20() {
		return tipoVariante20;
	}

	public void setTipoVariante20(String tipoVariante20) {
		this.tipoVariante20 = tipoVariante20;
	}

	public String getValoreVariante20() {
		return valoreVariante20;
	}

	public void setValoreVariante20(String valoreVariante20) {
		this.valoreVariante20 = valoreVariante20;
	}

	public String getImmagineVariante20() {
		return immagineVariante20;
	}

	public void setImmagineVariante20(String immagineVariante20) {
		this.immagineVariante20 = immagineVariante20;
	}

	public String getTipoVariante21() {
		return tipoVariante21;
	}

	public void setTipoVariante21(String tipoVariante21) {
		this.tipoVariante21 = tipoVariante21;
	}

	public String getValoreVariante21() {
		return valoreVariante21;
	}

	public void setValoreVariante21(String valoreVariante21) {
		this.valoreVariante21 = valoreVariante21;
	}

	public String getImmagineVariante21() {
		return immagineVariante21;
	}

	public void setImmagineVariante21(String immagineVariante21) {
		this.immagineVariante21 = immagineVariante21;
	}

	public String getTipoVariante22() {
		return tipoVariante22;
	}

	public void setTipoVariante22(String tipoVariante22) {
		this.tipoVariante22 = tipoVariante22;
	}

	public String getValoreVariante22() {
		return valoreVariante22;
	}

	public void setValoreVariante22(String valoreVariante22) {
		this.valoreVariante22 = valoreVariante22;
	}

	public String getImmagineVariante22() {
		return immagineVariante22;
	}

	public void setImmagineVariante22(String immagineVariante22) {
		this.immagineVariante22 = immagineVariante22;
	}

	public String getTipoVariante23() {
		return tipoVariante23;
	}

	public void setTipoVariante23(String tipoVariante23) {
		this.tipoVariante23 = tipoVariante23;
	}

	public String getValoreVariante23() {
		return valoreVariante23;
	}

	public void setValoreVariante23(String valoreVariante23) {
		this.valoreVariante23 = valoreVariante23;
	}

	public String getImmagineVariante23() {
		return immagineVariante23;
	}

	public void setImmagineVariante23(String immagineVariante23) {
		this.immagineVariante23 = immagineVariante23;
	}

	public String getTipoVariante24() {
		return tipoVariante24;
	}

	public void setTipoVariante24(String tipoVariante24) {
		this.tipoVariante24 = tipoVariante24;
	}

	public String getValoreVariante24() {
		return valoreVariante24;
	}

	public void setValoreVariante24(String valoreVariante24) {
		this.valoreVariante24 = valoreVariante24;
	}

	public String getImmagineVariante24() {
		return immagineVariante24;
	}

	public void setImmagineVariante24(String immagineVariante24) {
		this.immagineVariante24 = immagineVariante24;
	}

	public String getTipoVariante25() {
		return tipoVariante25;
	}

	public void setTipoVariante25(String tipoVariante25) {
		this.tipoVariante25 = tipoVariante25;
	}

	public String getValoreVariante25() {
		return valoreVariante25;
	}

	public void setValoreVariante25(String valoreVariante25) {
		this.valoreVariante25 = valoreVariante25;
	}

	public String getImmagineVariante25() {
		return immagineVariante25;
	}

	public void setImmagineVariante25(String immagineVariante25) {
		this.immagineVariante25 = immagineVariante25;
	}

	public String getTipoVariante26() {
		return tipoVariante26;
	}

	public void setTipoVariante26(String tipoVariante26) {
		this.tipoVariante26 = tipoVariante26;
	}

	public String getValoreVariante26() {
		return valoreVariante26;
	}

	public void setValoreVariante26(String valoreVariante26) {
		this.valoreVariante26 = valoreVariante26;
	}

	public String getImmagineVariante26() {
		return immagineVariante26;
	}

	public void setImmagineVariante26(String immagineVariante26) {
		this.immagineVariante26 = immagineVariante26;
	}

	public String getTipoVariante27() {
		return tipoVariante27;
	}

	public void setTipoVariante27(String tipoVariante27) {
		this.tipoVariante27 = tipoVariante27;
	}

	public String getValoreVariante27() {
		return valoreVariante27;
	}

	public void setValoreVariante27(String valoreVariante27) {
		this.valoreVariante27 = valoreVariante27;
	}

	public String getImmagineVariante27() {
		return immagineVariante27;
	}

	public void setImmagineVariante27(String immagineVariante27) {
		this.immagineVariante27 = immagineVariante27;
	}

	public String getTipoVariante28() {
		return tipoVariante28;
	}

	public void setTipoVariante28(String tipoVariante28) {
		this.tipoVariante28 = tipoVariante28;
	}

	public String getValoreVariante28() {
		return valoreVariante28;
	}

	public void setValoreVariante28(String valoreVariante28) {
		this.valoreVariante28 = valoreVariante28;
	}

	public String getImmagineVariante28() {
		return immagineVariante28;
	}

	public void setImmagineVariante28(String immagineVariante28) {
		this.immagineVariante28 = immagineVariante28;
	}

	public String getTipoVariante29() {
		return tipoVariante29;
	}

	public void setTipoVariante29(String tipoVariante29) {
		this.tipoVariante29 = tipoVariante29;
	}

	public String getValoreVariante29() {
		return valoreVariante29;
	}

	public void setValoreVariante29(String valoreVariante29) {
		this.valoreVariante29 = valoreVariante29;
	}

	public String getImmagineVariante29() {
		return immagineVariante29;
	}

	public void setImmagineVariante29(String immagineVariante29) {
		this.immagineVariante29 = immagineVariante29;
	}

	public String getTipoVariante30() {
		return tipoVariante30;
	}

	public void setTipoVariante30(String tipoVariante30) {
		this.tipoVariante30 = tipoVariante30;
	}

	public String getValoreVariante30() {
		return valoreVariante30;
	}

	public void setValoreVariante30(String valoreVariante30) {
		this.valoreVariante30 = valoreVariante30;
	}

	public String getImmagineVariante30() {
		return immagineVariante30;
	}

	public void setImmagineVariante30(String immagineVariante30) {
		this.immagineVariante30 = immagineVariante30;
	}



	public int getQuantitaVariante1() {
		return quantitaVariante1;
	}



	public void setQuantitaVariante1(int quantitaVariante1) {
		this.quantitaVariante1 = quantitaVariante1;
	}



	public int getQuantitaVariante2() {
		return quantitaVariante2;
	}



	public void setQuantitaVariante2(int quantitaVariante2) {
		this.quantitaVariante2 = quantitaVariante2;
	}



	public int getQuantitaVariante3() {
		return quantitaVariante3;
	}



	public void setQuantitaVariante3(int quantitaVariante3) {
		this.quantitaVariante3 = quantitaVariante3;
	}



	public int getQuantitaVariante4() {
		return quantitaVariante4;
	}



	public void setQuantitaVariante4(int quantitaVariante4) {
		this.quantitaVariante4 = quantitaVariante4;
	}



	public int getQuantitaVariante5() {
		return quantitaVariante5;
	}



	public void setQuantitaVariante5(int quantitaVariante5) {
		this.quantitaVariante5 = quantitaVariante5;
	}



	public int getQuantitaVariante6() {
		return quantitaVariante6;
	}



	public void setQuantitaVariante6(int quantitaVariante6) {
		this.quantitaVariante6 = quantitaVariante6;
	}



	public int getQuantitaVariante7() {
		return quantitaVariante7;
	}



	public void setQuantitaVariante7(int quantitaVariante7) {
		this.quantitaVariante7 = quantitaVariante7;
	}



	public int getQuantitaVariante8() {
		return quantitaVariante8;
	}



	public void setQuantitaVariante8(int quantitaVariante8) {
		this.quantitaVariante8 = quantitaVariante8;
	}



	public int getQuantitaVariante9() {
		return quantitaVariante9;
	}



	public void setQuantitaVariante9(int quantitaVariante9) {
		this.quantitaVariante9 = quantitaVariante9;
	}



	public int getQuantitaVariante10() {
		return quantitaVariante10;
	}



	public void setQuantitaVariante10(int quantitaVariante10) {
		this.quantitaVariante10 = quantitaVariante10;
	}



	public int getQuantitaVariante11() {
		return quantitaVariante11;
	}



	public void setQuantitaVariante11(int quantitaVariante11) {
		this.quantitaVariante11 = quantitaVariante11;
	}



	public int getQuantitaVariante12() {
		return quantitaVariante12;
	}



	public void setQuantitaVariante12(int quantitaVariante12) {
		this.quantitaVariante12 = quantitaVariante12;
	}



	public int getQuantitaVariante13() {
		return quantitaVariante13;
	}



	public void setQuantitaVariante13(int quantitaVariante13) {
		this.quantitaVariante13 = quantitaVariante13;
	}



	public int getQuantitaVariante14() {
		return quantitaVariante14;
	}



	public void setQuantitaVariante14(int quantitaVariante14) {
		this.quantitaVariante14 = quantitaVariante14;
	}



	public int getQuantitaVariante15() {
		return quantitaVariante15;
	}



	public void setQuantitaVariante15(int quantitaVariante15) {
		this.quantitaVariante15 = quantitaVariante15;
	}



	public int getQuantitaVariante16() {
		return quantitaVariante16;
	}



	public void setQuantitaVariante16(int quantitaVariante16) {
		this.quantitaVariante16 = quantitaVariante16;
	}



	public int getQuantitaVariante17() {
		return quantitaVariante17;
	}



	public void setQuantitaVariante17(int quantitaVariante17) {
		this.quantitaVariante17 = quantitaVariante17;
	}



	public int getQuantitaVariante18() {
		return quantitaVariante18;
	}



	public void setQuantitaVariante18(int quantitaVariante18) {
		this.quantitaVariante18 = quantitaVariante18;
	}



	public int getQuantitaVariante19() {
		return quantitaVariante19;
	}



	public void setQuantitaVariante19(int quantitaVariante19) {
		this.quantitaVariante19 = quantitaVariante19;
	}



	public int getQuantitaVariante20() {
		return quantitaVariante20;
	}



	public void setQuantitaVariante20(int quantitaVariante20) {
		this.quantitaVariante20 = quantitaVariante20;
	}



	public int getQuantitaVariante21() {
		return quantitaVariante21;
	}



	public void setQuantitaVariante21(int quantitaVariante21) {
		this.quantitaVariante21 = quantitaVariante21;
	}



	public int getQuantitaVariante22() {
		return quantitaVariante22;
	}



	public void setQuantitaVariante22(int quantitaVariante22) {
		this.quantitaVariante22 = quantitaVariante22;
	}



	public int getQuantitaVariante23() {
		return quantitaVariante23;
	}



	public void setQuantitaVariante23(int quantitaVariante23) {
		this.quantitaVariante23 = quantitaVariante23;
	}



	public int getQuantitaVariante24() {
		return quantitaVariante24;
	}



	public void setQuantitaVariante24(int quantitaVariante24) {
		this.quantitaVariante24 = quantitaVariante24;
	}



	public int getQuantitaVariante25() {
		return quantitaVariante25;
	}



	public void setQuantitaVariante25(int quantitaVariante25) {
		this.quantitaVariante25 = quantitaVariante25;
	}



	public int getQuantitaVariante26() {
		return quantitaVariante26;
	}



	public void setQuantitaVariante26(int quantitaVariante26) {
		this.quantitaVariante26 = quantitaVariante26;
	}



	public int getQuantitaVariante27() {
		return quantitaVariante27;
	}



	public void setQuantitaVariante27(int quantitaVariante27) {
		this.quantitaVariante27 = quantitaVariante27;
	}



	public int getQuantitaVariante28() {
		return quantitaVariante28;
	}



	public void setQuantitaVariante28(int quantitaVariante28) {
		this.quantitaVariante28 = quantitaVariante28;
	}



	public int getQuantitaVariante29() {
		return quantitaVariante29;
	}



	public void setQuantitaVariante29(int quantitaVariante29) {
		this.quantitaVariante29 = quantitaVariante29;
	}



	public int getQuantitaVariante30() {
		return quantitaVariante30;
	}



	public void setQuantitaVariante30(int quantitaVariante30) {
		this.quantitaVariante30 = quantitaVariante30;
	}



	public List<Variante_Articolo> getVarianti() {
		return varianti;
	}



	public void setVarianti(List<Variante_Articolo> varianti) {
		this.varianti = varianti;
	}


	public String getCodiceBarreVariante1() {
		return codiceBarreVariante1;
	}


	public void setCodiceBarreVariante1(String codiceBarreVariante1) {
		this.codiceBarreVariante1 = codiceBarreVariante1;
	}


	public String getCodiceBarreVariante2() {
		return codiceBarreVariante2;
	}


	public void setCodiceBarreVariante2(String codiceBarreVariante2) {
		this.codiceBarreVariante2 = codiceBarreVariante2;
	}


	public String getCodiceBarreVariante3() {
		return codiceBarreVariante3;
	}


	public void setCodiceBarreVariante3(String codiceBarreVariante3) {
		this.codiceBarreVariante3 = codiceBarreVariante3;
	}


	public String getCodiceBarreVariante4() {
		return codiceBarreVariante4;
	}


	public void setCodiceBarreVariante4(String codiceBarreVariante4) {
		this.codiceBarreVariante4 = codiceBarreVariante4;
	}


	public String getCodiceBarreVariante5() {
		return codiceBarreVariante5;
	}


	public void setCodiceBarreVariante5(String codiceBarreVariante5) {
		this.codiceBarreVariante5 = codiceBarreVariante5;
	}


	public String getCodiceBarreVariante6() {
		return codiceBarreVariante6;
	}


	public void setCodiceBarreVariante6(String codiceBarreVariante6) {
		this.codiceBarreVariante6 = codiceBarreVariante6;
	}


	public String getCodiceBarreVariante7() {
		return codiceBarreVariante7;
	}


	public void setCodiceBarreVariante7(String codiceBarreVariante7) {
		this.codiceBarreVariante7 = codiceBarreVariante7;
	}


	public String getCodiceBarreVariante8() {
		return codiceBarreVariante8;
	}


	public void setCodiceBarreVariante8(String codiceBarreVariante8) {
		this.codiceBarreVariante8 = codiceBarreVariante8;
	}


	public String getCodiceBarreVariante9() {
		return codiceBarreVariante9;
	}


	public void setCodiceBarreVariante9(String codiceBarreVariante9) {
		this.codiceBarreVariante9 = codiceBarreVariante9;
	}


	public String getCodiceBarreVariante10() {
		return codiceBarreVariante10;
	}


	public void setCodiceBarreVariante10(String codiceBarreVariante10) {
		this.codiceBarreVariante10 = codiceBarreVariante10;
	}


	public String getCodiceBarreVariante11() {
		return codiceBarreVariante11;
	}


	public void setCodiceBarreVariante11(String codiceBarreVariante11) {
		this.codiceBarreVariante11 = codiceBarreVariante11;
	}


	public String getCodiceBarreVariante12() {
		return codiceBarreVariante12;
	}


	public void setCodiceBarreVariante12(String codiceBarreVariante12) {
		this.codiceBarreVariante12 = codiceBarreVariante12;
	}


	public String getCodiceBarreVariante13() {
		return codiceBarreVariante13;
	}


	public void setCodiceBarreVariante13(String codiceBarreVariante13) {
		this.codiceBarreVariante13 = codiceBarreVariante13;
	}


	public String getCodiceBarreVariante14() {
		return codiceBarreVariante14;
	}


	public void setCodiceBarreVariante14(String codiceBarreVariante14) {
		this.codiceBarreVariante14 = codiceBarreVariante14;
	}


	public String getCodiceBarreVariante15() {
		return codiceBarreVariante15;
	}


	public void setCodiceBarreVariante15(String codiceBarreVariante15) {
		this.codiceBarreVariante15 = codiceBarreVariante15;
	}


	public String getCodiceBarreVariante16() {
		return codiceBarreVariante16;
	}


	public void setCodiceBarreVariante16(String codiceBarreVariante16) {
		this.codiceBarreVariante16 = codiceBarreVariante16;
	}


	public String getCodiceBarreVariante17() {
		return codiceBarreVariante17;
	}


	public void setCodiceBarreVariante17(String codiceBarreVariante17) {
		this.codiceBarreVariante17 = codiceBarreVariante17;
	}


	public String getCodiceBarreVariante18() {
		return codiceBarreVariante18;
	}


	public void setCodiceBarreVariante18(String codiceBarreVariante18) {
		this.codiceBarreVariante18 = codiceBarreVariante18;
	}


	public String getCodiceBarreVariante19() {
		return codiceBarreVariante19;
	}


	public void setCodiceBarreVariante19(String codiceBarreVariante19) {
		this.codiceBarreVariante19 = codiceBarreVariante19;
	}


	public String getCodiceBarreVariante20() {
		return codiceBarreVariante20;
	}


	public void setCodiceBarreVariante20(String codiceBarreVariante20) {
		this.codiceBarreVariante20 = codiceBarreVariante20;
	}


	public String getCodiceBarreVariante21() {
		return codiceBarreVariante21;
	}


	public void setCodiceBarreVariante21(String codiceBarreVariante21) {
		this.codiceBarreVariante21 = codiceBarreVariante21;
	}


	public String getCodiceBarreVariante22() {
		return codiceBarreVariante22;
	}


	public void setCodiceBarreVariante22(String codiceBarreVariante22) {
		this.codiceBarreVariante22 = codiceBarreVariante22;
	}


	public String getCodiceBarreVariante23() {
		return codiceBarreVariante23;
	}


	public void setCodiceBarreVariante23(String codiceBarreVariante23) {
		this.codiceBarreVariante23 = codiceBarreVariante23;
	}


	public String getCodiceBarreVariante24() {
		return codiceBarreVariante24;
	}


	public void setCodiceBarreVariante24(String codiceBarreVariante24) {
		this.codiceBarreVariante24 = codiceBarreVariante24;
	}


	public String getCodiceBarreVariante25() {
		return codiceBarreVariante25;
	}


	public void setCodiceBarreVariante25(String codiceBarreVariante25) {
		this.codiceBarreVariante25 = codiceBarreVariante25;
	}


	public String getCodiceBarreVariante26() {
		return codiceBarreVariante26;
	}


	public void setCodiceBarreVariante26(String codiceBarreVariante26) {
		this.codiceBarreVariante26 = codiceBarreVariante26;
	}


	public String getCodiceBarreVariante27() {
		return codiceBarreVariante27;
	}


	public void setCodiceBarreVariante27(String codiceBarreVariante27) {
		this.codiceBarreVariante27 = codiceBarreVariante27;
	}


	public String getCodiceBarreVariante28() {
		return codiceBarreVariante28;
	}


	public void setCodiceBarreVariante28(String codiceBarreVariante28) {
		this.codiceBarreVariante28 = codiceBarreVariante28;
	}


	public String getCodiceBarreVariante29() {
		return codiceBarreVariante29;
	}


	public void setCodiceBarreVariante29(String codiceBarreVariante29) {
		this.codiceBarreVariante29 = codiceBarreVariante29;
	}


	public String getCodiceBarreVariante30() {
		return codiceBarreVariante30;
	}


	public void setCodiceBarreVariante30(String codiceBarreVariante30) {
		this.codiceBarreVariante30 = codiceBarreVariante30;
	}


	public String getDimensioniVariante1() {
		return dimensioniVariante1;
	}


	public void setDimensioniVariante1(String dimensioniVariante1) {
		this.dimensioniVariante1 = dimensioniVariante1;
	}


	public String getDimensioniVariante2() {
		return dimensioniVariante2;
	}


	public void setDimensioniVariante2(String dimensioniVariante2) {
		this.dimensioniVariante2 = dimensioniVariante2;
	}


	public String getDimensioniVariante3() {
		return dimensioniVariante3;
	}


	public void setDimensioniVariante3(String dimensioniVariante3) {
		this.dimensioniVariante3 = dimensioniVariante3;
	}


	public String getDimensioniVariante4() {
		return dimensioniVariante4;
	}


	public void setDimensioniVariante4(String dimensioniVariante4) {
		this.dimensioniVariante4 = dimensioniVariante4;
	}


	public String getDimensioniVariante5() {
		return dimensioniVariante5;
	}


	public void setDimensioniVariante5(String dimensioniVariante5) {
		this.dimensioniVariante5 = dimensioniVariante5;
	}


	public String getDimensioniVariante6() {
		return dimensioniVariante6;
	}


	public void setDimensioniVariante6(String dimensioniVariante6) {
		this.dimensioniVariante6 = dimensioniVariante6;
	}


	public String getDimensioniVariante7() {
		return dimensioniVariante7;
	}


	public void setDimensioniVariante7(String dimensioniVariante7) {
		this.dimensioniVariante7 = dimensioniVariante7;
	}


	public String getDimensioniVariante8() {
		return dimensioniVariante8;
	}


	public void setDimensioniVariante8(String dimensioniVariante8) {
		this.dimensioniVariante8 = dimensioniVariante8;
	}


	public String getDimensioniVariante9() {
		return dimensioniVariante9;
	}


	public void setDimensioniVariante9(String dimensioniVariante9) {
		this.dimensioniVariante9 = dimensioniVariante9;
	}


	public String getDimensioniVariante10() {
		return dimensioniVariante10;
	}


	public void setDimensioniVariante10(String dimensioniVariante10) {
		this.dimensioniVariante10 = dimensioniVariante10;
	}


	public String getDimensioniVariante11() {
		return dimensioniVariante11;
	}


	public void setDimensioniVariante11(String dimensioniVariante11) {
		this.dimensioniVariante11 = dimensioniVariante11;
	}


	public String getDimensioniVariante12() {
		return dimensioniVariante12;
	}


	public void setDimensioniVariante12(String dimensioniVariante12) {
		this.dimensioniVariante12 = dimensioniVariante12;
	}


	public String getDimensioniVariante13() {
		return dimensioniVariante13;
	}


	public void setDimensioniVariante13(String dimensioniVariante13) {
		this.dimensioniVariante13 = dimensioniVariante13;
	}


	public String getDimensioniVariante14() {
		return dimensioniVariante14;
	}


	public void setDimensioniVariante14(String dimensioniVariante14) {
		this.dimensioniVariante14 = dimensioniVariante14;
	}


	public String getDimensioniVariante15() {
		return dimensioniVariante15;
	}


	public void setDimensioniVariante15(String dimensioniVariante15) {
		this.dimensioniVariante15 = dimensioniVariante15;
	}


	public String getDimensioniVariante16() {
		return dimensioniVariante16;
	}


	public void setDimensioniVariante16(String dimensioniVariante16) {
		this.dimensioniVariante16 = dimensioniVariante16;
	}


	public String getDimensioniVariante17() {
		return dimensioniVariante17;
	}


	public void setDimensioniVariante17(String dimensioniVariante17) {
		this.dimensioniVariante17 = dimensioniVariante17;
	}


	public String getDimensioniVariante18() {
		return dimensioniVariante18;
	}


	public void setDimensioniVariante18(String dimensioniVariante18) {
		this.dimensioniVariante18 = dimensioniVariante18;
	}


	public String getDimensioniVariante19() {
		return dimensioniVariante19;
	}


	public void setDimensioniVariante19(String dimensioniVariante19) {
		this.dimensioniVariante19 = dimensioniVariante19;
	}


	public String getDimensioniVariante20() {
		return dimensioniVariante20;
	}


	public void setDimensioniVariante20(String dimensioniVariante20) {
		this.dimensioniVariante20 = dimensioniVariante20;
	}


	public String getDimensioniVariante21() {
		return dimensioniVariante21;
	}


	public void setDimensioniVariante21(String dimensioniVariante21) {
		this.dimensioniVariante21 = dimensioniVariante21;
	}


	public String getDimensioniVariante22() {
		return dimensioniVariante22;
	}


	public void setDimensioniVariante22(String dimensioniVariante22) {
		this.dimensioniVariante22 = dimensioniVariante22;
	}


	public String getDimensioniVariante23() {
		return dimensioniVariante23;
	}


	public void setDimensioniVariante23(String dimensioniVariante23) {
		this.dimensioniVariante23 = dimensioniVariante23;
	}


	public String getDimensioniVariante24() {
		return dimensioniVariante24;
	}


	public void setDimensioniVariante24(String dimensioniVariante24) {
		this.dimensioniVariante24 = dimensioniVariante24;
	}


	public String getDimensioniVariante25() {
		return dimensioniVariante25;
	}


	public void setDimensioniVariante25(String dimensioniVariante25) {
		this.dimensioniVariante25 = dimensioniVariante25;
	}


	public String getDimensioniVariante26() {
		return dimensioniVariante26;
	}


	public void setDimensioniVariante26(String dimensioniVariante26) {
		this.dimensioniVariante26 = dimensioniVariante26;
	}


	public String getDimensioniVariante27() {
		return dimensioniVariante27;
	}


	public void setDimensioniVariante27(String dimensioniVariante27) {
		this.dimensioniVariante27 = dimensioniVariante27;
	}


	public String getDimensioniVariante28() {
		return dimensioniVariante28;
	}


	public void setDimensioniVariante28(String dimensioniVariante28) {
		this.dimensioniVariante28 = dimensioniVariante28;
	}


	public String getDimensioniVariante29() {
		return dimensioniVariante29;
	}


	public void setDimensioniVariante29(String dimensioniVariante29) {
		this.dimensioniVariante29 = dimensioniVariante29;
	}


	public String getDimensioniVariante30() {
		return dimensioniVariante30;
	}


	public void setDimensioniVariante30(String dimensioniVariante30) {
		this.dimensioniVariante30 = dimensioniVariante30;
	}

	public boolean isMostraVariante1() {
		return mostraVariante1;
	}

	public void setMostraVariante1(boolean mostraVariante1) {
		this.mostraVariante1 = mostraVariante1;
	}

}
