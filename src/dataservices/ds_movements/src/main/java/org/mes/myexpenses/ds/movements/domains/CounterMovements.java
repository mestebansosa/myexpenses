package org.mes.myexpenses.ds.movements.domains;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounterMovements implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	private long movements;
	private long expenses;
}
