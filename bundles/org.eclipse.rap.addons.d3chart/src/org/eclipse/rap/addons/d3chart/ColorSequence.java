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
package org.eclipse.rap.addons.d3chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;


public class ColorSequence {

  private final Color[] colors;
  private boolean isDisposed;

  public ColorSequence( Device device, RGB... values ) {
    if( device == null ) {
      throw new NullPointerException( "Device is null" );
    }
    if( device.isDisposed() ) {
      SWT.error( SWT.ERROR_DEVICE_DISPOSED );
    }
    if( values.length == 0 ) {
      throw new IllegalArgumentException( "Cannot create ColorSequence without any colors" );
    }
    colors = new Color[ values.length ];
    for( int i = 0; i < values.length; i++ ) {
      colors[ i ] = new Color( device, values[ i ] );
    }
  }

  public ColorStream loop() {
    checkDisposed();
    return new ColorStream( this );
  }

  public int size() {
    checkDisposed();
    return colors.length;
  }

  public Color get( int index ) {
    checkDisposed();
    return colors[ index ];
  }

  public boolean isDisposed() {
    return isDisposed;
  }

  public void dispose() {
    if( !isDisposed ) {
      for( int i = 0; i < colors.length; i++ ) {
        colors[ i ].dispose();
      }
    }
    isDisposed = true;
  }

  private void checkDisposed() {
    if( isDisposed ) {
      throw new IllegalStateException( "Color sequence disposed" );
    }
  }

}
