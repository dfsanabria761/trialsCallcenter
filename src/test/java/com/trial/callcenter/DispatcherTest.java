package com.trial.callcenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class DispatcherTest {
	private static final int LLAMADAS= 10;
	@Test
	public void testDispatchLlamadas() throws InterruptedException{
		List<Empleado> empleados = crearEmpleados();
		Dispatcher dispatcher = new Dispatcher(empleados);
		dispatcher.start();
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(dispatcher);
		
		crearLlamadas().stream().forEach(ll ->{
			dispatcher.dispatchCall(ll);
			try {
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e) {
				fail();
			}
		});
		
		es.awaitTermination(20, TimeUnit.SECONDS);
		assertEquals(LLAMADAS, empleados.stream().mapToInt(empleado-> empleado.getLlamadasAtendidas()).sum());
	}
	
	private static List<Llamada> crearLlamadas(){
		ArrayList<Llamada> llamadas = new ArrayList<>();
		for(int i = 0 ; i < LLAMADAS; i++) {
			llamadas.add(new Llamada());
		}
		return llamadas;
	}
	
	private static List<Empleado> crearEmpleados(){
		Empleado e1 = new Empleado(TipoEmpleado.OPERADOR);
		Empleado e2= new Empleado(TipoEmpleado.OPERADOR);
		Empleado e3 = new Empleado(TipoEmpleado.SUPERVISOR);
		Empleado e4 = new Empleado(TipoEmpleado.SUPERVISOR);
		Empleado e5 = new Empleado(TipoEmpleado.SUPERVISOR);
		Empleado s1 = new Empleado(TipoEmpleado.SUPERVISOR);
		Empleado s2 = new Empleado(TipoEmpleado.SUPERVISOR);
		Empleado s3 = new Empleado(TipoEmpleado.SUPERVISOR);
		Empleado d1 = new Empleado(TipoEmpleado.DIRECTOR);
		Empleado d2 = new Empleado(TipoEmpleado.DIRECTOR);
		return Arrays.asList(e1,e2,e3,e4,e5,s1,s2,s3,d1,d2);
	}
}
