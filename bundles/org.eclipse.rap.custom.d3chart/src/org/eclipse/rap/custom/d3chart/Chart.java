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
public abstract class Chart extends Canvas {

  private final RemoteObject remoteObject;
  private final List<ChartItem> items;

  public Chart( Composite parent, int style ) {
    super( parent, style );
    items = new ArrayList<ChartItem>();
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( getRemoteType() );
    remoteObject.set( "parent", WidgetUtil.getId( this ) );
    ChartResources.ensureJavaScriptResources();
  }

  public ChartItem[] getItems() {
    return items.toArray( new ChartItem[ 0 ] );
  }

  @Override
  public void dispose() {
    super.dispose();
    remoteObject.destroy();
  }

  protected abstract String getRemoteType();

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
