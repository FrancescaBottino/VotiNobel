package it.polito.tdp.nobel.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private List<Esame> soluzioneMigliore; //usata solo quando trovo soluzioni migliore di quelle trovate
	private double mediaSoluzioneMigliore;
	
	
public Model() {
	
	EsameDAO dao=new EsameDAO(); //per riempire partenza, lo faccio dal db
	this.partenza=dao.getTuttiEsami();
	
}
	
	
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		//Livello più alto della ricorsione, + la chiamo
		
		List<Esame> parziale= new ArrayList<Esame>(); //inizialmente vuoto
		
		soluzioneMigliore= new ArrayList<Esame>(); //Inizialmente vuoto 
		
		mediaSoluzioneMigliore=0; //pulisco
		
		//cerca1(parziale, 0, numeroCrediti); //chiama la ricorsione, livello =0 PRIMO APPROCCIO
		
		cerca1(parziale, 0, numeroCrediti); //SECONDO APPROCCIO
		
		return soluzioneMigliore;	 //vuota se non ne trova nessuna
	}
	
	//COMPLESSITA : 2^N , SEMPRE ESPONENZIALE, ma migliore
	//APPROCCIO 2)
	/*
	public void cerca2 (List<Esame> parziale, int L, int m) {
		
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
				soluzioneMigliore=new ArrayList<>(parziale); //sovrascrivo tutto
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
	*/
	
	

	//COMPLESSITA : N! (simile anagrammi) 
	//APPROCCIO 1)
	
	private void cerca1(List<Esame> parziale, int L, int m) {
		
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
				soluzioneMigliore=new ArrayList<>(parziale); //sovrascrivo tutto
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
		/*for(Esame e: partenza) {
			
			//per migliorare soluzione, evito di controllare soluzioni inutili, elemente scabiati
			
			if(!parziale.contains(e))  //aggiungo+lancio+rimuovo
				{ parziale.add(e);
				  cerca1(parziale, L+1, m); 
				  parziale.remove(e);
				}
			
				}
		*/
		
		/*soluzione forse migliore: 
		 * FACCIO IL CONTROLLO IN PIU SUI LIVELLI PER EVITARE TUTTI TUTTI I DUPLICATI, VADO IN ORDINE 
		 * 
		 * 
		 * for(int i=0; i<partenza.size(); i++){
		 * 
		 * if(!parziale.contains(partenza.get(i)) && i>=L) {
		 * 
		 * parziale.add(partenza.get(i));
		 * cerca1(parziale, L+1, m);
		 * parziale.remove(partenza.get(i));
		 * }
		 * }
		 * 
		 * 
		 * CONTROLLO ULTIMO ELEMENTO INSERITO E VADO A VEDERE LA SUA POSIZIONE
		 * PER VEDERE SE POSSO INSERIRLO O MENO
		 * NO-> PERCHE PARZIALE E' UN SET
		 * ---> USO LINKEDHASHSET OPPURE LISTA 
		 */
			
			
		int lastIndex=0;
		if(parziale.size()>0) {
			lastIndex= partenza.indexOf(parziale.get(parziale.size()-1)); //mi salvo la posizione dell'ultimo elemento inserito
			
		}
		
		for(int i=lastIndex; i<partenza.size(); i++) {
			
			if(!parziale.contains(partenza.get(i)) && i>=L) {
				  
				  parziale.add(partenza.get(i));
				  cerca1(parziale, L+1, m);
				  parziale.remove(partenza.get(i));
				}
			
			}
		
		}
		
		
		

	public double calcolaMedia(List<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(List<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
