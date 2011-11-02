package org.liblouis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LouisTest {

	private final String de_g2 = "sbs.dis,sbs-de-core6.cti,sbs-de-accents.cti,sbs-special.cti,sbs-whitespace.mod,sbs-de-letsign.mod,sbs-numsign.mod,sbs-litdigit-upper.mod,sbs-de-core.mod,sbs-de-g2-core.mod,sbs-special.mod";

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

		String a = "\n                       \t                                                                                          ";
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
	public void testWhitespace() {
		assertEquals(" – b ", Louis.squeeze(" \t\n\r   – \t\n\r   b \t\n\r   "));
	}
}
