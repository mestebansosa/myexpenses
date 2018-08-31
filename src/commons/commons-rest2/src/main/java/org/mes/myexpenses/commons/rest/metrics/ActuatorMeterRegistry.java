package org.mes.myexpenses.commons.rest.metrics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ActuatorMeterRegistry {

	// http://micrometer.io/docs/concepts#_function_tracking_counters. Chapter 8, Counters
	// http://www.baeldung.com/spring-boot-actuators
	// https://docs.spring.io/spring-boot/docs/2.0.x/actuator-api/html/
	
	// http://localhost:9000/myexpenses/actuator/myexpenses
	
	private SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();

	public SimpleMeterRegistry getMeterRegistry() {
		return meterRegistry;
	}
	
	public void createCounters(List<String> names) {
		for(String name : names) {
			meterRegistry.counter(name);
			// log.info("'{}' counter registered.", name);
		}		
		for(Meter meter : meterRegistry.getMeters()) {
			log.info("meter '{}' '{}'", meter.getId(), meter.measure());
		}
	}
	
	public void incrementCounter(String name) {
		// if name is not found in meterRegistry an Exception is thrown
		meterRegistry.find(name).counter().increment();
	}
	
	public Iterable<Measurement> dataCounter(String name) {
		return meterRegistry.find(name).counter().measure();
	}

	public List<String> getMethodNames(Class<?> c, String path) {
		List<String> names = new ArrayList<>();
		for (Method method : c.getDeclaredMethods()) {
			if (method.getAnnotation(GetMapping.class) != null 
					|| method.getAnnotation(PostMapping.class) != null
					|| method.getAnnotation(DeleteMapping.class) != null
					|| method.getAnnotation(PutMapping.class) != null) {
				names.add(path + "." + method.getName());
			}
		}
		return names;
	}

}
