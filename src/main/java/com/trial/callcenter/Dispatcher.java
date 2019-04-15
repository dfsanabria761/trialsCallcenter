package com.trial.callcenter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Dispatcher implements Runnable {

	public static final Integer MAX_LLAMADAS = 10;
	
	private boolean activo ; 
	
	private ExecutorService executorService;
	
	private ConcurrentLinkedQueue<Empleado> empleados;
	
	private ConcurrentLinkedQueue<Llamada> llamadas;
	
	public Dispatcher(List<Empleado> empleados)
	{
		this.empleados = new ConcurrentLinkedQueue<Empleado>(empleados);
		this.llamadas = new ConcurrentLinkedQueue<Llamada>();
		
		this.executorService=Executors.newFixedThreadPool(MAX_LLAMADAS);
		
	}
	
	public synchronized void dispatchCall(Llamada llamada) {
		System.out.println("Atendiendo llamada. \n Duración: "+llamada.getDuracion()+" s.");
		this.llamadas.add(llamada);
	}
	
	public synchronized void start() {
		this.activo=true;
		
		for (Empleado empleado : this.empleados) {
			this.executorService.execute(empleado);
		}
	}
	
	public synchronized void stop() {
		this.activo=false;
		this.executorService.shutdown();
	}

	public synchronized boolean isActivo() {
		return activo;
	}
 
	public synchronized void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Override
	public void run() {
		while(isActivo()) {
			if(this.llamadas.isEmpty()) {
				continue;
			}else {
				Empleado empleado = this.encontrarEmpleadoDisponible(this.empleados);
				if(empleado == null ) {
					continue;
				}else {
					Llamada llamada = this.llamadas.poll();
					try {
						empleado.atender(llamada);
					}catch(Exception e) {
						System.out.println("Error encontrado: "+e.getMessage());
						this.llamadas.add(llamada);
					}
				}
			}
		}
	}
	
	public Empleado encontrarEmpleadoDisponible(ConcurrentLinkedQueue<Empleado> empleados) {
		List<Empleado> empleadosDisponibles = empleados.stream().filter(empleado -> !empleado.isOcupado()).collect(Collectors.toList());
		System.out.println("Existen " + empleadosDisponibles.size()+" empleados disponibles.");
		Optional<Empleado> empleado = empleadosDisponibles.stream().filter(em -> em.getTipoEmpleado()==TipoEmpleado.OPERADOR).findAny();
		if(!empleado.isPresent()) {
			System.out.println("No existen operadores disponibles en el momento.");
			empleado = empleadosDisponibles.stream().filter(em -> em.getTipoEmpleado()==TipoEmpleado.SUPERVISOR).findAny();
			if(!empleado.isPresent()) {
				System.out.println("No existen supervisores disponibles en el momento.");
				empleado = empleadosDisponibles.stream().filter(em -> em.getTipoEmpleado()==TipoEmpleado.DIRECTOR).findAny();
				if(!empleado.isPresent()) {
					System.out.println("No existen directores disponibles en el momento.");
					return null;
				}
			}
		}
		System.out.println("Se encontró un empleado disponible, el tipo es: "+empleado.get().getTipoEmpleado());
		return empleado.get();
	}
	
	
}
