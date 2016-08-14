package com.movieztalk.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class NLPHelper {

	private static Tokenizer tokenizerInstance = null;

	public static Tokenizer getOpenNLPTextTokenizer() throws InvalidFormatException, IOException {
		if (tokenizerInstance == null) {
			InputStream is = new FileInputStream("modelfiles/en-token.bin");
			TokenizerModel model = new TokenizerModel(is);
			tokenizerInstance = new TokenizerME(model);
		}
		return tokenizerInstance;
	}
}
