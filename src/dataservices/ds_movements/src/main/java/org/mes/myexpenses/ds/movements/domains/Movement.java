package org.mes.myexpenses.ds.movements.domains;

import java.io.Serializable;
import java.util.List;

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
@Document(collection = "movements")
public class Movement implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	@Id
	@Indexed
	@Field("_id")
	private MovementId id;

	@Field("lex")
	@JsonPropertyDescription("List of expenses")
	private List<Expense> expenses;
}
