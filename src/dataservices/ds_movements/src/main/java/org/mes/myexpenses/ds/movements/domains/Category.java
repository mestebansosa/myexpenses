package org.mes.myexpenses.ds.movements.domains;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
public class Category implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	@Id
	@Indexed
	@Field("_id")
	@JsonPropertyDescription("id = category + expenseName")
	private CategoryId id;

	@Field("day")
	@JsonPropertyDescription("expense maximun per day")
	private Integer maxPerDay;

	@Field("mon")
	@JsonPropertyDescription("expense maximun per month")
	private Integer maxPerMonth;

	@Field("yea")
	@JsonPropertyDescription("expense maximun per year")
	private Integer maxPerYear;

	@Field("fix")
	@JsonPropertyDescription("expense type: fixed or variable")
	private Boolean fixed;
	
}
