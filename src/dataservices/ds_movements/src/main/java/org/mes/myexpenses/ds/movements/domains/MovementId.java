package org.mes.myexpenses.ds.movements.domains;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

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

	@Field("log")
	@JsonPropertyDescription("login")
	@NotEmpty @Size(min=3, max=20, message = "string size min=3, max=20") 
	private String login;

	// https://stackoverflow.com/questions/17754849/spring-3-2-date-time-format
	@Field("dat")
	@JsonPropertyDescription("date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yy")
	private Date date;
}
