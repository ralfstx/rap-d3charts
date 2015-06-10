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

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public abstract class Chart extends Canvas {

  private final List<ChartItem> items;
  protected final RemoteObject remoteObject;

  public Chart( Composite parent, int style, String remoteType ) {
    super( parent, style );
    items = new LinkedList<>();
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( remoteType );
    remoteObject.set( "parent", getId( this ) );
    remoteObject.setHandler( new AbstractOperationHandler() {
      @Override
      public void handleNotify( String eventName, JsonObject properties ) {
        if( "Selection".equals( eventName ) ) {
          Event event = new Event();
          event.index = properties.get( "index" ).asInt();
          event.item = items.get( event.index );
          notifyListeners( SWT.Selection, event );
        }
      }
    } );
    ChartResources.ensureJavaScriptResources();
  }

  public ChartItem[] getItems() {
    checkWidget();
    return items.toArray( new ChartItem[ 0 ] );
  }

  @Override
  public void dispose() {
    super.dispose();
    remoteObject.destroy();
  }

  @Override
  public void addListener( int eventType, Listener listener ) {
    boolean wasListening = isListening( SWT.Selection );
    super.addListener( eventType, listener );
    if( eventType == SWT.Selection && !wasListening ) {
      remoteObject.listen( "Selection", true );
    }
  }

  @Override
  public void removeListener( int eventType, Listener listener ) {
    boolean wasListening = isListening( SWT.Selection );
    super.removeListener( eventType, listener );
    if( eventType == SWT.Selection && wasListening && !isListening( SWT.Selection ) ) {
      remoteObject.listen( "Selection", false );
    }
  }

  void addItem( ChartItem item ) {
    items.add( item );
  }

  void removeItem( ChartItem item ) {
    items.remove( item );
  }

  String getRemoteId() {
    return remoteObject.getId();
  }

}
