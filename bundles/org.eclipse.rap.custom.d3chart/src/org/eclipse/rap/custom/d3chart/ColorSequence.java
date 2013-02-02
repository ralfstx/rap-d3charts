/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.custom.d3chart;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;


public class ColorSequence {

  private final int[] colors;

  public ColorSequence( int... colors ) {
    if( colors.length == 0 ) {
      throw new IllegalArgumentException( "Cannot create ColorSequence without any colors" );
    }
    this.colors = colors.clone();
  }

  public ColorStream loop() {
    return new ColorStream( this );
  }

  public int size() {
    return colors.length;
  }

  public RGB get( int index ) {
    int value = colors[ index ];
    return new RGB( value >> 16 & 0xff, value >> 8 & 0xff, value & 0xff );
  }

  public Color get( Device device, int index ) {
    return new Color( device, get( index ) );
  }

}
