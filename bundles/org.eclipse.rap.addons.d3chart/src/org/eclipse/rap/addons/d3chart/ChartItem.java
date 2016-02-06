/*******************************************************************************
 * Copyright (c) 2013, 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart;

import java.util.Arrays;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.JsonMapping;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Item;


public class ChartItem extends Item {

  private static final String REMOTE_TYPE = "d3chart.ChartItem";
  private final RemoteObject remoteObject;
  private final Chart chart;
  private float value;
  private float[] values;
  private Color color;

  public ChartItem( Chart chart ) {
    super( chart, SWT.NONE );
    this.chart = chart;
    chart.addItem( this );
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "parent", chart.getRemoteId() );
  }

  public float getValue() {
    checkWidget();
    return value;
  }

  public void setValue( float value ) {
    checkWidget();
    if( this.value != value ) {
      this.value = value;
      remoteObject.set( "value", value );
    }
  }

  public float[] getValues() {
    checkWidget();
    return values == null
                          ? null
                          : values.clone();
  }

  public void setValues( float... values ) {
    checkWidget();
    if( !Arrays.equals( this.values, values ) ) {
      this.values = values.clone();
      remoteObject.set( "values", jsonArray( values ) );
    }
  }

  public Color getColor() {
    checkWidget();
    return color == null
                         ? getChart().getDisplay().getSystemColor( SWT.COLOR_BLACK )
                         : color;
  }

  public void setColor( Color color ) {
    checkWidget();
    if( color == null
                      ? this.color != null
                      : !color.equals( this.color ) )
    {
      this.color = color;
      remoteObject.set( "color", JsonMapping.toJson( getColor(), 255 ) );
    }
  }

  @Override
  public void setText( String text ) {
    super.setText( text );
    remoteObject.set( "text", text );
  }

  @Override
  public void dispose() {
    super.dispose();
    getChart().removeItem( this );
    remoteObject.destroy();
  }

  private Chart getChart() {
    return chart;
  }

  private static JsonArray jsonArray( float[] values ) {
    // TODO use array.addAll in future versions
    JsonArray array = new JsonArray();
    for( float value : values ) {
      array.add( value );
    }
    return array;
  }
}
