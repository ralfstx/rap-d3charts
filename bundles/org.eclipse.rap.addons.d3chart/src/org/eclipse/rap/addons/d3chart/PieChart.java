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


public class PieChart extends Chart {

  private static final String REMOTE_TYPE = "d3chart.PieChart";

  private float startAngle = 0;
  private float endAngle = 1;
  private float innerRadius = 0;

  public PieChart( Composite parent, int style ) {
    super( parent, style, REMOTE_TYPE );
  }

  public float getStartAngle() {
    checkWidget();
    return startAngle;
  }

  public void setStartAngle( float angle ) {
    checkWidget();
    if( angle != startAngle ) {
      startAngle = angle;
      remoteObject.set( "startAngle", angle * Math.PI * 2 );
    }
  }

  public float getEndAngle() {
    checkWidget();
    return endAngle;
  }

  public void setEndAngle( float angle ) {
    checkWidget();
    if( angle != endAngle ) {
      endAngle = angle;
      remoteObject.set( "endAngle", angle * Math.PI * 2 );
    }
  }

  public float getInnerRadius() {
    checkWidget();
    return innerRadius;
  }

  public void setInnerRadius( float radius ) {
    checkWidget();
    if( radius != innerRadius ) {
      innerRadius = radius;
      remoteObject.set( "innerRadius", radius );
    }
  }

}
