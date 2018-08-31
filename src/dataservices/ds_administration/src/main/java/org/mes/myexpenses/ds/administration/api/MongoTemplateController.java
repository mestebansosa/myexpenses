package org.mes.myexpenses.ds.administration.api;

import javax.validation.Valid;

import org.mes.myexpenses.commons.rest.metrics.ActuatorMeterRegistry;
import org.mes.myexpenses.ds.administration.constants.MyExpensesConstants;
import org.mes.myexpenses.ds.administration.domains.Session;
import org.mes.myexpenses.ds.administration.domains.User;
import org.mes.myexpenses.ds.administration.service.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = MyExpensesConstants.ENDPOINTS.MONGO, produces = MediaType.APPLICATION_JSON_VALUE)
public class MongoTemplateController {

	private final MongoOperations mongoOperations;
	private final ActuatorMeterRegistry actuatorMeterRegistry;

	public MongoTemplateController(MongoOperations mongoOperations, ActuatorMeterRegistry actuatorMeterRegistry) {
		this.mongoOperations = mongoOperations;
		this.actuatorMeterRegistry = actuatorMeterRegistry;
		actuatorMeterRegistry.createCounters(
				actuatorMeterRegistry.getMethodNames(this.getClass(), MyExpensesConstants.ENDPOINTS.COUNTER_MONGO));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public User findOne(@RequestParam String name) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MONGO_FINDONE;
		log.info("{}/{}", methodName, name);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		return mongoOperations.findOne(name);
	}
	
	@PutMapping("/updateCandidateSessions")
	@ResponseStatus(HttpStatus.OK)
	public void updateCandidateSessions(@RequestBody @Valid Session session) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MONGO_UPDATECANDIDATESESSIONS;
		log.info("{}/{}", methodName, session);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		mongoOperations.updateCandidateSessions(session);
	}

}