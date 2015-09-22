package com.movieztalk.reviewsextraction;

public enum NameMonth {
	JAN(1),
	FEB(2),
	Mar(3),
	APR(4),
	MAY(5),
	JUN(6),
	JUL(7),
	AUG(8),
	SEP(9),
	OCT(10),
	NOV(11),
	DEC(12),
	;

	private int monthNumber;

	private NameMonth(int num){
		monthNumber = num;
	}

	public int getMonthNumber(){
		return monthNumber;
	}
}
