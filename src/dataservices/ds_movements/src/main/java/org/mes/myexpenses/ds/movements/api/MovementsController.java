package org.mes.myexpenses.ds.movements.api;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.mes.myexpenses.commons.exceptions.exceptions.ElementNotFoundException;
import org.mes.myexpenses.commons.exceptions.exceptions.OperationNotCompletedException;
import org.mes.myexpenses.commons.rest.metrics.ActuatorMeterRegistry;
import org.mes.myexpenses.ds.movements.constants.MyExpensesConstants;
import org.mes.myexpenses.ds.movements.domains.CounterMovements;
import org.mes.myexpenses.ds.movements.domains.Movement;
import org.mes.myexpenses.ds.movements.domains.MovementId;
import org.mes.myexpenses.ds.movements.repositories.MovementsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = MyExpensesConstants.ENDPOINTS.MOVEMENTS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MovementsController {
	
	private final MovementsRepository movementsRepository;
	private final ActuatorMeterRegistry actuatorMeterRegistry;
	
	public MovementsController(MovementsRepository movementsRepository, ActuatorMeterRegistry actuatorMeterRegistry) {
		this.movementsRepository = movementsRepository;
		this.actuatorMeterRegistry = actuatorMeterRegistry;
		actuatorMeterRegistry.createCounters(
				actuatorMeterRegistry.getMethodNames(this.getClass(), MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS));
	}
	
	@PostMapping("/findById")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Optional<Movement> findById(@RequestBody @Valid MovementId movementId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_FINDBYID;
		log.info("{}/{}", methodName, movementId);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		Optional<Movement> expense = movementsRepository.findById(movementId);
		if(!expense.isPresent()) {
			String detailed = String.format("%s %s not found.", methodName, movementId);
			log.error(detailed);
			throw new ElementNotFoundException(detailed);
		}
		return expense;
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Iterable<Movement> findAll() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_FINDALL;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		List<Movement> movements = movementsRepository.findAll();
		log.info("{} size {}", methodName, movements.size());
		return movements;
	}
	
	@GetMapping("/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CounterMovements count() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_COUNT;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		CounterMovements counter = new CounterMovements();
		counter.setMovements(movementsRepository.count());
		
		long expenses = 0;
		for(Movement movement: findAll()) {
			expenses+= movement.getExpenses().size();
		}
		counter.setExpenses(expenses);
		
		log.info("{} count {}", methodName, counter);
		return counter;
	}
	
	@PostMapping("/existsById")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public boolean existsById(@RequestBody @Valid MovementId movementId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_EXISTBYID;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		return movementsRepository.existsById(movementId);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Movement save(@RequestBody @Valid Movement movement) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_SAVE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		Movement expenseSaved = movementsRepository.save(movement);
		if(!expenseSaved.equals(movement)) {
			String detailed = String.format("%s %s not found.", methodName, movement.getId());
			log.error(detailed);
			throw new OperationNotCompletedException(detailed);
		}
		
		return movement;
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestBody @NotNull Movement expense) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_DELETE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		movementsRepository.delete(expense);
	}
	
	@DeleteMapping("/deleteById")
	@ResponseStatus(HttpStatus.OK)
	public void deleteById(@RequestBody @Valid MovementId movementId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_DELETEBYID;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		movementsRepository.deleteById(movementId);
	}

}