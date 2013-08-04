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
package org.eclipse.rap.addons.d3chart;

import org.eclipse.swt.graphics.Color;


public class ColorStream {

  private final ColorSequence sequence;
  private int index;

  public ColorStream( ColorSequence sequence ) {
    if( sequence == null ) {
      throw new NullPointerException( "sequence is null" );
    }
    if( sequence.isDisposed() ) {
      throw new IllegalStateException( "sequence is disposed" );
    }
    this.sequence = sequence;
  }

  public Color next() {
    if( sequence.isDisposed() ) {
      throw new IllegalStateException( "sequence is disposed" );
    }
    return sequence.get( index++ % sequence.size() );
  }

}
