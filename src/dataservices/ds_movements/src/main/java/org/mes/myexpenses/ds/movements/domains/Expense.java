package org.mes.myexpenses.ds.movements.domains;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
public class Expense implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	@Id
	@Indexed
	@Field("_id")
	@JsonPropertyDescription("id = category + expenseName")
	private CategoryId id;

	@Field("des")
	@JsonPropertyDescription("brief description")
	private String description;

	// TODO Use of Decimal128
	@Field("qty")
	@JsonPropertyDescription("quantity")
	private Float quantity;

}
