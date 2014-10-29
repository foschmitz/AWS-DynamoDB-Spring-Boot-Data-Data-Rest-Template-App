package org.lab.estate.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EstateType {
	HOUSE("Haus"), APARTMENT("Wohnung"), OFFICEBUILDING("Bürogebäude"), PLOT(
			"Grundstück");

	String type;

	private EstateType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	private static final List<EstateType> VALUES = Collections
			.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static EstateType randomEstateType() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}

}
