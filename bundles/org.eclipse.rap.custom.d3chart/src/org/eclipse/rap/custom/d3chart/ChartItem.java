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

import org.eclipse.rap.rwt.internal.protocol.ProtocolUtil;
import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectFactory;
import org.eclipse.rap.rwt.lifecycle.WidgetAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Item;


@SuppressWarnings( "restriction" )
public class ChartItem extends Item {

  private static final String REMOTE_TYPE = "d3chart.ChartItem";
  private final RemoteObject remoteObject;
  private double value;
  private Color color;

  public ChartItem( Chart chart ) {
    super( chart, SWT.NONE );
    chart.addItem( this );
    value = 0;
    color = chart.getDisplay().getSystemColor( SWT.COLOR_BLACK );
    remoteObject = RemoteObjectFactory.getInstance().createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "parent", chart.getRemoteId() );
  }

  public void setValue( double value ) {
    this.value = value;
    remoteObject.set( "value", value );
  }

  public double getValue() {
    return value;
  }

  public void setColor( Color color ) {
    this.color = color;
    remoteObject.set( "color", ProtocolUtil.getColorAsArray( color, false ) );
  }

  public Color getColor() {
    return color;
  }

  @Override
  public void dispose() {
    super.dispose();
    getChart().removeItem( this );
  }

  private Chart getChart() {
    return ( Chart )getAdapter( WidgetAdapter.class ).getParent();
  }

}
