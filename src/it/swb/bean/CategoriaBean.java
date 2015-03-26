package it.swb.bean;

import java.io.Serializable;
import java.util.List;

import it.swb.database.Categoria_DAO;
import it.swb.log.Log;
import it.swb.model.Categoria;
import it.swb.model.CategoriaAmazon;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "categoriaBean")
@RequestScoped
public class CategoriaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private TreeNode root;  
	private TreeNode categoriaSelezionata;
	
	private List<CategoriaAmazon> categorieAmazon;
	private String nodoSelezionato;
	
	private boolean modalitaModifica = false;
	
	public CategoriaBean(){
		getRoot();
	}
	
	public void abilitaModifica(){
		Log.debug("Modifica Abilitata");
		modalitaModifica = true;
	}
	
	public void salvaModifica(){
		Categoria c = (Categoria)categoriaSelezionata.getData();
		c.setIdCategoriaAmazon(nodoSelezionato);
		Log.debug("nodoSelezionato: "+nodoSelezionato);
		
		Categoria_DAO.assegnaCategoriaAmazon(c);
		
		Log.debug("Modifica Salvata");
		modalitaModifica = false;
	}
	
	public void disabilitaModifica(){
		Log.debug("Modifica Disbilitata");
		modalitaModifica = false;
	}

	public TreeNode getRoot() {
		if (root==null)
			root = Categoria_DAO.getAlberoCategorie();
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getCategoriaSelezionata() {
		return categoriaSelezionata;
	}

	public void setCategoriaSelezionata(TreeNode categoriaSelezionata) {
		this.categoriaSelezionata = categoriaSelezionata;
	}

	public boolean isModalitaModifica() {
		return modalitaModifica;
	}

	public void setModalitaModifica(boolean modalitaModifica) {
		this.modalitaModifica = modalitaModifica;
	}

	public String getCategoriaSelezionato() {
		return nodoSelezionato;
	}

	public void setCategoriaSelezionato(String nodoSelezionato) {
		this.nodoSelezionato = nodoSelezionato;
	}

	public List<CategoriaAmazon> getCategorieAmazon() {
		if (categorieAmazon==null)
			categorieAmazon = Categoria_DAO.getCategorieAmazon();
		return categorieAmazon;
	}

	public void setCategorieAmazon(List<CategoriaAmazon> categorieAmazon) {
		this.categorieAmazon = categorieAmazon;
	}



  }
