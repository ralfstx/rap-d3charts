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
package org.eclipse.rap.demo.d3chart.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;


public class ChartExamplePage implements IExamplePage {

  private Composite tabBar;
  private Composite mainArea;
  private final List<IExamplePage> subPages = new ArrayList<IExamplePage>();

  public void createControl( Composite parent ) {
    parent.setLayout( new FillLayout() );
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( new FormLayout() );
    mainArea = createMainArea( composite );
    tabBar = createTabBar( composite );
    createItem( "Bar", new BarChartExample() );
    createItem( "Circle", new CircleChartExample() );
    showPage( subPages.get( 0 ) );
  }

  private Composite createMainArea( Composite parent ) {
    Composite area = new Composite( parent, SWT.NONE );
    area.setLayout( new FillLayout() );
    FormData layoutData = new FormData();
    layoutData.top = new FormAttachment( 0 );
    layoutData.left = new FormAttachment( 0 );
    layoutData.right = new FormAttachment( 100 );
    layoutData.bottom = new FormAttachment( 100, -30 );
    area.setLayoutData( layoutData );
    return area;
  }

  private Composite createTabBar( Composite parent ) {
    Composite bar = new Composite( parent, SWT.NONE );
    RowLayout layout = new RowLayout( SWT.HORIZONTAL );
    layout.marginLeft = 25;
    layout.marginRight = 25;
    layout.marginTop = 5;
    layout.marginBottom = 5;
    layout.spacing = 10;
    bar.setLayout( layout );
    FormData layoutData = new FormData();
    layoutData.top = new FormAttachment( 100, -30 );
    layoutData.left = new FormAttachment( 0 );
    layoutData.right = new FormAttachment( 100 );
    layoutData.bottom = new FormAttachment( 100 );
    bar.setLayoutData( layoutData );
    return bar;
  }

  private void createItem( String text, final IExamplePage page ) {
    subPages.add( page );
    final Label label = new Label( tabBar, SWT.NONE );
    label.setText( text );
    label.setCursor( label.getDisplay().getSystemCursor( SWT.CURSOR_HAND ) );
    label.addListener( SWT.MouseDown, new Listener() {
      public void handleEvent( Event event ) {
        label.setForeground( new Color( label.getDisplay(), 0x31, 0x61, 0x9C ) );
        for( Control control : label.getParent().getChildren() ) {
          if( control != label ) {
            control.setForeground( null );
          }
        }
        showPage( page );
      }
    } );
  }

  private void showPage( IExamplePage page ) {
    Control[] children = mainArea.getChildren();
    for( Control control : children ) {
      control.dispose();
    }
    Composite composite = new Composite( mainArea, SWT.NONE );
    page.createControl( composite );
    mainArea.layout();
  }

}
