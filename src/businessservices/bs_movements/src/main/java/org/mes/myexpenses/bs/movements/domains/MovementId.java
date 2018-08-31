package org.mes.myexpenses.bs.movements.domains;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovementId implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	@NotEmpty @Size(min=3, max=20, message = "string size min=3, max=20") 
	private String login;

	// https://stackoverflow.com/questions/17754849/spring-3-2-date-time-format
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yy")
	private Date date;
}
