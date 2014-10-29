package org.lab.estate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.lab.estate.repository.Estate;
import org.lab.estate.repository.EstateType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a utility class to aid in the construction of Estate objects with
 * random names, urls, and durations. The class also provides a facility to
 * convert objects into JSON using Jackson, which is the format that the
 * EstateSvc controller is going to expect data in for integration testing.
 * 
 * @author jules
 * 
 */
public class TestData {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private enum Streets {
		Birkenstrasse, Hasenweg, Lindenallee, Rosenweg, Dorfplatz, Ostring, Westring, Plautzenweg, Huschenstrasse, Grillh\u00FCtte;

		private static final List<Streets> VALUES = Collections
				.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static Streets randomStreet() {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}

	/**
	 * Construct and return a Estate object with a dec, add and price
	 * 
	 * @return
	 */
	public static Estate randomEstate() {
		// Information about the Estate
		// Construct a random identifier using Java's UUID class
		int housenumber = randInt(1, 100);
		String randStreet = Streets.randomStreet().toString();
		String type = EstateType.randomEstateType().getType().toString();
		String address = randStreet + " " + housenumber;
		long purchasePrice = 60 * (int) Math.rint(Math.random() * 60) * 1000;
		return new Estate(type, address, purchasePrice);
	}

	/**
	 * Convert an object to JSON using Jackson's ObjectMapper
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object o) throws Exception {
		return objectMapper.writeValueAsString(o);
	}

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
