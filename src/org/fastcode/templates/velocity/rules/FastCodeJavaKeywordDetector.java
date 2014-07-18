package org.fastcode.templates.velocity.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class FastCodeJavaKeywordDetector implements IWordDetector {

	/*
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	public boolean isWordStart(final char aChar) {
		return true;
		//return aChar == 'f' || aChar == 'b' || aChar == 'h' || aChar == 'j';
	}

	/*
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	public boolean isWordPart(final char aChar) {
		return true;
		//return Character.isLetterOrDigit(aChar) || aChar == '<' || aChar == ':';
	}
}
