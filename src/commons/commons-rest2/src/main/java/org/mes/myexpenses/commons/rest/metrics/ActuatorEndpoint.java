package org.mes.myexpenses.commons.rest.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Meter.Id;

@Component
@Endpoint(id="counters")
public class ActuatorEndpoint {

	@Autowired
	private ActuatorMeterRegistry actuatorMeterRegistry;

    private final Map<String, Double> counters = new HashMap<>();

    ActuatorEndpoint() {}

    @ReadOperation
    public List<Meter> getCounters(@Selector String name) {
		return new ArrayList<Meter>(actuatorMeterRegistry.getMeterRegistry().getMeters());
    }
  
    @ReadOperation
    public Map<String, Double> getCounts() {
    	for( Meter meter: actuatorMeterRegistry.getMeterRegistry().getMeters()) {
    		Id id = meter.getId();
    		Counter counter = actuatorMeterRegistry.getMeterRegistry().counter(id.getName());
    		counters.put(id.getName(), counter.count());
    	}
		return counters;
    }   

}