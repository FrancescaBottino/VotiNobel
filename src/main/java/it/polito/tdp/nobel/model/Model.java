package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore; //usata solo quando trovo soluzioni migliore di quelle trovate
	private double mediaSoluzioneMigliore;
	
	
public Model() {
	
	EsameDAO dao=new EsameDAO(); //per riempire partenza, lo faccio dal db
	this.partenza=dao.getTuttiEsami();
	
}
	
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		//Livello più alto della ricorsione, + la chiamo
		
		Set<Esame> parziale= new HashSet<Esame>(); //inizialmente vuoto
		
		soluzioneMigliore= new HashSet<Esame>(); //Inizialmente vuoto 
		
		mediaSoluzioneMigliore=0; //pulisco
		
		//cerca1(parziale, 0, numeroCrediti); //chiama la ricorsione, livello =0 PRIMO APPROCCIO
		
		cerca2(parziale, 0, numeroCrediti); //SECONDO APPROCCIO
		
		return soluzioneMigliore;	 //vuota se non ne trova nessuna
	}
	
	//COMPLESSITA : 2^N , SEMPRE ESPONENZIALE, ma migliore
	public void cerca2 (Set<Esame> parziale, int L, int m) {
		
		//metodo ricorsivo 
		
	    //1) casi terminali
				
		//controllo crediti
		int crediti=sommaCrediti(parziale); //somma crediti attuali nella parziale
				
		if(crediti>m) {
			return;
		}
		if(crediti==m) {
		  //potenzialemnte va bene , ma la sua media deve essere milgiore di tute le altre calcolate fino ad ora
			double media=calcolaMedia(parziale);
					
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new HashSet<>(parziale); //sovrascrivo tutto
				mediaSoluzioneMigliore=media; //mi salvo anche la sol.
					}
					
			return;
		}
				
		//qui crediti saranno < m
		//raggiungiamo L=N --> non ci sono più esami da aggiungere
		if(L==partenza.size()) {
			return;
					
		}
				
		//2) Genero sottoproblemi
		//Partenza[L] è da aggiungere o no? provo entrambe
		
		parziale.add(partenza.get(L));
		cerca2(parziale, L+1, m); //se aggiungo L
		
		parziale.remove(partenza.get(L));
		cerca2(parziale, L+1, m); //se non aggiungo L
		
	}
	

	//COMPLESSITA : N! (simile anagrammi)
	private void cerca1(Set<Esame> parziale, int L, int m) {
		
		//metodo ricorsivo 
		
		//1) casi terminali
		
		//controllo crediti
		int crediti=sommaCrediti(parziale); //somma crediti attuali nella parziale
		
		if(crediti>m) {
			return;
		}
		if(crediti==m) {
			//potenzialemnte va bene , ma la sua media deve essere milgiore di tute le altre calcolate fino ad ora
			double media=calcolaMedia(parziale);
			
			if(media>mediaSoluzioneMigliore) {
				soluzioneMigliore=new HashSet<>(parziale); //sovrascrivo tutto
				mediaSoluzioneMigliore=media; //mi salvo anche la sol.
			}
			
			return;
		}
		
		//qui crediti saranno < m
		//raggiungiamo L=N --> non ci sono più esami da aggiungere
		if(L==partenza.size()) {
			return;
			
		}
		
		
		//2) Generare i sotto-problemi
		for(Esame e: partenza) {
			
			//per migliorare soluzione, evito di controllare olsuzioni inutili, elemente scabiati
			
			if(!parziale.contains(e))  //aggiungo+lancio+rimuovo
				{ parziale.add(e);
				  cerca1(parziale, L+1, m); 
				  parziale.remove(e);
				}
			
				}
			
			
		}
		
		
		
		
	


	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
