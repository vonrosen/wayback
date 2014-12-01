package org.archive.wayback.archivalurl;

import org.archive.accesscontrol.model.RegexRule;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.replay.HttpHeaderProcessor;
import org.archive.wayback.util.htmllex.ParseContext;

public class ArchivalURLJSRuleStringTransformerReplayRenderer extends
		ArchivalURLJSStringTransformerReplayRenderer {

	public ArchivalURLJSRuleStringTransformerReplayRenderer(
			HttpHeaderProcessor httpHeaderProcessor) {
		super(httpHeaderProcessor);
	}

	protected void handleOraclePolicy(ParseContext context, CaptureSearchResult result) {
		RegexRule rule = result.getRule();
		
		if (rule != null) {
			context.setRule(rule);
		}		
	}
}
