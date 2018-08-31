package org.mes.myexpenses.ds.administration.api;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.mes.myexpenses.commons.exceptions.exceptions.ElementNotFoundException;
import org.mes.myexpenses.commons.exceptions.exceptions.OperationNotCompletedException;
import org.mes.myexpenses.commons.rest.metrics.ActuatorMeterRegistry;
import org.mes.myexpenses.ds.administration.constants.MyExpensesConstants;
import org.mes.myexpenses.ds.administration.domains.CounterSessions;
import org.mes.myexpenses.ds.administration.domains.Session;
import org.mes.myexpenses.ds.administration.repositories.SessionsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = MyExpensesConstants.ENDPOINTS.SESSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionsController {
	
	private final SessionsRepository sessionsRepository;
	private final ActuatorMeterRegistry actuatorMeterRegistry;

	public SessionsController(SessionsRepository sessionsRepository, ActuatorMeterRegistry actuatorMeterRegistry) {
		this.sessionsRepository = sessionsRepository;
		this.actuatorMeterRegistry = actuatorMeterRegistry;
		actuatorMeterRegistry.createCounters(
				actuatorMeterRegistry.getMethodNames(this.getClass(), MyExpensesConstants.ENDPOINTS.COUNTER_SESSIONS));
	}

	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Session findById(@PathVariable @NotNull String id) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_SESSIONS_FINDBYID;
		log.info("{}/{}", methodName, id);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		Optional<Session> session = sessionsRepository.findById(id);
		if(!session.isPresent()) {
			log.error("{}/{} not found");
		}
		return session.get();
	}
	
	@GetMapping("/firstByName/{name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Session findFirstByStart(@PathVariable @NotNull Date date) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_SESSIONS_FINDFIRSTBYSTART;
		log.info("{}/{}", methodName, date);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		Optional<Session> session = sessionsRepository.findFirstByStart(date);
		if(!session.isPresent()) {
			String detailed = String.format("%s/%s not found.", methodName, date);
			log.error(detailed);
			throw new ElementNotFoundException(detailed);
		}
		
		return session.get();
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Iterable<Session> findAll() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_SESSIONS_FINDALL;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		List<Session> sessions = sessionsRepository.findAll();
		log.info("{} size {}", methodName, sessions.size());
		return sessions;
	}
	
	@GetMapping("/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CounterSessions count() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_SESSIONS_COUNT;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		CounterSessions count = new CounterSessions();
		count.setSessions(sessionsRepository.count());
		log.info("{} count {}", methodName, count);
		return count;
	}
	
	@GetMapping("/exists/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public boolean existsById(@PathVariable @NotNull String id) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_SESSIONS_EXISTBYID;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		return sessionsRepository.existsById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Session save(@RequestBody @Valid Session session) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_SESSIONS_SAVE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		session.setStart(new Date());
		Session sessionSaved = sessionsRepository.save(session);
		if(!sessionSaved.getUser().contentEquals(session.getUser())) {
			String detailed = String.format("%s %s not completed.", methodName, session);
			log.error(detailed);
			throw new OperationNotCompletedException(detailed);
		}
		
		return session;
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestBody @NotNull Session session) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_SESSIONS_DELETE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		sessionsRepository.delete(session);
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteById(@PathVariable @NotNull String id) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_SESSIONS_DELETEBYID;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		sessionsRepository.deleteById(id);
	}

}