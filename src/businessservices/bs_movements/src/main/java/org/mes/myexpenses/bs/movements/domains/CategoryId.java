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
public class CategoryId implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	private String category;
	private String expenseName;
	
	public int compareTo(CategoryId categoryId) {
		return this.category.compareTo(categoryId.getCategory());
	}

}
