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


public class ColorStream_Test {

  @Test( expected = NullPointerException.class )
  public void creationFailsWithNull() {
    new ColorStream( null );
  }

  @Test
  public void next_loopsWithSingleColor() {
    ColorStream stream = new ColorStream( new ColorSequence( 23 ) );

    RGB color1 = stream.next();
    RGB color2 = stream.next();

    assertEquals( new RGB( 0, 0, 23 ), color1 );
    assertEquals( new RGB( 0, 0, 23 ), color2 );
  }

  @Test
  public void next_loopsThroughColors() {
    ColorStream stream = new ColorStream( new ColorSequence( 23, 42 ) );

    RGB color1 = stream.next();
    RGB color2 = stream.next();
    RGB color3 = stream.next();

    assertEquals( new RGB( 0, 0, 23 ), color1 );
    assertEquals( new RGB( 0, 0, 42 ), color2 );
    assertEquals( new RGB( 0, 0, 23 ), color3 );
  }

}
