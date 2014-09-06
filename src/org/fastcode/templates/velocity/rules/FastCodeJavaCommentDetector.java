package org.fastcode.templates.velocity.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class FastCodeJavaCommentDetector implements IWordDetector {

	/*
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	 */
	public boolean isWordStart(final char aChar) {
		return aChar == '/' ;
	}

	/*
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	 */
	public boolean isWordPart(final char aChar) {
		return Character.isLetterOrDigit(aChar) || aChar == '*' || aChar == '/';
	}
}
