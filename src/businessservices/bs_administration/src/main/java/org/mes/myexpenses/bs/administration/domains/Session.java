package org.mes.myexpenses.bs.administration.domains;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	private String id;
	@NotEmpty @Size(min=3, max=20, message = "string size min=3, max=20") 
	private String user;
	private Date start;
	private Date end;
}
