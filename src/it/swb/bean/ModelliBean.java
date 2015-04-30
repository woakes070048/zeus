package it.swb.bean;

import it.swb.database.Articolo_DAO;
import it.swb.database.Ordine_DAO;
import it.swb.java.EditorModelliYatego;
import it.swb.log.Log;
import it.swb.model.Ordine;
import it.swb.piattaforme.amazon.ElaboratoreOrdini;
import it.swb.utility.Methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "modelliBean")
@ViewScoped
public class ModelliBean  implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Object[]> modelliDaCaricare;
	private List<Object[]> modelliCaricati;
	private String[] modelloSelezionato;
	private List<String[]> valoriModelloSelezionato;
	
	private String fileSelezionato;
	private String nomeFileSelezionato;
	private String percorsoFileSelezionato;
	private StreamedContent file;  
	
	public void unificaModelliYatego(){
		EditorModelliYatego.unificaModelli(null);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione completata", "mcd_yatego è ora disponibile."));  
	}
	
	public void eliminaModello(){
		boolean ok = Methods.delete(percorsoFileSelezionato+nomeFileSelezionato);
		
		if (ok)
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione completata", nomeFileSelezionato+" è stato eliminato."));  
		else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione NON completata", nomeFileSelezionato+" non è stato eliminato. Controllare i log."));  
	}
    
    public void downloadFile() {        
        InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(percorsoFileSelezionato+nomeFileSelezionato);  
        if (nomeFileSelezionato.contains(".csv"))
        	file = new DefaultStreamedContent(stream, "text/csv", nomeFileSelezionato);  
        else if (nomeFileSelezionato.contains(".txt"))
        	file = new DefaultStreamedContent(stream, "text/txt", nomeFileSelezionato);  
    }  
  
    public StreamedContent getFile() throws FileNotFoundException {  
    	//String percorso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("percorso");
    	//String nome 	= (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nome");
    	
    	//System.out.println("percorso:"+percorso+nome);
    	Log.info("Download file: "+percorsoFileSelezionato+nomeFileSelezionato);
    	
    	if (percorsoFileSelezionato!=null && !percorsoFileSelezionato.isEmpty() && nomeFileSelezionato!=null && !nomeFileSelezionato.isEmpty()){
    		
    		File f = new File(percorsoFileSelezionato+nomeFileSelezionato);
            
            if (f.exists()){
            	InputStream stream = new FileInputStream(f);
    			
            	//((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(percorso+nome);
            	
            	if (nomeFileSelezionato.contains(".csv"))
                	file = new DefaultStreamedContent(stream, "text/csv", nomeFileSelezionato);  
                else if (nomeFileSelezionato.contains(".txt"))
                	file = new DefaultStreamedContent(stream, "text/txt", nomeFileSelezionato); 
            }
    	}
    	
        return file;  
    }    
	
	
	public void uploadFile(FileUploadEvent event) {  
		Properties config = new Properties();	      
        try {
        	config.load(Log.class.getResourceAsStream("/zeus.properties"));
        	
            File targetFolder = new File(config.getProperty("percorso_upload_file_ordini"));
            String nomeFile = event.getFile().getFileName();
            InputStream inputStream = event.getFile().getInputstream();
            File f = new File(targetFolder,nomeFile);
            
            if (!f.exists()) f.createNewFile();
            OutputStream out = new FileOutputStream(f);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Operazione completata", nomeFile + " è stato caricato."));  
            
            String percorso = targetFolder+"\\"+nomeFile;
            
            List<Ordine> list = null;
            
            if (nomeFile.contains("txt"))
            	list = ElaboratoreOrdini.leggiTxtOrdiniAmazon(percorso);
            
            else if (nomeFile.contains("csv"))
            	list = ElaboratoreOrdini.leggiCsvOrdiniYatego(percorso);
    		
    		Ordine_DAO.elaboraOrdini(list);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public List<Object[]> getModelliDaCaricare() {
		Properties config = new Properties();	      
		try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String percorso = config.getProperty("percorso_modelli_da_caricare");
        	
			File d = new File(percorso);
				
			String a[] = d.list(); 
			
			modelliDaCaricare = new ArrayList<Object[]>();
			
			int j = 1;
			for (int i=0;i<a.length;i++)
			{
				if(a[i].contains(".")){
					Object[] o = new Object[3];
					o[0] = j;
					o[1] = percorso;
					o[2] = a[i];
					
					modelliDaCaricare.add(o);
				}
				j++;
			}
		}
		catch (Exception e){
			Log.info(e);
			e.printStackTrace();
		}
		return modelliDaCaricare;
	}

	public void setModelliDaCaricare(List<Object[]> modelliDaCaricare) {
		this.modelliDaCaricare = modelliDaCaricare;
	}

	public List<Object[]> getModelliCaricati() {
		Properties config = new Properties();	      
		try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			String percorso = config.getProperty("percorso_modelli_caricati");
        	
			File d = new File(percorso);
				
			String a[] = d.list(); 
			
			modelliCaricati = new ArrayList<Object[]>();
			
			int j = 1;
			for (int i=0;i<a.length;i++)
			{
				if(a[i].contains(".")){
					Object[] o = new Object[3];
					o[0] = j;
					o[1] = percorso;
					o[2] = a[i];
					
					modelliCaricati.add(o);
				}
				j++;
			}
		}
		catch (Exception e){
			Log.info(e);
			e.printStackTrace();
		}
		return modelliCaricati;
	}

	public void setModelliCaricati(List<Object[]> modelliCaricati) {
		this.modelliCaricati = modelliCaricati;
	}

	public String getFileSelezionato() {
		return fileSelezionato;
	}

	public void setFileSelezionato(String fileSelezionato) {
		this.fileSelezionato = fileSelezionato;
	}

	public String getNomeFileSelezionato() {
		return nomeFileSelezionato;
	}

	public void setNomeFileSelezionato(String nomeFileSelezionato) {
		this.nomeFileSelezionato = nomeFileSelezionato;
	}

	public String getPercorsoFileSelezionato() {
		return percorsoFileSelezionato;
	}

	public void setPercorsoFileSelezionato(String percorsoFileSelezionato) {
		this.percorsoFileSelezionato = percorsoFileSelezionato;
	}  
	
