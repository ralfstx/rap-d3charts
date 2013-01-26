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
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectImpl;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


@SuppressWarnings( "restriction" )
public abstract class Chart extends Canvas {

  private final List<ChartItem> items;
  protected final RemoteObject remoteObject;

  public Chart( Composite parent, int style ) {
    super( parent, style );
    items = new ArrayList<ChartItem>();
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( getRemoteType() );
    remoteObject.set( "parent", WidgetUtil.getId( this ) );
    remoteObject.setHandler( new AbstractOperationHandler() {
      @Override
      public void handleNotify( String eventName, Map<String, Object> properties ) {
        if( "Selection".equals( eventName ) ) {
          Event event = new Event();
          event.index = ( ( Integer )properties.get( "index" ) ).intValue();
          event.item = items.get( event.index );
          notifyListeners( SWT.Selection, event );
        }
      }
      @Override
      public void handleCall( String method, Map<String,Object> parameters ) {
        // TODO remove, sending a call as a workaround for missing notify operations
      }
    } );
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
    if( eventType == SWT.Selection && wasListening ) {
      if( !isListening( SWT.Selection ) ) {
        remoteObject.listen( "Selection", false );
      }
    }
  }

  @Override
  protected void removeListener( int eventType, SWTEventListener listener ) {
    super.removeListener( eventType, listener );
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
