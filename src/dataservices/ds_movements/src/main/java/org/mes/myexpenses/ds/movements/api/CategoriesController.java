package org.mes.myexpenses.ds.movements.api;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.mes.myexpenses.commons.exceptions.exceptions.ElementNotFoundException;
import org.mes.myexpenses.commons.exceptions.exceptions.OperationNotCompletedException;
import org.mes.myexpenses.commons.rest.metrics.ActuatorMeterRegistry;
import org.mes.myexpenses.ds.movements.constants.MyExpensesConstants;
import org.mes.myexpenses.ds.movements.domains.Category;
import org.mes.myexpenses.ds.movements.domains.CategoryId;
import org.mes.myexpenses.ds.movements.domains.CounterCategories;
import org.mes.myexpenses.ds.movements.repositories.CategoriesRepository;
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
	
	private final CategoriesRepository categoryRepository;
	private final ActuatorMeterRegistry actuatorMeterRegistry;

	public CategoriesController(CategoriesRepository categoryRepository, ActuatorMeterRegistry actuatorMeterRegistry) {
		this.categoryRepository = categoryRepository;
		this.actuatorMeterRegistry = actuatorMeterRegistry;
		actuatorMeterRegistry.createCounters(
				actuatorMeterRegistry.getMethodNames(this.getClass(), MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES));
	}

	@PostMapping("/findById")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Category findById(@RequestBody @Valid CategoryId categoryId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_FINDBYID;
		log.info("{}/{}", methodName, categoryId);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		Optional<Category> category = categoryRepository.findById(categoryId);
		if(!category.isPresent()) {
			String detailed = String.format("%s %s not found.", methodName, categoryId);
			log.error(detailed);
			throw new ElementNotFoundException(detailed);
		}
		return category.get();
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Iterable<Category> findAll() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_FINDALL;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		List<Category> categories = categoryRepository.findAll();
		log.info("{} size {}", methodName, categories.size());
		return categories;
	}
	
	@GetMapping("/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CounterCategories count() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_COUNT;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		CounterCategories count = new CounterCategories();
		count.setCategories(categoryRepository.count());
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
		
		return categoryRepository.existsById(categoryId);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Category save(@RequestBody @Valid Category category) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_SAVE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		Category categorySaved = categoryRepository.save(category);
		if(!categorySaved.equals(category)) {
			String detailed = String.format("%s %s not found.", methodName, category.getId());
			log.error(detailed);
			throw new OperationNotCompletedException(detailed);
		}
		
		return category;
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestBody @NotNull Category category) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_DELETE;
		log.info("{}/{}", methodName, category);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		categoryRepository.delete(category);
	}
	
	@DeleteMapping("/deleteById")
	@ResponseStatus(HttpStatus.OK)
	public void deleteById(@RequestBody @Valid CategoryId categoryId) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_CATEGORIES_DELETEBYID;
		log.info("{}/{}", methodName, categoryId);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		categoryRepository.deleteById(categoryId);
	}

}