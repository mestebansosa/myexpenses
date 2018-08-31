package org.mes.myexpenses.commons.rest.config;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "rest.template", name = "enabled", havingValue = "true")
public class RestTemplateClient {

	public final RestTemplate restTemplate;
	public RestTemplateClient(@Qualifier("custom-rest-template") RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@PostConstruct
	public void init() {
		log.info("Initializing business Service: RestTemplateClient");
	}
	
	public <T> T getForEntity(URI url, Class<T> clazz, String methodName) {
		ResponseEntity<T> responseEntity = null;
		try {			
			log.debug("getForEntity:{} url:{}", methodName, url);			
			responseEntity = restTemplate.getForEntity(url, clazz);
			checkStatusCode(responseEntity);
			log.info("getForEntity:{} data found:{}", methodName, responseEntity);
		} catch (Exception e) {
			log.error("getForEntity:{} exception:{} ", methodName, e.getMessage());
			throw e;
		}
		return responseEntity.getBody();

	}

	/**
	 * Return type is T, and payload type is String
	 * @param url
	 * @param clazz
	 * @param payload
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public <T> T postForEntity(URI url, Class<T> clazz, String payload, String methodName) {
		ResponseEntity<T> responseEntity = null;
		try {
			log.debug("postForEntity:{} url:{}", methodName, url);
			responseEntity = restTemplate.postForEntity(url, payload, clazz);
			checkStatusCode(responseEntity);
			log.info("postForEntity:{} data found:{}", methodName, responseEntity);
		} catch (Exception e) {
			log.error("postForEntity:{} exception:{} ", methodName, e.getMessage());
			throw e;
		}
		return responseEntity.getBody();
	}
	
	/**
	 * Return type is equal to payload type
	 */
/*	public <T> T postForObject2(URI url, Class<T> clazz, T payload, String methodName) throws Exception {
		T response = null;
		try {
			log.debug("postForObject2:{} url:{}", methodName, url);
			response = restTemplate.postForObject(url, payload, clazz);
			log.info("postForObject2:{} data found:{}", methodName, response);
		} catch (Exception e) {
			log.error("postForObject2:{} exception:{} ", methodName, e.getMessage());
			throw e;
		}
		return response;
	}
*/	
	/**
	 * Return type is Not equal to payload type. It is valid to use Object as the
	 * type, instead of using a new U type if the return and payload types were
	 * differents.
	 * 
	 * @param url
	 * @param clazz
	 * @param payload
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public <T> T postForObject(URI url, Class<T> clazz, Object payload, String methodName)  {
		T response = null;
		try {
			log.debug("postForObject:{} url:{}", methodName, url);
			response = restTemplate.postForObject(url, payload, clazz);
			log.info("postForObject:{} data found:{}", methodName, response);
		} catch (Exception e) {
			log.error("postForObject:{} exception:{} ", methodName, e.getMessage());
			throw e;
		}
		return response;
	}
	
	public <T> T put(URI url, Class<T> clazz, T payload, String methodName) {
		T response = null;
		try {
			log.debug("put:{} url:{}", methodName, url);
			restTemplate.put(url, payload);
			log.info("put:{} data created.", methodName);
		} catch (Exception e) {
			log.error("put:{} exception:{} ", methodName, e.getMessage());
			throw e;
		}
		return response;
	}

	/**
	 * Two types: one for the return type, the other for the payload type.
	 * Mostly used for DELETE, but this method should be valid for all the types of HttpMethod
	 * @param url
	 * @param responseType
	 * @param payloadType
	 * @param httpMethod
	 * @param payload
	 * @param methodName, just for log traces
	 * @return
	 * @throws Exception
	 */
	public <T,U> T exchange(URI url, Class<T> responseType, Class<U> payloadType, HttpMethod httpMethod, U payload, String methodName) {
		ResponseEntity<T> responseEntity = null;
		try {
			log.info("exchange:{} url:{} payload:{}", methodName, url, payload);
			HttpEntity<U> entity = new HttpEntity<>(payload);
			responseEntity = restTemplate.exchange(url, httpMethod, entity, responseType); 
			checkStatusCode(responseEntity);
			log.info("exchange:{} data sent:{}", methodName, responseEntity);
		} catch (Exception e) {
			log.error("exchange:{} exception:{} ", methodName, e.getMessage());
			throw e;
		}
		return responseEntity.getBody();
	}

	/**
	 * Just in case it is needed to set some headers in the http entity
	 * @param url
	 * @param responseType
	 * @param payloadType
	 * @param httpMethod
	 * @param payload
	 * @param xForwardedHost
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public <T,U> T exchange(URI url, Class<T> responseType, Class<U> payloadType, HttpMethod httpMethod, U payload, String xForwardedHost, String methodName) {
		ResponseEntity<T> responseEntity = null;
		try {
			log.debug("exchange:{} url:{} payload:{}", methodName, url, payload);
			HttpEntity<U> entity = createHttpEntityWithHeader(payload, xForwardedHost);
			responseEntity = restTemplate.exchange(url, httpMethod, entity, responseType); 
			checkStatusCode(responseEntity);
			log.info("exchange:{} data sent:{}", methodName, responseEntity);
		} catch (Exception e) {
			log.error("exchange:{} exception:{} ", methodName, e.getMessage());
			throw e;
		}
		return responseEntity.getBody();
	}


/*	public <T> T exchange2(URI url, Class<T> clazz, HttpMethod httpMethod, Object payload, String xForwardedHost, String methodName) throws Exception {
		ResponseEntity<T> responseEntity = null;
		try {
			log.info("exchange {} url {} payload {}", methodName, url, payload);
			HttpEntity<Object> entity = createHttpEntityWithHeader(payload, xForwardedHost);
			responseEntity = restTemplate.exchange(url, httpMethod, entity, clazz); 
			checkStatusCode(responseEntity);
			log.info("exchange {} data sent {}", methodName, responseEntity);
		} catch (HttpClientErrorException h) {
			log.warn("exchange {} status {} exception {} ", methodName, h.getStatusCode(), h.getMessage());
			throw new Exception("exchange " + httpMethod.name() + ". Error:" +  h.getMessage());
		} catch (Exception e) {
			log.error("exchange {} exception {} ", methodName, e.getMessage());
			throw new Exception(httpMethod.name() + ". Error:" +  e.getMessage());
		}
		return responseEntity.getBody();
	}
*/
/*	public <T> T exchange2(URI url, Class<T> clazz, HttpMethod httpMethod, String payload, String methodName) throws Exception {
		return exchange2(url, clazz, httpMethod, payload, "", methodName);
	}
	
*/
	public void checkStatusCode(ResponseEntity<?> responseEntity) {
		if (responseEntity.getStatusCode().value() != HttpStatus.OK.value()) {
			log.info("HTTP status code: " + responseEntity.getStatusCode());
		}
	}

	public <T> HttpEntity<T> createHttpEntityWithHeader(T payload, String xForwardedHost) {
		if(xForwardedHost == null || xForwardedHost == "") {
			return new HttpEntity<T>(payload);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Forwarded-Host", xForwardedHost);
		return new HttpEntity<T>(payload, headers);
	}
	
}
