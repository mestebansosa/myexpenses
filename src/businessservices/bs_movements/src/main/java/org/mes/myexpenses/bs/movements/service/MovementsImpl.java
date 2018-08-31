package org.mes.myexpenses.bs.movements.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.mes.myexpenses.bs.movements.domains.CounterMovements;
import org.mes.myexpenses.bs.movements.domains.Movement;
import org.mes.myexpenses.bs.movements.domains.MovementId;
import org.mes.myexpenses.commons.rest.config.RestTemplateClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

//TODO. Try to do it with redirect (GET) or forward (POST,DELETE,PUT)
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "myexpenses.dataservices.movements")
@Service
@Data
public class MovementsImpl implements Movements {
	// Dataservice Properties
	private String host;
	private String movementsPath;
	private String mongoPath;
	private int port;

	private UriComponentsBuilder uriComponentsBuilderMovements;
	private UriComponentsBuilder uriComponentsBuilderMongo;

	private final RestTemplateClient restTemplateClient;

	public MovementsImpl(RestTemplateClient restTemplateClient) {
		this.restTemplateClient = restTemplateClient;		
	}

	@PostConstruct
	public void init() {
		log.info("AdministrationImpl [host={}, port={}, path={}]", host, port, movementsPath);
		UriComponentsBuilder uriCB = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port);
		uriComponentsBuilderMovements = uriCB.cloneBuilder().path(movementsPath);
		uriComponentsBuilderMongo = uriCB.cloneBuilder().path(movementsPath).path(mongoPath);
	}

	@Override
	public Movement save(Movement movement) {
		UriComponentsBuilder uriCB = uriComponentsBuilderMovements.cloneBuilder();
		URI url = uriCB.build().toUri();

		log.info("save: Creating movement with {}, {}", url.toASCIIString(), movement);
		return restTemplateClient.postForObject(url, Movement.class, movement, "save");
	}

	@Override
	public Movement findById(MovementId movementId) {
		UriComponentsBuilder uriCB = uriComponentsBuilderMovements.cloneBuilder();
		uriCB.path("/").path("findById");
		URI url = uriCB.build().toUri();

		log.info("findById: {}/{}", url.toASCIIString(), movementId);
		return restTemplateClient.postForObject(url, Movement.class, movementId, "findById");
	}

	@Override
	public List<Movement> findAll() {
		UriComponentsBuilder uriCB = uriComponentsBuilderMovements.cloneBuilder();
		URI url = uriCB.build().toUri();

		log.info("findAll: Get the List of Movements", url.toASCIIString());
		Movement[] response = restTemplateClient.getForEntity(url, Movement[].class, "findAll");
		return Arrays.asList(response);
	}

	@Override
	public CounterMovements count() {
		UriComponentsBuilder uriCB = uriComponentsBuilderMovements.cloneBuilder();
		uriCB.path("/").path("count");
		URI url = uriCB.build().toUri();

		CounterMovements count = restTemplateClient.getForEntity(url, CounterMovements.class, "count");
		log.info("count: {}, url: {}", count, url.toASCIIString());
		return count;
	}

	@Override
	public boolean existsById(MovementId movementId) {
		UriComponentsBuilder uriCB = uriComponentsBuilderMovements.cloneBuilder();
		uriCB.path("/").path("existsById");
		URI url = uriCB.build().toUri();

		log.info("existsById: Checking {} if exists user with {}", url.toASCIIString(), movementId);
		return restTemplateClient.postForObject(url, Boolean.class, movementId, "existsById");
	}

	@Override
	public void delete(Movement movement) {
		UriComponentsBuilder uriCB = uriComponentsBuilderMovements.cloneBuilder();
		URI url = uriCB.build().toUri();

		log.info("delete: {}/{}", url.toASCIIString(), movement);
		restTemplateClient.exchange(url, Void.class, Movement.class, HttpMethod.DELETE, movement, "delete");
	}

	@Override
	public void deleteById(MovementId movementId) {
		UriComponentsBuilder uriCB = uriComponentsBuilderMovements.cloneBuilder();
		uriCB.path("/").path("deleteById");
		URI url = uriCB.build().toUri();

		log.info("delete: {}/{}", url.toASCIIString(), movementId);
		restTemplateClient.exchange(url, Void.class, MovementId.class, HttpMethod.DELETE, movementId, "delete");
	}

}
