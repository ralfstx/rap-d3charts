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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


@SuppressWarnings( "restriction" )
public class Chart extends Canvas {

  private static final String REMOTE_TYPE = "d3chart.Chart";
  private final RemoteObject remoteObject;
  private final List<ChartItem> items;
  private String type = "bar";

  public Chart( Composite parent, int style ) {
    super( parent, style );
    items = new ArrayList<ChartItem>();
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "parent", WidgetUtil.getId( this ) );
    ChartResources.ensureJavaScriptResources();
  }

  public ChartItem[] getItems() {
    return items.toArray( new ChartItem[ 0 ] );
  }

  public void setType( String type ) {
    this.type = type;
    remoteObject.set( "type", type );
  }

  public String getType() {
    return type;
  }

  @Override
  public void dispose() {
    super.dispose();
    remoteObject.destroy();
  }

  void addItem( ChartItem item ) {
    items.add( item );
  }

  void removeItem( ChartItem item ) {
    items.remove( item );
  }

  String getRemoteId() {
    return ( ( RemoteObjectImpl )remoteObject ).getId();
  }

}
