package org.mes.myexpenses.bs.movements.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.mes.myexpenses.bs.movements.constants.MyExpensesConstants;
import org.mes.myexpenses.bs.movements.domains.CounterMovements;
import org.mes.myexpenses.bs.movements.domains.Expense;
import org.mes.myexpenses.bs.movements.domains.Movement;
import org.mes.myexpenses.bs.movements.domains.MovementId;
import org.mes.myexpenses.bs.movements.service.Categories;
import org.mes.myexpenses.bs.movements.service.CategoriesImpl;
import org.mes.myexpenses.bs.movements.service.Movements;
import org.mes.myexpenses.bs.movements.service.MovementsImpl;
import org.mes.myexpenses.commons.exceptions.exceptions.DuplicatedEntryException;
import org.mes.myexpenses.commons.exceptions.exceptions.ElementNotFoundException;
import org.mes.myexpenses.commons.exceptions.exceptions.OperationNotCompletedException;
import org.mes.myexpenses.commons.rest.metrics.ActuatorMeterRegistry;
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
	
	private final Movements movements;
	private final Categories categories;
	private final ActuatorMeterRegistry actuatorMeterRegistry;

	public MovementsController(MovementsImpl movements, CategoriesImpl categories, ActuatorMeterRegistry actuatorMeterRegistry) {
		this.movements = movements;
		this.categories = categories;
		this.actuatorMeterRegistry = actuatorMeterRegistry;
		actuatorMeterRegistry.createCounters(
				actuatorMeterRegistry.getMethodNames(this.getClass(), MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Movement create(@RequestBody @Valid Movement movement) throws DuplicatedEntryException, OperationNotCompletedException {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_CREATE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		// If the entry exists, thrown an exception.
		if(existsById(movement.getId())) {
			String detailed = String.format("%s %s already exists.", methodName, movement.getId());
			log.error(detailed);
			throw new DuplicatedEntryException("Already exists", detailed);
		}

		checkExpenses(movement.getExpenses());
		
		Movement movementSaved = movements.save(movement);
		if(!movementSaved.equals(movement)) {
			String detailed = String.format("%s %s not saved.", methodName, movement.getId());
			log.error(detailed);
			throw new OperationNotCompletedException(detailed);
		}
		
		return movement;
	}

	@PostMapping("/expenses/add")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Movement expensesAdd(@RequestBody @Valid Movement movement) throws ElementNotFoundException {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_EXPENSESS_ADD;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		// If the entry does not exists, thrown an exception.
		if(!existsById(movement.getId())) {
			String detailed = String.format("%s %s not found.", methodName, movement.getId());
			log.error(detailed);
			throw new ElementNotFoundException(detailed);
		}

		// TODO Check out repeats elements in expenses.
		checkExpenses(movement.getExpenses());
		
		// Add new expenses to the current movement. 
		Movement currentMovement = movements.findById(movement.getId());
		List<Expense> currentExpenses = currentMovement.getExpenses();
		boolean result = currentExpenses.addAll(movement.getExpenses());
		if(!result) log.warn("No expenses added to the list.");

		currentExpenses.sort((a, b) -> a.compareTo(b));
		
		Movement movementSaved = movements.save(currentMovement);
/*		if(!movementSaved.equals(movement)) {
			log.error("Movement with id '{}' not saved.", movement);
			throw new ElementNotFoundException("Movement not saved.");
		}
*/		
		return movementSaved;
	}


	@PostMapping("/expenses/delete")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Movement expensesDelete(@RequestBody @Valid Movement movement) throws ElementNotFoundException {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_EXPENSESS_DELETE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		// If the entry does not exists, thrown an exception.
		if(!existsById(movement.getId())) {
			String detailed = String.format("%s %s not found.", methodName, movement.getId());
			log.error(detailed);
			throw new ElementNotFoundException(detailed);
		}

		// TODO Check out repeats elements in expenses.
		checkExpenses(movement.getExpenses());
		
		// Delete expenses to the current movement. 
		Movement currentMovement = movements.findById(movement.getId());
		List<Expense> currentExpenses = currentMovement.getExpenses();
		boolean result = currentExpenses.removeAll(movement.getExpenses());
		if(!result) log.warn("No expenses deleted from the list.");
		currentExpenses.sort((a, b) -> a.compareTo(b));
		
		Movement movementSaved = movements.save(currentMovement);
/*		if(!movementSaved.equals(movement)) {
			log.error("Movement with id '{}' not saved.", movement);
			throw new ElementNotFoundException("Movement not saved.");
		}
*/		
		return movementSaved;
	}


	@PostMapping("/findById")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Movement findById(@RequestBody @Valid MovementId movementId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_FINDBYID;
		log.info("{}/{}", methodName, movementId);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		return movements.findById(movementId);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Iterable<Movement> findAll() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_FINDALL;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		List<Movement> movementList = movements.findAll();
		log.info("{} size {}", methodName, movementList.size());
		return movementList;
	}
	
	@GetMapping("/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CounterMovements count() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_COUNT;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		CounterMovements count = movements.count();
		log.info("{} count {}", methodName, count);
		return count;
	}
	
	@PostMapping("/existsById")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public boolean existsById(@RequestBody @Valid MovementId movementId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_EXISTBYID;
		log.info("{}/{}", methodName, movementId);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		return movements.existsById(movementId);
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestBody @NotNull Movement movement) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_DELETE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		movements.delete(movement);
	}
	
	@DeleteMapping("/deleteById")
	@ResponseStatus(HttpStatus.OK)
	public void deleteById(@RequestBody @Valid MovementId movementId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_MOVEMENTS_DELETEBYID;
		log.info("{}/{}", methodName, movementId);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		movements.deleteById(movementId);
	}

	/**
	 * Check out expenses have a correct Category
	 * @param expenses
	 */
	private void checkExpenses(List<Expense> expenses) throws ElementNotFoundException {
		for(Expense expense: expenses) {
			if(!categories.existsById(expense.getId())) {
				String detailed = String.format("%s %s not found.", "checkExpenses", expense.getId());
				log.error(detailed);
				throw new ElementNotFoundException(detailed);				
			}
		}
	}

}