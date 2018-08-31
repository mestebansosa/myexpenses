package org.mes.myexpenses.bs.movements.domains;

import java.io.Serializable;

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

	private CategoryId id;
	private String description;
	private Float quantity;
	
	public int compareTo(Expense expense) {
		return this.id.compareTo(expense.getId());
	}

}
