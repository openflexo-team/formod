package org.openflexo.ta.b.parser.lexer;

import java.io.IOException;
import java.io.PushbackReader;

import org.openflexo.ta.b.parser.node.Token;

public class CustomLexer extends Lexer {

	public enum EntryPointKind {
		CompilationUnit {
			@Override
			public Token getToken(Lexer l) {
				return l.new0("", 0, 0);
			}

			@Override
			public State getState(Lexer l) {
				return State.SECTION;
			}
		},
		Predicate {
			@Override
			public Token getToken(Lexer l) {
				return l.new1("", 0, 0);
			}

			@Override
			public State getState(Lexer l) {
				return State.NORMAL;
			}
		},
		Expression {
			@Override
			public Token getToken(Lexer l) {
				return l.new2("", 0, 0);
			}

			@Override
			public State getState(Lexer l) {
				return State.NORMAL;
			}
		};
		public abstract Token getToken(Lexer l);

		public abstract State getState(Lexer l);
	}

	private boolean started = false;
	private EntryPointKind entryPoint;

	public CustomLexer(PushbackReader in, EntryPointKind ep) {
		super(in);
		this.entryPoint = ep;
	}

	@Override
	public Token getToken() throws IOException, LexerException {
		if (!started) {
			this.started = true;
			this.state = this.entryPoint.getState(this);
			return this.entryPoint.getToken(this);
		}
		return super.getToken();
	}
}
