package es.ucm.fdi.model.simobject;

import java.util.List;
import java.util.Map;

import es.ucm.fdi.view.Describable;
/**Vehículo*/
public class Vehicle extends SimObject implements Describable{
	protected int maxSpeed;
	protected int realSpeed;
	protected Road realRoad;
	protected int location;
	protected List<Junction> itinerary;
	protected int nextJunction;
	protected int faultyTime;
	protected int kilometrage;
	protected boolean isArrived;

	public Vehicle(int maxSpeed, List<Junction> itinerary, String id) {
		super(id);
		location = 0;
		this.maxSpeed = maxSpeed;
		realSpeed = 0;
		faultyTime = 0;
		this.itinerary = itinerary;
		kilometrage = 0;
		nextJunction = 1;
		isArrived = false;
	}

	public int getLocation() {
		return location;
	}

	public void setTimeFault(int n) {
		faultyTime += n;
		realSpeed = 0;
	}

	public Junction getNextJunction() {
		if (nextJunction != itinerary.size()){
			return itinerary.get(nextJunction);
		}else{
			return null;
		}
	}

	public Road getRoad() {
		return realRoad;
	}

	public int getFaultyTime() {
		return faultyTime;
	}

	public int getRealSpeed() {
		return realSpeed;
	}

	public void setRealSpeed(int vel) {
		if (vel > maxSpeed){
			realSpeed = maxSpeed;
		}else{
			realSpeed = vel;
		}
	}
	/**Avanza en la carretera actual a la velocidad que tiene como atributo*/
	public void advance() {
		if (faultyTime > 0) {
			faultyTime--;
		} else {
			if (location + realSpeed >= realRoad.getLength()) {
				kilometrage += realRoad.getLength() - location;
				location = realRoad.getLength();
				realSpeed = 0;

				realRoad.getEndJunction().addVehicle(this);
			} else {
				kilometrage += realSpeed;
				location += realSpeed;
			}
		}
	}
	/**Cambia de carretera al vehículo*/
	public void moveToNextRoad(Road r) {
		r.newVehicleR(this);
		nextJunction++;
		realRoad = r;
		location = 0;
		realSpeed = 0;
	}

	public boolean getArrived() {
		return isArrived;
	}

	public void arrived() {
		isArrived = true;
	}
	/**Guarda sus atributos en el mapa pasado como argumento*/
	protected void fillReportDetails(Map<String, String> out) {
		out.put("speed", "" + realSpeed);
		out.put("kilometrage", "" + kilometrage);
		out.put("faulty", "" + faultyTime);
		if (!isArrived){
			out.put("location", "(" + realRoad.getId() + "," + location + ")");
		} else{
			out.put("location", "arrived");
		}
	}

	public String getFillVehicle() {
		return ("(" + id + "," + location + ")");

	}

	protected String getReportHeader() {
		return "vehicle_report";
	}

	@Override
	public void describe(Map<String, String> out) {
	out.put("ID", this.id);
	out.put("Road",this.realRoad.getId());
	out.put("Location",""+this.location);
	out.put("Speed",""+this.realSpeed);
	out.put("Km",""+this.kilometrage);
	out.put("Faulty Units",""+this.faultyTime);
	StringBuilder junctions=new StringBuilder();
	itinerary.forEach(junction->junctions.append(junction.getId()+","));
	if(itinerary.size()!=0){
		junctions.delete(junctions.length() - 1,
				junctions.length());
	}
	out.put("Itinerary","["+junctions+"]");
	}
}
