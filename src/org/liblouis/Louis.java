package org.liblouis;

import java.io.UnsupportedEncodingException;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

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

public class Louis {

	private final int outlenMultiplier;
	private final String encoding;
	private final LouisLibrary INSTANCE;

	private static Louis instance;

	/**
	 * public interface that calls Liblouis translate. As a workaround for a bug
	 * in LibLouis, we squeeze all whitespace into a single space before calling
	 * liblouis translate.
	 * 
	 * @param trantab
	 * @param inbuf
	 * @return
	 */
	public static String translate(final String trantab, final String inbuf) {
		return getInstance().translateString(trantab, squeeze(inbuf));
	}

	public static Louis getInstance() {
		if (instance == null) {
			instance = new Louis();
		}
		return instance;
	}

	public String version() {
		return INSTANCE.lou_version();
	}

	private Louis() {
		INSTANCE = (LouisLibrary) Native.loadLibrary(("louis"),
				LouisLibrary.class);
		outlenMultiplier = INSTANCE.lou_charSize();
		switch (outlenMultiplier) {
		case 2:
			encoding = "UTF-16LE";
			break;
		case 4:
			encoding = "UTF-32LE";
			break;
		default:
			throw new RuntimeException(
					"unsuported char size configured in liblouis: "
							+ outlenMultiplier);
		}
	}

	private String translateString(final String trantab, final String inbuf) {
		try {
			final int inlen = inbuf.length();
			if (inlen == 0) {
				return "";
			}
			final byte[] inbufArray = inbuf.getBytes(encoding);
			final byte[] outbufArray = new byte[outlenMultiplier
					* inbufArray.length];
			final IntByReference poutlen = new IntByReference(inlen
					* outlenMultiplier);
			if (INSTANCE.lou_translateString(trantab, inbufArray,
					new IntByReference(inlen), outbufArray, poutlen, null,
					null, 0) == 0) {
				throw new RuntimeException("Unable to complete translation");
			}
			return new String(outbufArray, 0, poutlen.getValue()
					* (inbufArray.length / inlen), encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding not supported by JVM:"
					+ encoding);
		}
	}

	public void louFree() {
		INSTANCE.lou_free();
	}

	public static String squeeze(final String in) {
		return in.replaceAll("(?:\\p{Z}|\\s)+", " ");
	}

	interface LouisLibrary extends Library {

		public int lou_translateString(final String trantab,
				final byte[] inbuf, final IntByReference inlen,
				final byte[] outbuf, final IntByReference outlen,
				final byte[] typeform, final byte[] spacing, final int mode);

		public int lou_charSize();

		public String lou_version();

		public String lou_free();
	}

	public static void main(final String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: prog table(s) string");
			System.exit(1);
		}
		else {
			System.out.println(Louis.translate(args[0], args[1]));
		}

	}
}
