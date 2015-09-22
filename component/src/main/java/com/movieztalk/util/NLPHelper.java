package com.movieztalk.util;

import java.io.IOException;
import java.io.InputStream;

import com.movieztalk.cosinesimilarity.ReviewsSimilarity;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class NLPHelper {

	private static Tokenizer tokenizerInstance = null;

	public static Tokenizer getOpenNLPTextTokenizer() {
		if (tokenizerInstance == null) {
			try {
				InputStream modelIn = NLPHelper.class
						.getResourceAsStream("/en-token.bin");
				TokenizerModel model = new TokenizerModel(modelIn);
				tokenizerInstance = new TokenizerME(model);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return tokenizerInstance;
	}
}
