package org.archive.wayback.replay.html;

import org.archive.accesscontrol.model.RegexRule;
import org.archive.wayback.accesscontrol.CollectionContext;
import org.archive.wayback.core.CaptureSearchResult;

public interface RuleRewriteDirector {
	
	public RegexRule getRewriteDirectiveRule(CollectionContext context, CaptureSearchResult capture);
}
