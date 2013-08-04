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

import org.eclipse.rap.addons.d3chart.Colors;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Colors_Test {

  private Device display;

  @Before
  public void setUp() {
    Fixture.setUp();
    display = new Display();
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test( expected = SWTException.class )
  public void cat10_failsWithDiposedDevice() {
    display.dispose();

    Colors.cat10Colors( display );
  }

  @Test( expected = SWTException.class )
  public void cat20_failsWithDiposedDevice() {
    display.dispose();

    Colors.cat20Colors( display );
  }

}
