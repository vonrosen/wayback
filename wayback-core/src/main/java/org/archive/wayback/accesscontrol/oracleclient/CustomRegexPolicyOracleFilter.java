package org.archive.wayback.accesscontrol.oracleclient;

import java.util.Date;
import java.util.logging.Logger;

import org.archive.accesscontrol.RobotsUnavailableException;
import org.archive.accesscontrol.RuleOracleUnavailableException;
import org.archive.accesscontrol.model.RegexRule;
import org.archive.util.ArchiveUtils;
import org.archive.wayback.core.CaptureSearchResult;

public class CustomRegexPolicyOracleFilter extends CustomPolicyOracleFilter {
	
	private static final Logger LOGGER = Logger
			.getLogger(CustomRegexPolicyOracleFilter.class.getName());
	
	public CustomRegexPolicyOracleFilter(String oracleUrl, String accessGroup,
			String proxyHostPort) {
		super(oracleUrl, accessGroup, proxyHostPort);
	}
	
	@Override
	public int filterObject(CaptureSearchResult o) {
		String url = o.getOriginalUrl();
		Date captureDate = o.getCaptureDate();
		Date retrievalDate = new Date();

		RegexRule rule;
		try {
			rule = client.getPolicyRegexRule(
				ArchiveUtils.addImpliedHttpIfNecessary(url), captureDate,
				retrievalDate, accessGroup);

			o.setRule(rule);

			if (rule == null) {
				return defaultFilter;
			}
			for (Policy handler : Policy.values()) {
				if (handler.matches(rule.getPolicy())) {
					return handler.apply(o, this);
				}
			}
			
			return 1;
			// unhandled policy is okay. it's just passed to upper-level
			// through CaptureSearchResult#oraclePolicy.
		} catch (RobotsUnavailableException e) {
			e.printStackTrace();
		} catch (RuleOracleUnavailableException e) {
			LOGGER.warning(
				"Oracle Unavailable/not running, default to allow all until it responds. Details: " +
						e.toString());
		}

		return defaultFilter;
	}
	

}
