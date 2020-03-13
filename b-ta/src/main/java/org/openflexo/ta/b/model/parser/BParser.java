/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Connie-core, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.ta.b.model.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.openflexo.p2pp.RawSource;
import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BModelFactory;
import org.openflexo.ta.b.model.BObject;
import org.openflexo.ta.b.parser.lexer.CustomLexer;
import org.openflexo.ta.b.parser.lexer.CustomLexer.EntryPointKind;
import org.openflexo.ta.b.parser.lexer.LexerException;
import org.openflexo.ta.b.parser.node.Start;
import org.openflexo.ta.b.parser.parser.Parser;
import org.openflexo.ta.b.parser.parser.ParserException;

/**
 * This class provides the parsing service for B.<br>
 * This includes syntactic and semantics analyzer.<br>
 * 
 * SableCC is used to generate the grammar located in b-ta-parser.<br>
 * 
 * @author sylvain
 */
public class BParser {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BParser.class.getPackage().getName());

	/**
	 * This is the method to invoke to perform a parsing.<br>
	 * Syntactic and semantics analyzer are performed and returned value is a {@link BComponent}
	 * 
	 * @param data
	 *            data to parse
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static BObject parse(String data, BModelFactory modelFactory, EntryPointKind entryPointKind) throws ParseException, IOException {
		return parse(new StringReader(data), new StringReader(data), modelFactory, entryPointKind);
	}

	/**
	 * This is the method to invoke to perform a parsing.<br>
	 * Syntactic and semantics analyzer are performed and returned value is a {@link BComponent}
	 * 
	 * @param data
	 *            data to parse
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static BComponent parse(String data, BModelFactory modelFactory) throws ParseException, IOException {
		return (BComponent) parse(new StringReader(data), new StringReader(data), modelFactory, EntryPointKind.CompilationUnit);
	}

	/**
	 * This is the method to invoke to perform a parsing.<br>
	 * Syntactic and semantics analyzer are performed and returned value is a {@link BComponent}
	 * 
	 * @param inputStream
	 *            source stream
	 * @return
	 * @throws ParseException
	 *             if parsing expression lead to an error
	 */
	public static BObject parse(InputStream inputStream, BModelFactory modelFactory, EntryPointKind entryPointKind)
			throws ParseException, IOException {

		// InputStream rawSourceInputStream = IOUtils.toBufferedInputStream(inputStream);
		// inputStream.reset();

		byte[] buf = IOUtils.toByteArray(inputStream);
		InputStream inputStream1 = new ByteArrayInputStream(buf);
		InputStream inputStream2 = new ByteArrayInputStream(buf);

		return parse(new InputStreamReader(inputStream1), new InputStreamReader(inputStream2), modelFactory, entryPointKind);
	}

	/**
	 * This is the method to invoke to perform a parsing.<br>
	 * Syntactic and semantics analyzer are performed and returned value is a {@link BComponent}
	 * 
	 * @param inputStream
	 *            source stream
	 * @return
	 * @throws ParseException
	 *             if parsing expression lead to an error
	 */
	public static BComponent parse(InputStream inputStream, BModelFactory modelFactory) throws ParseException, IOException {

		// InputStream rawSourceInputStream = IOUtils.toBufferedInputStream(inputStream);
		// inputStream.reset();

		byte[] buf = IOUtils.toByteArray(inputStream);
		InputStream inputStream1 = new ByteArrayInputStream(buf);
		InputStream inputStream2 = new ByteArrayInputStream(buf);

		return (BComponent) parse(new InputStreamReader(inputStream1), new InputStreamReader(inputStream2), modelFactory,
				EntryPointKind.CompilationUnit);
	}

	private static BObject parse(Reader reader, Reader rawSourceReader, BModelFactory modelFactory, EntryPointKind entryPointKind)
			throws ParseException, IOException {
		try {
			// System.out.println("Parsing: " + anExpression);

			RawSource rawSource = readRawSource(rawSourceReader);

			int rowTranslation = 0;
			int charTranslation = 0;

			// We handle here an issue with B grammar that does not support comments at the beginning of the source file
			// To overpass here, workaround is to detect preliminary comment, and then removing it from the parsed source,
			// while not forgetting translation indexes, in order to reconsider these primilary fragments while
			// re pretty-printing B source
			if (rawSource.size() > 0 && rawSource.getRow(0).trim().startsWith("/*")) {
				int endRow = -1;
				int endChar = -1;
				while (endChar == -1) {
					endRow++;
					endChar = rawSource.getRow(endRow).lastIndexOf("*/");
				}
				// System.out.println("Found comment ending at " + endRow + " et " + endChar);
				// System.out.println("Source: [" + rawSource
				// .makeFragment(rawSource.getStartPosition(), rawSource.makePositionAfterChar(endRow + 1, endChar + 2)).getRawText()
				// + "]");

				RawSourceFragment withoutHeaderComment = rawSource
						.makeFragment(rawSource.makePositionAfterChar(endRow + 1, endChar + 2).increment(), rawSource.getEndPosition());
				reader = new StringReader(withoutHeaderComment.getRawText());

				rowTranslation = endRow + 1;
				charTranslation = endChar + 2;
			}

			// Create a Parser instance.
			Parser p = new Parser(new CustomLexer(new PushbackReader(reader), entryPointKind));

			// Parse the input.
			Start tree;
			tree = p.parse();

			// Apply the semantics analyzer.
			BSemanticsAnalyzer t = new BSemanticsAnalyzer(modelFactory, tree, rawSource, rowTranslation, charTranslation);
			// tree.apply(t);

			return t.getBObject();
		} catch (ParserException e) {
			e.printStackTrace();
			throw new ParseException(e.getMessage() + " while parsing " + reader);
		} catch (LexerException e) {
			throw new ParseException(e.getMessage() + " while parsing " + reader);
		}
	}

	/**
	 * Read raw source of the file
	 * 
	 * @param ioDelegate
	 * @throws IOException
	 */
	private static RawSource readRawSource(Reader reader) throws IOException {
		return new RawSource(reader);
	}

}
