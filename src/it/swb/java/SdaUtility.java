package it.swb.java;

import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.model.Ordine;
import it.swb.model.Variante_Articolo;
import it.swb.utility.Costanti;
import it.swb.utility.Methods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class SdaUtility {
	
	public static int aggiungiOrdineALDV(Ordine o, String percorso){		
		 int risultato = 0;
	     try {
			
	    	 boolean esiste = true;
				
			File f = new File(percorso);
			
			if (!f.exists()) {
				esiste = false;
				f.createNewFile();
			}
						
			FileOutputStream fos = new FileOutputStream (percorso, true);
			
			PrintWriter pw = new PrintWriter (fos);
			
			if (!esiste) {
				//System.out.println("il file non esisteva");
				//pw.println("TemplateType=Home	Version=1.0	This row for Amazon.com use only.  Do not modify or delete.		");
				//pw.println("sku	StandardProductId	ProductIdType	ProductName	Brand	Manufacturer	Description	BulletPoint1	BulletPoint2	BulletPoint3	BulletPoint4	BulletPoint5	RecommendedBrowseNode1	RecommendedBrowseNode2	ProductType	MainImageUrl	OtherImageUrl1	OtherImageUrl2	OtherImageUrl3	OtherImageUrl4	OtherImageUrl5	OtherImageUrl6	OtherImageUrl7	OtherImageUrl8	SwatchImageUrl	LegalDisclaimer	SellerWarrantyDescription	ManufacturerSafetyWarning	UpdateDelete	ItemPrice	Currency	Quantity	ConditionType	ConditionNote	ItemPackageQuantity	NumberOfPieces	LaunchDate	ReleaseDate	LeadtimeToShip	RestockDate	MaxAggregateShipQuantity	CountryProducedIn	ShippingWeight	ShippingWeightUnitOfMeasure	ItemLength	ItemHeight	ItemWeight	ItemWeightUnitOfMeasure	ManufacturerPartNumber	SearchTerms1	SearchTerms2	SearchTerms3	SearchTerms4	SearchTerms5	SalesPrice	SaleEndDate	SaleStartDate	Parentage	ParentSku	RelationshipType	VariationTheme	Size	SizeMap	Color	ColorMap");
			}			
			
			aggiungiOrdine(o,pw);
			
			pw.close();
			
			risultato = 1;
	
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			risultato = 0;
		}		
	     return risultato;
	}

	
	private static void aggiungiOrdine(Ordine o, PrintWriter pw){

		//Destinatario Ragione Sociale
		//Destinatario Referente
		//Destinatario Indirizzo
		//Destinatario Cap
		//Destinatario Localita
		//Destinatario Provincia
		//Destinatario Nazione
		//Destinatario Telefono
		//Destinatario Fax
		//Destinatario Cellulare
		//Destinatario Email
		//Destinatario ID Fiscale
		//Codice Servizio
		//Codice Accessorio Base
		
		
		//(A) SKU 
		//pw.print(a.getCodice());
		pw.print("	");
	    
	    //(B) CODICE BARRE
		//if (a.getCodiceBarre()!=null && !a.getCodiceBarre().trim().isEmpty())
			//pw.print(a.getCodiceBarre().toUpperCase().trim());
		pw.print("	");
	    
	    
	    /* fine !!! */
	    
	    pw.println();
		
	}
}
