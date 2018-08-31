package org.mes.myexpenses.ds.administration.api;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.mes.myexpenses.commons.exceptions.exceptions.ElementNotFoundException;
import org.mes.myexpenses.commons.exceptions.exceptions.LoginException;
import org.mes.myexpenses.commons.exceptions.exceptions.OperationNotCompletedException;
import org.mes.myexpenses.commons.rest.metrics.ActuatorMeterRegistry;
import org.mes.myexpenses.ds.administration.constants.MyExpensesConstants;
import org.mes.myexpenses.ds.administration.domains.CounterUsers;
import org.mes.myexpenses.ds.administration.domains.Login;
import org.mes.myexpenses.ds.administration.domains.User;
import org.mes.myexpenses.ds.administration.repositories.UsersRepository;
import org.mes.myexpenses.ds.administration.utils.Encoders;
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
@RequestMapping(value = MyExpensesConstants.ENDPOINTS.USERS, produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {
	
	private final UsersRepository usersRepository;
	private final Encoders encoders;
	private final ActuatorMeterRegistry actuatorMeterRegistry;
	
	public UsersController(UsersRepository usersRepository, Encoders encoders,
			ActuatorMeterRegistry actuatorMeterRegistry) {
		this.usersRepository = usersRepository;
		this.encoders = encoders;
		this.actuatorMeterRegistry = actuatorMeterRegistry;
		actuatorMeterRegistry.createCounters(
				actuatorMeterRegistry.getMethodNames(this.getClass(), MyExpensesConstants.ENDPOINTS.COUNTER_USERS));
	}
	
	@GetMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Optional<User> findById(@PathVariable @NotNull String id) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_USERS_FINDBYID;
		log.info("{}/{}", methodName, id);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		Optional<User> user = usersRepository.findById(id);
		if(!user.isPresent()) {
			String detailed = String.format("%s/%s not found.", methodName, id);
			log.error(detailed);
			throw new ElementNotFoundException(detailed);
		}
		return user;
	}
	
	@GetMapping("/firstByName/{name}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Optional<User> findFirstByFirstname(@PathVariable @NotNull String name) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_USERS_FINDFIRSTBYFIRSTNAME;
		log.info("{}/{}", methodName, name);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		Optional<User> user = usersRepository.findFirstByFirstname(name);
		if(!user.isPresent()) {
			String detailed = String.format("%s/%s not found.", methodName, name);
			log.error(detailed);
			throw new ElementNotFoundException(detailed);
		}
		
		return user;
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Iterable<User> findAll() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_USERS_FINDALL;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		List<User> users = usersRepository.findAll();
		log.info("{} size {}", methodName, users.size());
		return users;
	}
	
	@GetMapping("/count")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CounterUsers count() {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_USERS_COUNT;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		CounterUsers count = new CounterUsers();
		count.setUsers(usersRepository.count());
		log.info("{} count {}", methodName, count);
		return count;
	}
	
	@GetMapping("/exists/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public boolean existsById(@PathVariable @NotNull String id) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_USERS_EXISTBYID;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		return usersRepository.existsById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public User save(@RequestBody @Valid User user) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_USERS_SAVE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		user.setPassword(encoders.makePasswordHash(user.getPassword()));
		user.setCreated(new Date());
		User userSaved = usersRepository.save(user);
		if(!userSaved.equals(user)) {
			String detailed = String.format("%s/%s not saved.", methodName, user.getId());
			log.error(detailed);
			throw new OperationNotCompletedException(detailed);
		}
		
		return user;
	}

	@PostMapping("/validate")
	//@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public void validate(@RequestBody @Valid Login login) throws LoginException {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_USERS_VALIDATE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		// validate user.
		Optional<User> user = usersRepository.findById(login.getId());
		if(! user.isPresent()) {
			String detailed = String.format("%s/%s user not found.", methodName, login.getId());
			log.error(detailed);
			throw new LoginException("User not found.", detailed);			
		}
		
		// validate pwd
		if(! encoders.validatePwd(user.get().getPassword(), login.getPassword())) {
			String detailed = String.format("%s/%s user not match.", methodName, login.getId());
			log.error(detailed);
			throw new LoginException("User not match.", detailed);			
		}
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestBody @NotNull User user) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_USERS_DELETE;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		usersRepository.delete(user);
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteById(@PathVariable @NotNull String id) {
		String methodName = MyExpensesConstants.ENDPOINTS.COUNTER_USERS_DELETEBYID;
		log.info("{}", methodName);
		actuatorMeterRegistry.incrementCounter(methodName);
		
		usersRepository.deleteById(id);
	}

}