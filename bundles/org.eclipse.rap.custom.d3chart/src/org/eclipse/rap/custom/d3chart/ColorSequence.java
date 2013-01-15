/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Color schemes taken from d3.
 * https://github.com/mbostock/d3/wiki/Ordinal-Scales
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.custom.d3chart;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;


public class ColorSequence {

  int index;

  private final int[] colors;

  private static final int[] DEFAULT_COLORS = { 0x404040 };

  public static final int[] CAT10_COLORS = {
    0x1f77b4,
    0xff7f0e,
    0x2ca02c,
    0xd62728,
    0x9467bd,
    0x8c564b,
    0xe377c2,
    0x7f7f7f,
    0xbcbd22,
    0x17becf
  };

  public static final int[] CAT20_COLORS = {
    0x1f77b4,
    0xaec7e8,
    0xff7f0e,
    0xffbb78,
    0x2ca02c,
    0x98df8a,
    0xd62728,
    0xff9896,
    0x9467bd,
    0xc5b0d5,
    0x8c564b,
    0xc49c94,
    0xe377c2,
    0xf7b6d2,
    0x7f7f7f,
    0xc7c7c7,
    0xbcbd22,
    0xdbdb8d,
    0x17becf,
    0x9edae5
  };

  public ColorSequence() {
    this( DEFAULT_COLORS );
  }

  public ColorSequence( int... colors ) {
    this.colors = colors;
  }

  public Color next( Device device ) {
    int value = colors[ index++ % colors.length ];
    return new Color( device, value >> 16 & 0xff, value >> 8 & 0xff, value & 0xff );
  }

}
