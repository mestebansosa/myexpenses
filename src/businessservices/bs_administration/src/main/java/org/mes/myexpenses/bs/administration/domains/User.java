package org.mes.myexpenses.bs.administration.domains;

import java.io.Serializable;

import javax.validation.constraints.Email;
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
public class User  implements Serializable {
	private static final long serialVersionUID = -6743782939567855239L;
	
	// http://www.baeldung.com/javax-validation 
	// https://stackoverflow.com/questions/8756768/annotations-from-javax-validation-constraints-not-working
	@NotEmpty @Size(min=3, max=20, message = "string size min=3, max=20") 
	private String id;
	@NotEmpty @Size(min=2, max=50, message = "string size min=2, max=20") 
	private String firstname;
	@NotEmpty @Size(min=5, max=50, message = "string size min=5, max=50") 
	private String lastname;
	@NotEmpty @Size(min=3, max=10, message = "string size min=3, max=10") 
	private String password;
	@NotEmpty @Size(min=3, max=10, message = "string size min=3, max=10") 
	private String verify;
	@Email(message = "Email should be valid") 
	private String email;
	
	public boolean compareSignup(User signupResponse) {
		if (this.getId().equalsIgnoreCase(signupResponse.getId())
				&& this.getFirstname().equalsIgnoreCase(signupResponse.getFirstname())
				&& this.getLastname().equalsIgnoreCase(signupResponse.getLastname())
				&& this.getEmail().equalsIgnoreCase(signupResponse.getEmail())) {
			return true;
		}
		return false;
	}

}
