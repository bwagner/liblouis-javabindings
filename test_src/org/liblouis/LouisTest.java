package org.liblouis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LouisTest {

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
				"N*A BLAZON",
				Louis.translate(
						"sbs.dis,sbs-de-core6.cti,sbs-de-accents-ch.cti,sbs-special.cti,sbs-whitespace.mod,sbs-de-letsign.mod,sbs-numsign.mod,sbs-litdigit-upper.mod,sbs-de-core.mod,sbs-de-g2-core.mod,sbs-special.mod",
						"Nina Blazon"));
	}

	@Test
	public void testEmpty() {
		assertEquals(
				"",
				Louis.translate(
						"sbs.dis,sbs-de-core6.cti,sbs-de-accents-ch.cti,sbs-special.cti,sbs-whitespace.mod,sbs-de-letsign.mod,sbs-numsign.mod,sbs-litdigit-upper.mod,sbs-de-core.mod,sbs-de-g2-core.mod,sbs-special.mod",
						""));
	}

}
