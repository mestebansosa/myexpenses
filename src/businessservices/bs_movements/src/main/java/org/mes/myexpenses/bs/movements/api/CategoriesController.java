package org.mes.myexpenses.bs.movements.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.mes.myexpenses.bs.movements.constants.MyExpensesConstants;
import org.mes.myexpenses.bs.movements.domains.Category;
import org.mes.myexpenses.bs.movements.domains.CategoryId;
import org.mes.myexpenses.bs.movements.domains.CounterCategories;
import org.mes.myexpenses.bs.movements.service.Categories;
import org.mes.myexpenses.bs.movements.service.CategoriesImpl;
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
@RequestMapping(value = MyExpensesConstants.ENDPOINTS.CATEGORIES, produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoriesController {
	
	private final Categories categories;
	private final ActuatorMeterRegistry actuatorMeterRegistry;

	public CategoriesController(CategoriesImpl categories, ActuatorMeterRegistry actuatorMeterRegistry) {
		this.categories = categories;
		this.actuatorMeterRegistry = actuatorMeterRegistry;
		actuatorMeterRegistry.createCounters(
				actuatorMeterRegistry.getMethodNames(this.getClass(), MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES));
	}

	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Category create(@RequestBody @Valid Category category) throws DuplicatedEntryException, OperationNotCompletedException {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_CREATE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		// If the entry exists, thrown an exception.
		if(existsById(category.getId())) {
			String detailed = String.format("%s %s already exists.", methodName, category.getId());
			log.error(detailed);
			throw new DuplicatedEntryException(detailed);
		}
		
		Category categorySaved = categories.save(category);
		if(!categorySaved.equals(category)) {
			String detailed = String.format("%s %s not saved.", methodName, category.getId());
			log.error(detailed);
			throw new OperationNotCompletedException(detailed);
		}
		
		return category;
	}

	@PostMapping("/update")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Category update(@RequestBody @Valid Category category) throws ElementNotFoundException {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_UPDATE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		// If the entry does not exists, thrown an exception.
		if(!existsById(category.getId())) {
			String detailed = String.format("%s %s not found.", methodName, category.getId());
			log.error(detailed);
			throw new ElementNotFoundException(detailed);
		}
		
		Category categorySaved = categories.save(category);
		if(!categorySaved.equals(category)) {
			String detailed = String.format("%s %s not saved.", methodName, category.getId());
			log.error(detailed);
			throw new ElementNotFoundException(detailed);
		}
		
		return category;
	}

	@PostMapping("/findById")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Category findById(@RequestBody @Valid CategoryId categoryId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_FINDBYID;
		log.info("{}/{}", methodName, categoryId);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		return categories.findById(categoryId);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Iterable<Category> findAll() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_FINDALL;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		List<Category> categoryList = categories.findAll();
		log.info("{} size {}", methodName, categoryList.size());
		return categoryList;
	}
	
	@GetMapping("/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CounterCategories count() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_COUNT;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		CounterCategories count = categories.count();
		log.info("{} count {}", methodName, count);
		return count;
	}
	
	@PostMapping("/existsById")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public boolean existsById(@RequestBody @Valid CategoryId categoryId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_EXISTBYID;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		return categories.existsById(categoryId);
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestBody @NotNull Category category) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_DELETE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		categories.delete(category);
	}
	
	@DeleteMapping("/deleteById")
	@ResponseStatus(HttpStatus.OK)
	public void deleteById(@RequestBody @Valid CategoryId categoryId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_DELETEBYID;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		categories.deleteById(categoryId);
	}

}