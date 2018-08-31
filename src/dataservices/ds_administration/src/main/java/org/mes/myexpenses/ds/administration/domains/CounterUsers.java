package org.mes.myexpenses.ds.administration.domains;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounterUsers implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	private long users;
}
