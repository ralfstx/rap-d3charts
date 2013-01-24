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
package org.eclipse.rap.demo.d3chart.internal.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DataSet {

  private List<String> columns;
  private final List<Item> rows = new ArrayList<Item>();

  public DataSet( String resourceName ) throws IOException {
    InputStream inputStream = DataSet.class.getClassLoader().getResourceAsStream( resourceName );
    if( inputStream == null ) {
      throw new IllegalArgumentException( "Resource not found" );
    }
    BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream, "UTF-8" ) );
    try {
      read( reader );
    } finally {
      reader.close();
    }
  }

  public List<String> getColumns() {
    return Collections.unmodifiableList( columns );
  }

  public int getColumnCount() {
    return columns.size();
  }

  public Item getRow( int index ) {
    return rows.get( index );
  }

  public List<Item> getRows() {
    return Collections.unmodifiableList( rows );
  }

  public int getRowCount() {
    return rows.size();
  }

  private void read( BufferedReader reader ) throws IOException {
    String line = reader.readLine();
    while( line != null ) {
      readLine( line );
      line = reader.readLine();
    }
  }

  private void readLine( String line ) {
    String[] elements = line.split( "," );
    if( columns == null ) {
      readColumns( elements );
    } else {
      readValues( elements );
    }
  }

  private void readColumns( String[] elements ) {
    columns = new ArrayList<String>( elements.length - 1 );
    for( int i = 0; i < elements.length - 1; i++ ) {
      columns.add( elements[ i + 1 ] );
    }
  }

  private void readValues( String[] elements ) {
    String text = elements[ 0 ];
    float[] values = new float[ elements.length - 1 ];
    for( int i = 0; i < values.length; i++ ) {
      values[ i ] = Float.parseFloat( elements[ i + 1 ].trim() );
    }
    rows.add( new Item( text, values ) );
  }

  public static class Item {
    private final String text;
    private final float[] values;

    public Item( String text, float[] values ) {
      this.text = text;
      this.values = values;
    }

    public String getText() {
      return text;
    }

    public float[] getValues() {
      return values;
    }

  }

}