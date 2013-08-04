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

import org.eclipse.swt.widgets.Composite;


public class BarChart extends Chart {

  private static final String REMOTE_TYPE = "d3chart.BarChart";

  private int barWidth = 25;
  private int spacing = 2;

  public BarChart( Composite parent, int style ) {
    super( parent, style, REMOTE_TYPE );
  }

  public int getBarWidth() {
    checkWidget();
    return barWidth;
  }

  public void setBarWidth( int width ) {
    checkWidget();
    if( width != barWidth ) {
      barWidth = width;
      remoteObject.set( "barWidth", width );
    }
  }

  public int getSpacing() {
    checkWidget();
    return spacing;
  }

  public void setSpacing( int width ) {
    checkWidget();
    if( width != spacing ) {
      spacing = width;
      remoteObject.set( "spacing", width );
    }
  }

}
