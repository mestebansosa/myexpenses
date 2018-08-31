package org.mes.myexpenses.commons.rest.accesslog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Slf4jRequestLog;
import org.eclipse.jetty.util.annotation.ManagedObject;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ManagedObject("Custom NCSA format")
public class CustomRequestLog extends Slf4jRequestLog {
	@Default
	private boolean isExtended = false;
	@Default
	private boolean isLatency = false;
	@Default
	private boolean isOrigin = false;
	@Default
	private boolean isCookies = false;
	@Default
	private boolean isServer = false;
	@Default
	private boolean isProxiedForAddressPrefered = false;
	@Default
	private String logName = "org.eclipse.jetty.server.RequestLog";
	@Default
	private String dateFormat = "dd/MMM/yyyy:HH:mm:ss Z";
	@Default
	private Locale locale = Locale.getDefault();
	@Default
	private String timeZone = "GMT";
	@Default
	private List<String> ignoredPaths = null;
	@Default
	private boolean isCustomField = false;
	@Default
	private String customFieldName = "";

	public CustomRequestLog(boolean isExtended, boolean isLatency, boolean isOrigin, boolean isCookies,
			boolean isServer, boolean isProxiedForAddressPrefered, String logName, String dateFormat, Locale locale,
			String timeZone, List<String> ignoredPaths, boolean isCustomField, String customFieldName) {
		super();
		this.isOrigin = isOrigin;
		this.isCustomField = isCustomField;
		this.customFieldName = customFieldName;

		// set properties
		super.setLogLatency(isLatency);
		super.setLogCookies(isCookies);
		super.setLogServer(isServer);
		super.setPreferProxiedForAddress(isProxiedForAddressPrefered);
		super.setLoggerName(logName);
		super.setLogDateFormat(dateFormat);
		super.setLogLocale(locale);
		super.setLogTimeZone(timeZone);

		if (ignoredPaths != null && !ignoredPaths.isEmpty()) {
			super.setIgnorePaths((String[]) ignoredPaths.toArray());
		}

		super.setExtended(requireEnableExtensions());
	}

	private final boolean requireEnableExtensions() {
		return (this.isOrigin() || this.isCustomField() || this.isExtended()) ? true : false;
	}

	@Override
	protected void logExtended(StringBuilder b, Request request, Response response) throws IOException {
		if (this.isExtended()) {
			super.logExtended(b, request, response);
		}

		if (this.isOrigin()) {
			final String crossOrigin = request.getHeader(HttpHeader.ORIGIN.toString());
			if (crossOrigin == null)
				b.append("\"-\"");
			else {
				b.append('"');
				b.append(crossOrigin);
				b.append('"');
			}

			if (this.isCustomField()) {
				b.append(" ");
			}
		}

		if (this.isCustomField()) {
			final String customFieldValue = (String) request.getAttribute(this.getCustomFieldName());
			if (customFieldValue == null)
				b.append("-");
			else {
				b.append(customFieldValue);
			}
		}
	}

}
