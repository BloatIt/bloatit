package com.bloatit.web.linkable.documentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.XmlText;
import com.bloatit.framework.webprocessor.components.renderer.HtmlMarkdownRenderer;

public class HtmlDocumentationRenderer extends PlaceHolderElement {
	private static final String DEFAULT_LANGUAGE = "en";

	/**
	 * <p>
	 * Store the html documents (after they've been converted from markdown)
	 * </p>
	 */
	private static Map<MarkdownDocumentationMarker, MarkdownDocumentationContent> cache = Collections
			.synchronizedMap((new HashMap<MarkdownDocumentationMarker, MarkdownDocumentationContent>()));
	private final boolean exist;

	public enum DocumentationType {
		FRAME("frame"), MAIN_DOC("main");

		private final String path;

		DocumentationType(final String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}

		@Override
        public String toString() {
			return getPath();
		}
	}

	public HtmlDocumentationRenderer(final DocumentationType type, final String key) {
		final String dir = FrameworkConfiguration.getDocumentationDir();
		final String language = Context.getLocalizator().getLanguageCode();

		String path = dir + "/" + type.getPath() + "/" + key + "_" + language;

		if (!load(path)) {
			if (!language.equals(DEFAULT_LANGUAGE)) {
				path = dir + "/" + type.getPath() + "/" + key + "_"
						+ DEFAULT_LANGUAGE;
				if (!load(path)) {
					exist = false;
					Context.getSession().notifyBad(
							Context.tr(
									"Documentation entry {0} doesn''t exist.",
									key));
					Log.web().warn(
							"Documentation file " + type + "/" + key
									+ " doesn't exist");
				} else {
					Log.web().warn(
							"Documentation file " + type + "/" + key
									+ " doesn't exist in language " + language);
					final String notify = Context
							.tr("Documentation file {0} doesn''t exist in language {1}, using english instead",
									key, language);
					Context.getSession().notifyBad(notify);
					exist = true;
				}
			} else {
				exist = false;
				Log.web().warn(
						"Documentation file " + type + "/" + key
								+ " doesn't exist");
				Context.getSession().notifyBad(
						Context.tr("Documentation entry {0} doesn''t exist.",
								key));
			}
		} else {
			exist = true;
		}
	}

	public boolean isExists() {
		return exist;
	}

	/**
	 * Loads the markdown file at <code>path</code>
	 * 
	 * @param path
	 *            the path of the file to load
	 * @return <i>true</i> if the file has been loaded, <i>false</i> otherwise
	 */
	private boolean load(final String path) {
		FileInputStream fis;
		try {
			final File targetFile = new File(path);

			if (!targetFile.exists()) {
				Log.framework().warn(
						"User tried to access doc file " + targetFile.getName()
								+ " but it doesn't exist.");
				return false;
			}
			fis = new FileInputStream(targetFile);

			final MarkdownDocumentationMarker mdm = new MarkdownDocumentationMarker(
					path);
			final MarkdownDocumentationContent mdc = cache.get(mdm);

			if (mdc == null
					|| mdc.savedDate
							.before(new Date(targetFile.lastModified()))) {
				// No content, or content has been saved before the file was
				// last modified
				Log.framework().trace(
						"Reading from the markdown documentation file " + path);
				final byte[] b = new byte[fis.available()];
				fis.read(b);
				final String markDownContent = new String(b);
				final HtmlMarkdownRenderer content = new HtmlMarkdownRenderer(
						markDownContent);
				cache.put(mdm, new MarkdownDocumentationContent(new Date(),
						content.getRendereredContent()));
				add(content);
			} else {
				Log.framework().trace(
						"Using cache for documentation file " + path);
				add(new XmlText(mdc.htmlString));
			}
			return true;

		} catch (final FileNotFoundException e) {
			// User asked a wrong documentation file
			Log.web().warn(
					"A user tries to access documentation file " + path
							+ " but file is not available.");
			return false;
		} catch (final IOException e) {
			throw new ExternalErrorException(
					"An error occured while parsing the documentation file "
							+ path, e);
		}
	}

	/**
	 * Nested class used as a key to cache parsed content
	 */
	private class MarkdownDocumentationMarker {
		public String path;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final MarkdownDocumentationMarker other = (MarkdownDocumentationMarker) obj;
			if (path == null) {
				if (other.path != null) {
					return false;
				}
			} else if (!path.equals(other.path)) {
				return false;
			}

			return true;
		}

		public MarkdownDocumentationMarker(final String path) {
			super();
			this.path = path;
		}
	}

	/**
	 * Nested class used as a MapEntry.value to cache parsed markdown content
	 */
	private class MarkdownDocumentationContent {
		public Date savedDate;
		public String htmlString;

		public MarkdownDocumentationContent(final Date savedDate,
				final String htmlString) {
			super();
			this.savedDate = savedDate;
			this.htmlString = htmlString;
		}
	}
}
