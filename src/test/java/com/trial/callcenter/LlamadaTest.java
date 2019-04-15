package com.trial.callcenter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
public class LlamadaTest{
	List<Llamada> llamadas = new ArrayList<Llamada>();
	@Test(expected= NoSuchElementException.class)
	public void testCrearLlamada() {
		
		for (int i = 0; i < 10; i++) {
			Llamada llamada = new Llamada();
			llamadas.add(llamada);
		}
		Optional<Llamada> llamada = llamadas.stream().filter(ll -> ll.getDuracion()<=5 || ll.getDuracion()>=10).findAny();	
		System.out.println(llamada.get().getDuracion());
	}
	
}