//	private void getModelli(){
//		Properties config = new Properties();	      
//		try {
//			config.load(Log.class.getResourceAsStream("/zeus.properties"));
//        	
//			File d = new File(config.getProperty("percorso_modelli_caricamento"));
//			System.out.println("Verifico se la directory esiste: " + d.exists() );
//			
//			String a[] = d.list(); 
//			
//			List<String[]> l = new ArrayList<String[]>();
//			
//			
//			System.out.println("stampo la lista dei files contenuti nella directory:");
//			int j = 0;
//			for (int i=0;i<a.length;i++)
//			{
//				j = j + 1;
//				System.out.println(j + ". " + a[i]);
//				l.add(new String[]{a[i]});
//			}
//		}
//		catch (Exception e){
//			Log.info(e);
//			e.printStackTrace();
//		}
//		
//	}
	
	public void impostaCaricatoListener(ActionEvent event){
		
	}
	
	public void impostaCaricato(){
		String testa = "";
		String messaggio = "";
		String piattaforma = "";
		
		try{
			if (nomeFileSelezionato.contains("txt")){
				piattaforma = "amazon";
				this.valoriModelloSelezionato =  Methods.leggiMcdAmazon(percorsoFileSelezionato+nomeFileSelezionato);
			}
	        
	        else if (nomeFileSelezionato.contains("csv")){
	        	piattaforma = "yatego";
	        	this.valoriModelloSelezionato =  Methods.leggiMcdYatego(percorsoFileSelezionato+nomeFileSelezionato);
	        }
	        
	        Articolo_DAO.setPresenzaSu(valoriModelloSelezionato, piattaforma, 1);
	        
	        Methods.copyFile(percorsoFileSelezionato+nomeFileSelezionato, percorsoFileSelezionato+"caricati\\"+nomeFileSelezionato);
	        Methods.delete(percorsoFileSelezionato+nomeFileSelezionato);
	        
	        testa = "Operazione completata";
	        messaggio = nomeFileSelezionato+" è stato impostato come caricato.";
		}
		catch (Exception e){
			testa = "Si è verificato un errore";
			messaggio = e.getMessage();
			Log.info(e);
			e.printStackTrace();
		}
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(testa,messaggio));  
	}
	
	public void ottieniValori(){
		try{
			System.out.println("ottieni valori "+nomeFileSelezionato);
			
			FacesMessage msg = new FacesMessage(percorsoFileSelezionato+nomeFileSelezionato);  
	        FacesContext.getCurrentInstance().addMessage(null, msg);  
	        
	        if (nomeFileSelezionato.contains("txt"))
	        	this.valoriModelloSelezionato =  Methods.leggiMcdAmazon(percorsoFileSelezionato+nomeFileSelezionato);
	        
	        else if (nomeFileSelezionato.contains("csv"))
	        	this.valoriModelloSelezionato =  Methods.leggiMcdYatego(percorsoFileSelezionato+nomeFileSelezionato);
	        
	    } catch (Exception e){
			//testa = "Si è verificato un errore";
			//messaggio = e.getMessage();
			Log.info(e);
			e.printStackTrace();
		}
	}

	public String[] getModelloSelezionato() {
		return modelloSelezionato;
	}

	public void setModelloSelezionato(String[] modelloSelezionato) {
		this.modelloSelezionato = modelloSelezionato;
	}

	public List<String[]> getValoriModelloSelezionato() {
		return valoriModelloSelezionato;
	}

	public void setValoriModelloSelezionato(List<String[]> valoriModelloSelezionato) {
		this.valoriModelloSelezionato = valoriModelloSelezionato;
	}

}
