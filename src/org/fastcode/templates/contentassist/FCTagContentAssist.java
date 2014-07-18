package org.fastcode.templates.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.fastcode.templates.velocity.contentassist.FastCodeKeywordsManager;
import org.fastcode.templates.velocity.rules.FastCodeKeywordDetector;
import org.fastcode.util.FastCodeUtil;

public class FCTagContentAssist extends AbstractTemplateContentAssistant {

	private static FastCodeKeywordDetector	tagDetector	= new FastCodeKeywordDetector();

	/**
	 * Instantiates a new fckeyword content assistant.
	 */

	public FCTagContentAssist() {
		super(new String[] { "<f", "</f" }, new char[] { 'f' });
	}

	/* (non-Javadoc)
	 * @see org.fastcode.templates.contentassist.AbstractTemplateContentAssistant#getCompletionProposals(org.eclipse.jface.text.IDocument, int, int)
	 */
	@Override
	public List<ICompletionProposal> getCompletionProposals(final IDocument document, final int offset, final int length, final String spaceToPad) {
		try {
			final String element = getElement(document, offset, length);
			if (element != null) {
				final String partition = FastCodeUtil.getPartition(document, offset);
				return FastCodeKeywordsManager.getCompletionProposals(partition, element, offset, length, spaceToPad);
			}
		} catch (final Exception e) {/* ignore */
		}

		return new ArrayList<ICompletionProposal>();
	}

	private String getElement(final IDocument document, final int offset, final int length) throws BadLocationException {
		if (length <= 0) {
			return null;
		}

		final String element = document.get(offset, length);
		if (element.startsWith("<f") && isValidElement(element) || element.startsWith("</f") && isValidElement(element)) {
			return element;
		}
		return null;
	}

	private boolean isValidElement(final String element) {
		final char[] chars = element.toCharArray();

		if (chars.length != 0 || tagDetector.isWordStart(chars[1])) {
			return true;
		}
		if (chars.length == 3) {
			if (tagDetector.isWordStart(chars[2])) {
				return true;
			}

		}

		for (int i = 1; i < chars.length; ++i) {
			if (!tagDetector.isWordPart(chars[i])) {
				return false;
			}
		}
		return false;

	}

}
