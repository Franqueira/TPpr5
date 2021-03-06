package es.ucm.fdi.simobject;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import es.ucm.fdi.model.simobject.Junction;
import es.ucm.fdi.model.simobject.Road;
import es.ucm.fdi.model.simobject.Vehicle;
import static org.junit.Assert.*;

public class JunctionTest {
	List<Junction> itinerary = new ArrayList<>();
	@Test
	public void junctionTest() {
		itinerary.add(new Junction("j1"));
		itinerary.add(new Junction("j2"));
		itinerary.add(new Junction("j3"));
		Vehicle v = new Vehicle(5, itinerary, "v1");
		Vehicle v1 = new Vehicle(15, itinerary, "v2");
		Vehicle v2 = new Vehicle(15, itinerary, "v3");
		Road r1 = new Road("r1", 30, 10, itinerary.get(0), itinerary.get(1));
		Road r2 = new Road("r2", 15, 20, itinerary.get(1), itinerary.get(2));
		itinerary.get(1).addIncoming(r1);
		itinerary.get(1).addOutgoing(r2);
		itinerary.get(2).addIncoming(r2);
		v.moveToNextRoad(r1);
		v1.moveToNextRoad(r1);
		v2.moveToNextRoad(r1);
		Junction test = itinerary.get(1);
		test.addVehicle(v);
		test.addVehicle(v1);
		test.addVehicle(v2);
		test.advance();
		assertTrue("El método avanza no funciona bien",
				v.getRoad() == r2 && v1.getRoad() == r1 && v2.getRoad() == r1);
		test.advance();
		assertTrue("El método avanza no funciona bien", v1.getRoad() == r2
				&& v2.getRoad() == r1);
		test = itinerary.get(2);
		test.addVehicle(v);
		test.addVehicle(v1);
		test.advance();
		assertTrue("No hace que un vehículo llegue",
				v.getArrived() && !v1.getArrived());
	}

}
