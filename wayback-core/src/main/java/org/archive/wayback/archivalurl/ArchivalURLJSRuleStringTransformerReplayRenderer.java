package org.archive.wayback.archivalurl;

import org.archive.accesscontrol.model.RegexRule;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.replay.HttpHeaderProcessor;
import org.archive.wayback.util.htmllex.ParseContext;
import org.archive.wayback.webapp.AccessPoint;

public class ArchivalURLJSRuleStringTransformerReplayRenderer extends
		ArchivalURLJSStringTransformerReplayRenderer {

	public ArchivalURLJSRuleStringTransformerReplayRenderer(
			HttpHeaderProcessor httpHeaderProcessor) {
		super(httpHeaderProcessor);
	}	
	
	protected void handleOraclePolicy(ParseContext context, WaybackRequest wbRequest, CaptureSearchResult result) {
		RegexRule rule = result.getOracleRegexRule();
		
		if (rule == null) {
			AccessPoint accessPoint = wbRequest.getAccessPoint();
			if (accessPoint != null) {
				rule = accessPoint.getRewriteDirectiveRule(result);
			}
		}
		
		if (rule != null) {
			context.setRule(rule);
		}		
	}
}
