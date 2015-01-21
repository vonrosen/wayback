package org.archive.wayback.accesscontrol.oracleclient;

import java.util.Date;
import java.util.logging.Logger;

import org.archive.accesscontrol.AccessControlException;
import org.archive.accesscontrol.RegexRuleAccessControlClient;
import org.archive.accesscontrol.RobotsUnavailableException;
import org.archive.accesscontrol.RuleOracleUnavailableException;
import org.archive.accesscontrol.model.RegexRule;
import org.archive.util.ArchiveUtils;
import org.archive.wayback.accesscontrol.CollectionContext;
import org.archive.wayback.accesscontrol.oracleclient.CustomPolicyOracleFilter.Policy;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.replay.html.RuleRewriteDirector;
import org.archive.wayback.resourceindex.filters.ExclusionFilter;

public class RegexRuleOraclePolicyService extends OraclePolicyService implements RuleRewriteDirector {

	private static final Logger LOGGER = Logger.getLogger(RegexRuleOraclePolicyService.class.getName());
	
	protected void initializeClient() {
		client = new RegexRuleAccessControlClient(oracleUrl);
		if (proxyHostPort != null) {
			int colonIdx = proxyHostPort.indexOf(':');
			if (colonIdx > 0) {
				String host = proxyHostPort.substring(0, colonIdx);
				int port = Integer.valueOf(proxyHostPort
					.substring(colonIdx + 1));
				client.setRobotProxy(host, port);
			}
		}
	}	
	
	protected void initializeClient(String oracleUrl, String proxyHostPort) {
		client = new RegexRuleAccessControlClient(oracleUrl);
		if (proxyHostPort != null) {
			int colonIdx = proxyHostPort.indexOf(':');
			if (colonIdx > 0) {
				String host = proxyHostPort.substring(0, colonIdx);
				int port = Integer.valueOf(proxyHostPort
					.substring(colonIdx + 1));
				client.setRobotProxy(host, port);
			}
		}
	}
	
	protected RegexRule getRawRule(String accessGroup,
			CaptureSearchResult capture) throws RobotsUnavailableException,
			RuleOracleUnavailableException {

		String url = capture.getOriginalUrl();
		Date captureDate = capture.getCaptureDate();
		Date retrievalDate = new Date();
		
		return (RegexRule)client.getRule(
			ArchiveUtils.addImpliedHttpIfNecessary(url), captureDate,
			retrievalDate, accessGroup);
	}
	
	public RegexRule getRewriteDirectiveRule(CollectionContext context, CaptureSearchResult capture) {
		String accessGroup = context.getCollectionContextName();
		try {
			RegexRule rule = getRawRule(accessGroup, capture);
			// exclusion policies are not rewrite directives. map them to null.
			// (Danger: assumes Policy enum has exclusion values only).
			for (Policy handler : Policy.values()) {
				if (handler.matches(rule.getPolicy())) {
					return null;
				}
			}
			return rule;
		} catch (AccessControlException ex) {
			// TODO: If retrieval of rewrite directive fails due to an error in
			// underlining service, replay can suffer. It would be better to let
			// user know of this transient problem.
			LOGGER.warning(
				"Oracle Unavailable/not running, default to allow all until it responds. Details: " +
						ex.toString());
			return null;
		}
	}
	
	protected ExclusionFilter getExclusionFilter(String accessGroup) {
		return new CustomRegexPolicyOracleFilter(client, accessGroup);
	}
	
}
