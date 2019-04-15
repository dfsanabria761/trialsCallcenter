package com.trial.callcenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class EmpleadoTest {
	@Test(expected = NullPointerException.class)
	public void testCreacionIncorrecta() {
		new Empleado(null);
	}
	
	@Test
	public void testCrearOperador() {
		Empleado e = new Empleado(TipoEmpleado.OPERADOR);
		assertNotNull(e);
		assertEquals(e.getTipoEmpleado(),TipoEmpleado.OPERADOR);
		assertFalse(e.isOcupado());
	}
	
	@Test
	public void testCrearSupervisor() {
		Empleado e = new Empleado(TipoEmpleado.SUPERVISOR);
		assertNotNull(e);
		assertEquals(e.getTipoEmpleado(),TipoEmpleado.SUPERVISOR);
		assertFalse(e.isOcupado());
	}
	
	@Test
	public void testCrearDirector() {
		Empleado e = new Empleado(TipoEmpleado.DIRECTOR);
		assertNotNull(e);
		assertEquals(e.getTipoEmpleado(),TipoEmpleado.DIRECTOR);
		assertFalse(e.isOcupado());
	}
	
	@Test
	public void testAtenderEstandoDisponible() throws InterruptedException {
		Empleado e = new Empleado(TipoEmpleado.OPERADOR);

		ExecutorService es = Executors.newSingleThreadExecutor();

		es.execute(e);
		e.atender(new Llamada());
		es.awaitTermination(11, TimeUnit.SECONDS);
		assertEquals(e.getLlamadasAtendidas(),1);
		
	}
	
}
