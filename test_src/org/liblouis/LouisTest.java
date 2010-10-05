package org.liblouis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class LouisTest {
	
	@Test
	public void testTranslation(){
		assertEquals("_w dom9,n", Louis.translate("en-us-g2.ctb", "world domination"));
	}
	
	 @Test(expected=RuntimeException.class)
	public void testTranslationWrongTable(){
		Louis.translate("wrong_ctb_table_name", "world domination");
	}
}
