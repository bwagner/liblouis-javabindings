package org.liblouis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Copyright (C) 2010 Swiss Library for the Blind, Visually Impaired and Print
 * Disabled
 * 
 * This file is part of liblouis-javabindings.
 * 
 * liblouis-javabindings is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

public class LouisTest {

	private final String de_g2 = "sbs.dis,sbs-de-core6.cti,sbs-de-accents.cti,sbs-special.cti,sbs-whitespace.mod,sbs-de-letsign.mod,sbs-numsign.mod,sbs-litdigit-upper.mod,sbs-de-core.mod,sbs-de-g2-core.mod,sbs-special.mod";

	private final String NBSP = "\u00A0";

	@Test
	public void testTranslation() {
		assertEquals("_w dom9,n",
				Louis.translate("en-us-g2.ctb", "world domination"));
	}

	@Test(expected = RuntimeException.class)
	public void testTranslationWrongTable() {
		Louis.translate("wrong_ctb_table_name", "world domination");
	}

	@Test
	public void testMultipleTables() {
		assertEquals(
				"if test fails, first check the list of liblouis tables (first string argument to Louis.translate)",
				"N*A BLAZON", Louis.translate(de_g2, "Nina Blazon"));
	}

	@Test
	public void testEmpty() {
		assertEquals("", Louis.translate(de_g2, ""));
	}

	@Test
	public void testSqueezeEnUs() {

		String a = "\n                       \t                                                                                          ";
		final String en_us = "en-us-g2.ctb";
		assertEquals("bla bla--bla",
				Louis.translate(en_us, "bla    bla\n" + a + "\t\t – bla"));
	}

	@Test
	public void testDashEnUs() {

		final String en_us = "en-us-g2.ctb";
		assertEquals("bla bla--bla",
				Louis.translate(en_us, "bla    bla  – bla"));
	}

	@Test
	public void testSqueezeDe() {

		String a = "\n                       \t                                                                                          ";
		assertEquals("BLA BLA'- BLA",
				Louis.translate(de_g2, "bla    bla\n" + a + "\t\t – bla"));
	}

	@Test
	public void testDe() {

		assertEquals("BLA BLA'- BLA", Louis.translate(de_g2, "bla bla – bla"));
	}

	@Test
	public void testNbsp() {

		assertEquals("BLA BLA", Louis.translate(de_g2, "BLA" + NBSP + "BLA"));
	}

	@Test
	public void testWhitespace() {
		assertEquals(
				" – b ",
				Louis.squeeze(" \t\n\r " + NBSP + "  – \t\n\r " + NBSP
						+ "   b \t\n\r " + NBSP + "  "));
	}
}
