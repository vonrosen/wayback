package org.archive.wayback.replay.html.rewrite;

import org.archive.accesscontrol.model.RegexReplacement;
import org.archive.accesscontrol.model.RegexRule;
import org.archive.wayback.replay.html.ReplayParseContext;
import org.archive.wayback.replay.html.transformer.RegexReplaceStringTransformer;

public class RegexRewritingStringTransformer extends RewritingStringTransformer {
	
	public String transform(ReplayParseContext rpContext, String input) {
		
		if (!rpContext.isInScriptText() && !rpContext.isInJS()) {
			return input;
		}
		
		RegexRule rule = rpContext.getRule();
		
		if (rule != null) {
			RegexReplaceStringTransformer transformer = 
					new RegexReplaceStringTransformer();
			
			for (RegexReplacement regex : rule.getRegexReplacements()) {
			
				transformer.setRegex(regex.getRegex());
				transformer.setReplacement(regex.getReplacement());
			
				input = transformer.rewrite(rpContext, rule.getPolicy(), input);
			}
		}
		
		return input;
	}
}
