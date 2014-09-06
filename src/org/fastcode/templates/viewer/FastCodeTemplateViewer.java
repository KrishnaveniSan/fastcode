package org.fastcode.templates.viewer;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.hyperlink.IHyperlinkPresenter;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.fastcode.templates.rules.FastCodeTemplatePartitionScanner;
import org.fastcode.templates.rules.FastCodeTemplatePartitions;

/**
 * Viewer for fastcode templates.
 */
public class FastCodeTemplateViewer extends SourceViewer {

	/**
	 * Instantiates a new template viewer.
	 *
	 * @param parent the parent
	 * @param styles the styles
	 */
	public FastCodeTemplateViewer(final Composite parent, final int styles) {
		super(parent, null, styles);

		final Font font = JFaceResources.getFont(PreferenceConstants.EDITOR_TEXT_FONT);
		getTextWidget().setFont(font);
	}

	/*
	 * @see org.eclipse.jface.text.source.SourceViewer#setDocument(org.eclipse.jface.text.IDocument)
	 */
	@Override
	public void setDocument(final IDocument document) {

		if (document != null) {
			final Map<String, IDocumentPartitioner> partitioners = new HashMap<String, IDocumentPartitioner>();
			partitioners.put(FastCodeTemplatePartitions.TEMPLATE_PARTITIONING, new FastPartitioner(new FastCodeTemplatePartitionScanner(),
									new String[] {
											FastCodeTemplatePartitions.SINGLE_LINE_COMMENT,
											FastCodeTemplatePartitions.MULTI_LINE_COMMENT,
											FastCodeTemplatePartitions.FC_METHOD,
											FastCodeTemplatePartitions.FC_FIELD,
											FastCodeTemplatePartitions.FC_CLASS,
											FastCodeTemplatePartitions.FC_FILE,
											FastCodeTemplatePartitions.FC_PACKAGE,
											FastCodeTemplatePartitions.FC_FOLDER,
											FastCodeTemplatePartitions.FC_PROJECT,
											FastCodeTemplatePartitions.FC_MESSAGE,
											FastCodeTemplatePartitions.FC_EXIT,
											FastCodeTemplatePartitions.FC_IMPORT,
											FastCodeTemplatePartitions.FC_XML,
											FastCodeTemplatePartitions.FC_CLASSES,
											FastCodeTemplatePartitions.FC_FILES,
											FastCodeTemplatePartitions.FC_PROPERTY,
											FastCodeTemplatePartitions.FC_INFO,
											FastCodeTemplatePartitions.FC_SNIPPET,
											FastCodeTemplatePartitions.SINGLE_LINE_JAVA_COMMENT,
											FastCodeTemplatePartitions.MULTI_LINE_JAVA_COMMENT
											}));
			TextUtilities.addDocumentPartitioners(document, partitioners);
		}
		super.setDocument(document);
	}

	@Override
	public void setHyperlinkPresenter(final IHyperlinkPresenter hyperlinkPresenter) throws IllegalStateException {
		//		if (this.fHyperlinkManager != null) {
		//			throw new IllegalStateException();
		//		}

		this.fHyperlinkPresenter = hyperlinkPresenter;

	}
}
