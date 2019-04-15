package com.trial.callcenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Clase que modela el objeto empleado, quien atenderá las llamadas.
 * @author df.sanabria@uniandes.edu.co
 *
 */
public class Empleado implements Runnable{
	
	private TipoEmpleado tipoEmpleado;
	
	private Queue<Llamada> colaLlamadas;
	private int llamadasAtendidas;

	private boolean ocupado;
	
	public Empleado(TipoEmpleado tipoEmpleado) {
		if(tipoEmpleado!=null) {
			this.tipoEmpleado=tipoEmpleado;
			this.colaLlamadas = new ConcurrentLinkedQueue<Llamada>();
			this.ocupado=false;		
		}else {
			throw new NullPointerException();
		}
			
	}
	
	
	


	public TipoEmpleado getTipoEmpleado() {
		return tipoEmpleado;
	}


	public void setTipoEmpleado(TipoEmpleado tipoEmpleado) {
		this.tipoEmpleado = tipoEmpleado;
	}


	public synchronized boolean isOcupado() {
		return ocupado;
	}


	public synchronized void setOcupado(boolean ocupado) {
		System.out.println("El empleado número "+Thread.currentThread().getId()+ (ocupado?" se ocupará": " se desocupará"));
		this.ocupado = ocupado;
	}
	
	public synchronized void atender(Llamada llamada) {
		System.out.println("Añadiendo llamada a cola. ");
		this.colaLlamadas.add(llamada);
	}


	public synchronized int getLlamadasAtendidas() {
        return llamadasAtendidas;
    } 
	
	public void run() {
		long id = Thread.currentThread().getId();
		System.out.println("Inicia ejecución del empleado "+id);
		
		while(true) {
			if(!this.colaLlamadas.isEmpty()) {
				Llamada llamada = this.colaLlamadas.poll();
				Llamada t = llamada;
				System.out.println("El empleado "+id + " fue asignado con una llamada de duración: " +llamada.getDuracion());

				this.setOcupado(true);
				try {
					Long dur = (long)(llamada.getDuracion());
					TimeUnit.SECONDS.sleep(dur);
				}catch(Exception e) {
					System.out.println("Error procesando llamada por parte del empleado "+id);
				}
				finally {
					this.setOcupado(false);
				}
				this.llamadasAtendidas++;

				System.out.println("El empleado "+id+" termina ejecución y es liberado.");
			}
		}
		
	}

}
