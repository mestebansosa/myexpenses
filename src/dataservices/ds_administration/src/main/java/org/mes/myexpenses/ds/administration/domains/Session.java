package org.mes.myexpenses.ds.administration.domains;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
@Document(collection = "sessions")
public class Session implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	@Id
	@Indexed
	@Field("_id")
	@JsonPropertyDescription("id = sessionId")
	private String id;

	@Field("use")
	@JsonPropertyDescription("user")
	@NotEmpty @Size(min=3, max=20, message = "string size min=3, max=20") 
	private String user;

	@Field("sta")
	@JsonPropertyDescription("date start")
	private Date start;

	@Field("end")
	@JsonPropertyDescription("date end")
	private Date end;
}
