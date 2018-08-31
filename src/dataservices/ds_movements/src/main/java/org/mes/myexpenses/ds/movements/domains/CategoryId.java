package org.mes.myexpenses.ds.movements.domains;

import java.io.Serializable;

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
public class CategoryId implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	@Field("cat")
	@JsonPropertyDescription("category")
	private String category;

	@Field("ena")
	@JsonPropertyDescription("expense name")
	private String expenseName;
}
