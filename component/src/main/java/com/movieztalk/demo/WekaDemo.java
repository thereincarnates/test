package com.movieztalk.demo;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class WekaDemo {

	public static void main(String args[]) throws Exception {
		Attribute Attribute1 = new Attribute("firstNumeric");
		Attribute Attribute2 = new Attribute("secondNumeric");

		FastVector fvNominalVal = new FastVector(3);
		fvNominalVal.addElement("blue");
		fvNominalVal.addElement("gray");
		fvNominalVal.addElement("black");
		Attribute Attribute3 = new Attribute("aNominal", fvNominalVal);

		FastVector fvClassVal = new FastVector(2);
		fvClassVal.addElement("positive");
		fvClassVal.addElement("negative");
		Attribute ClassAttribute = new Attribute("theClass", fvClassVal);

		FastVector fvWekaAttributes = new FastVector(4);
		fvWekaAttributes.addElement(Attribute1);
		fvWekaAttributes.addElement(Attribute2);
		fvWekaAttributes.addElement(Attribute3);
		fvWekaAttributes.addElement(ClassAttribute);

		Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);
		isTrainingSet.setClassIndex(3);

		Instance iExample = new DenseInstance(4);
		iExample.setValue((Attribute) fvWekaAttributes.elementAt(0), 1.0);
		iExample.setValue((Attribute) fvWekaAttributes.elementAt(1), 0.5);
		iExample.setValue((Attribute) fvWekaAttributes.elementAt(2), "gray");
		iExample.setValue((Attribute) fvWekaAttributes.elementAt(3), "positive");

		// add the instance
		isTrainingSet.add(iExample);

		Classifier cModel = (Classifier) new NaiveBayes();
		cModel.buildClassifier(isTrainingSet);
		Evaluation eTest = new Evaluation(isTrainingSet);
		eTest.evaluateModel(cModel, isTrainingSet);
		String strSummary = eTest.toSummaryString();
		System.out.println(strSummary);

		iExample = new DenseInstance(4);
		iExample.setValue((Attribute) fvWekaAttributes.elementAt(0), 1.0);
		iExample.setValue((Attribute) fvWekaAttributes.elementAt(1), 0.5);
		iExample.setValue((Attribute) fvWekaAttributes.elementAt(2), "gray");
		// iExample.setValue((Attribute)fvWekaAttributes.elementAt(3),
		// "positive");
		iExample.setDataset(isTrainingSet);
		System.out.println(cModel.distributionForInstance(iExample));
		for (double d : cModel.distributionForInstance(iExample)) {
			System.out.println(d);
		}

	}

}
