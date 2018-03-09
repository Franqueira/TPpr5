package es.ucm.fdi.simobject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Junction extends SimulationObject {
	private Map<Road, IncomingRoad> saberInc;
	private List<IncomingRoad> semaforo;
	private List<Road> salientes;
	private Map<Junction, Road> saberSaliente;

	public Junction(String id) {
		super(id);
		saberInc = new HashMap<>(); // el profe puso Linked
		semaforo = new ArrayList<>();
		salientes = new ArrayList<>();
		saberSaliente = new HashMap<>();
	}

	// hay que ver como gestionamos la creación de Roads e Incoming Roads.
	public void entraVehiculo(Vehicle c) {
		saberInc.get(c.getRoad()).cola.add(c);

	}

	public void insertarSalida(Road salida) {
		salientes.add(salida);
	}

	public void insertSalientes(Junction c, Road r) {
		salientes.add(r);
		saberSaliente.put(c, r);
	}

	public void insertEntrante(Road r) {
		IncomingRoad s = new IncomingRoad(r.getId());
		saberInc.put(r, s);
		semaforo.add(s);

	}

	public void avanza() { // porque tendría puesto con el int lon?
		Vehicle aux;
		boolean a = true;
		int i;
		for (i = 0; i < semaforo.size() && a; i++) {
			if (semaforo.get(i).semaforoVerde) {
				aux = semaforo.get(i).cola.getFirst();
				aux.moverASiguienteCarretera(saberSaliente.get(aux
						.getProxCruce()));
				/*
				 * for (int j = 0; j < salientes.size() && a; j++) { // porque
				 * no // iba a // poder // moverse?
				 * 
				 * if (salientes.get(j).getFinal() == aux.nextJunction()) {//
				 * caso // de // la // carretera // correcta
				 * aux.moverASiguienteCarretera(salientes.get(j));
				 * semaforo.get(i).cola.poll();// n ose si pop vale a = false; }
				 * }
				 */
				semaforo.get(i).cola.pop();// no se si pop vale;
				semaforo.get(i).semaforoVerde = false;

			}

		}
		if (i == semaforo.size())
			i = 0;

		semaforo.get(i).semaforoVerde = true;

	}

	protected void fillReportDetails(Map<String, String> out) {
		StringBuilder reportJunct = new StringBuilder();
		for (int i = 0; i < semaforo.size(); i++) {
			reportJunct.append(semaforo.get(i).GeneraReport() + " , ");

		}
		reportJunct.delete(reportJunct.length() - 3, reportJunct.length() - 1);

		out.put("queues", reportJunct.toString());
	}

	protected String getReportHeader() {

		return "[junction_report]";
	}

	// hay que ver como poner semaforo a verde solo de la primera carretera.
	private class IncomingRoad {
		// como se el identificador de esta carretera para el report
		// debería cambiar todos los strings por StringBuilder no?
		private ArrayDeque<Vehicle> cola;
		private String ide; // entender porque no hace falta
		private boolean semaforoVerde;

		public IncomingRoad(String r) {
			cola = new ArrayDeque<>();
			ide = r;
			semaforoVerde = false;// el primero hay que ponerlo a verde
		}

		// quite método de añadir y quitar el ultimo
		protected String GeneraReport() {
			// String aux = "", aux2 = "";
			StringBuilder a = new StringBuilder();
			StringBuilder a1 = new StringBuilder();
			for (int i = 0; i < cola.size(); i++) { // manera de hacer eficiente
													// esto??
				a.insert(2, cola.getFirst().getId());
				a.insert(2, ", ");
				// aux2+=cola.getFirst().getId() +", ";
				cola.push(cola.getFirst());
				cola.pop();
			}
			a.delete(a.length() - 2, a.length() - 1);// no se si es -1 o
														// -2
			// aux2=aux2.substring(0, aux2.length()-2);
			a1.append("(");
			a1.append(ide);
			a1.append(", ");
			if (semaforoVerde)
				a1.append("green");
			else
				a1.append("red");
			a1.append("[");
			a1.append(a);// no se cual de las dos hay que utilizar.
			a1.append("]");

			// aux="("+ide+", "+"green"+"["+aux2+"]";

			return a1.toString();

		}

	}
}
