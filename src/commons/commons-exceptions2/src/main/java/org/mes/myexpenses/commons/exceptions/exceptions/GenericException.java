package org.mes.myexpenses.commons.exceptions.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({ "cause", "stackTrace", "localizedMessage", "suppressed" })
public class GenericException extends RuntimeException {
	private static final long serialVersionUID = 3337189982362779332L;
	@Default
	private Integer code = 0;
	@Default
	private String message = "";
	@Default
	private String detailed = "";
}
