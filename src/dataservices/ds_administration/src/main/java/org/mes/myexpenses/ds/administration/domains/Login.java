package org.mes.myexpenses.ds.administration.domains;

import java.io.Serializable;

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
public class Login  implements Serializable {
	private static final long serialVersionUID = -6743782939567855239L;
	
	// http://www.baeldung.com/javax-validation 
	// https://stackoverflow.com/questions/8756768/annotations-from-javax-validation-constraints-not-working
	@NotEmpty @Size(min=3, max=20, message = "string size min=3, max=20") 
	private String id;
	@NotEmpty @Size(min=3, max=10, message = "string size min=3, max=10") 
	private String password;
	
}
