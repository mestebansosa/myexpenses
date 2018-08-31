package org.mes.myexpenses.bs.movements.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.mes.myexpenses.bs.movements.domains.Category;
import org.mes.myexpenses.bs.movements.domains.CategoryId;
import org.mes.myexpenses.bs.movements.domains.CounterCategories;
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
public class CategoriesImpl implements Categories {
	// Dataservice Properties
	private String host;
	private String categoriesPath;
	private String mongoPath;
	private int port;

	private UriComponentsBuilder uriComponentsBuilderCategories;
	private UriComponentsBuilder uriComponentsBuilderMongo;

	private final RestTemplateClient restTemplateClient;

	public CategoriesImpl(RestTemplateClient restTemplateClient) {
		this.restTemplateClient = restTemplateClient;
	}

	@PostConstruct
	public void init() {
		log.info("AdministrationImpl [host={}, port={}, path={}]", host, port, categoriesPath);
		UriComponentsBuilder uriCB = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port);
		uriComponentsBuilderCategories = uriCB.cloneBuilder().path(categoriesPath);
		uriComponentsBuilderMongo = uriCB.cloneBuilder().path(categoriesPath).path(mongoPath);
	}


	@Override
	public Category findById(CategoryId categoryId) {
		UriComponentsBuilder uriCB = uriComponentsBuilderCategories.cloneBuilder();
		uriCB.path("/").path("findById");
		URI url = uriCB.build().toUri();

		log.info("existsById: Checking {} if exists user with {}", url.toASCIIString(), categoryId);
		return restTemplateClient.postForObject(url, Category.class, categoryId, "findById");
	}

	@Override
	public List<Category> findAll() {
		UriComponentsBuilder uriCB = uriComponentsBuilderCategories.cloneBuilder();
		URI url = uriCB.build().toUri();

		log.info("findAll: Get the List of Categories", url.toASCIIString());
		Category[] response = restTemplateClient.getForEntity(url, Category[].class, "findAll");
		return Arrays.asList(response);
	}

	@Override
	public CounterCategories count() {
		UriComponentsBuilder uriCB = uriComponentsBuilderCategories.cloneBuilder();
		uriCB.path("/").path("count");
		URI url = uriCB.build().toUri();

		CounterCategories count = restTemplateClient.getForEntity(url, CounterCategories.class, "count");
		log.info("count: {}, url: {}", count, url.toASCIIString());
		return count;
	}

	@Override
	public boolean existsById(CategoryId categoryId)  {
		UriComponentsBuilder uriCB = uriComponentsBuilderCategories.cloneBuilder();
		uriCB.path("/").path("existsById");
		URI url = uriCB.build().toUri();

		log.info("existsById: Checking {} if exists user with {}", url.toASCIIString(), categoryId);
		return restTemplateClient.postForObject(url, Boolean.class, categoryId, "existsById");
	}

	@Override
	public Category save(Category category) {
		UriComponentsBuilder uriCB = uriComponentsBuilderCategories.cloneBuilder();
		URI url = uriCB.build().toUri();

		log.info("save: Creating user with {}, {}", url.toASCIIString(), category);
		return restTemplateClient.postForObject(url, Category.class, category, "save");
	}

	@Override
	public void delete(Category category) {
		UriComponentsBuilder uriCB = uriComponentsBuilderCategories.cloneBuilder();
		URI url = uriCB.build().toUri();

		log.info("delete: {}/{}", url.toASCIIString(), category);
		restTemplateClient.exchange(url, Void.class, Category.class, HttpMethod.DELETE, category, "delete");
	}

	@Override
	public void deleteById(CategoryId categoryId) {
		UriComponentsBuilder uriCB = uriComponentsBuilderCategories.cloneBuilder();
		uriCB.path("/").path("deleteById");
		URI url = uriCB.build().toUri();

		log.info("deleteById: {}/{}", url.toASCIIString(), categoryId);
		restTemplateClient.exchange(url, Void.class, CategoryId.class, HttpMethod.DELETE, categoryId, "deleteById");
	}

}
