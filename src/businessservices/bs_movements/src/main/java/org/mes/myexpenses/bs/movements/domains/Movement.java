package org.mes.myexpenses.bs.movements.domains;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movement implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	private MovementId id;
	private List<Expense> expenses;
}
