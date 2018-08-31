package org.mes.myexpenses.ds.administration.domains;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@Document(collection = "users")
public class User implements Serializable {
	private static final long serialVersionUID = 3987853255185793725L;

	@Id
	@Indexed
	@Field("_id")
	@JsonPropertyDescription("id = login")
	@NotEmpty @Size(min=3, max=20, message = "string size min=3, max=20") 
	private String id;

	@Field("fna")
	@NotNull
	@JsonPropertyDescription("user first name") // firstName
	@NotEmpty @Size(min=2, max=50, message = "string size min=2, max=20") 
	private String firstname;

	@Field("lna")
	@NotEmpty
	@JsonPropertyDescription("user last name") // lastName
	@NotEmpty @Size(min=5, max=50, message = "string size min=5, max=50") 
	private String lastname;

	@Field("ema")
	@Email
	@JsonPropertyDescription("user email")
	@Email(message = "Email should be valid") 
	private String email;

	@Field("pwd")
	@NotNull
	@JsonPropertyDescription("user password")
	@NotEmpty @Size(min=3, max=10, message = "string size min=3, max=10") 
	private String password;
	
	@Field("cre")
	@JsonPropertyDescription("date created")
	private Date created;
}
