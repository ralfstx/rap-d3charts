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

import static org.junit.Assert.*;

import org.eclipse.swt.graphics.RGB;
import org.junit.Test;


public class ColorSequence_Test {

  @Test( expected = NullPointerException.class )
  public void creationFailsWithNull() {
    new ColorSequence( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void creationFailsWithoutColor() {
    new ColorSequence();
  }

  @Test
  public void loop_createsStreamWithSingleColor() {
    ColorSequence sequence = new ColorSequence( 0 );

    ColorStream stream = sequence.loop();

    assertEquals( new RGB( 0, 0, 0 ), stream.next() );
  }

  @Test
  public void loop_createsStreamWithMultipleColors() {
    ColorSequence sequence = new ColorSequence( 1, 2, 3 );

    ColorStream stream = sequence.loop();

    assertEquals( new RGB( 0, 0, 1 ), stream.next() );
    assertEquals( new RGB( 0, 0, 2 ), stream.next() );
    assertEquals( new RGB( 0, 0, 3 ), stream.next() );
  }

  @Test
  public void keepsSafeCopyOfColors() {
    int[] colors = { 1, 2, 3 };
    ColorSequence sequence = new ColorSequence( colors );

    colors[ 0 ] = 5;
    ColorStream stream = sequence.loop();

    assertEquals( new RGB( 0, 0, 1 ), stream.next() );
  }

  @Test
  public void size() {
    ColorSequence sequence = new ColorSequence( 1, 2, 3 );

    int size = sequence.size();

    assertEquals( 3, size );
  }

  @Test
  public void get_returnsCorrectRGB() {
    ColorSequence sequence = new ColorSequence( 0x12abfe );

    RGB color = sequence.get( 0 );

    assertEquals( new RGB( 0x12, 0xab, 0xfe ), color );
  }

}
