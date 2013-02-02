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

d3chart.StreamChart = function( parent ) {
  this._stack = d3.layout.stack()
    .offset( "wiggle" )
    .values( function( d ) { return d.values; } );
  this._items = new d3chart.ItemList();
  this._chart = new d3chart.Chart( parent, this );
};

d3chart.StreamChart.prototype = {

  addItem: function( item ) {
    this._items.add( item );
    this._chart._scheduleUpdate();
  },

  removeItem: function( item ) {
    this._items.remove( item );
    this._chart._scheduleUpdate();
  },

  destroy: function() {
    this._chart.destroy();
  },

  initialize: function( chart ) {
    this._layer = chart.getLayer( "layer" );

    var padding = chart._padding;
    var width = chart._width - chart._padding * 2;
    var height = chart._height - chart._padding * 2;

    this._xScale = d3.scale.linear()
      .domain( [ 0, 17 ] )
      .range( [ padding, width ] );
    this._yScale = d3.scale.linear()
      .domain( [ 0, 160 ] )
      .range( [ height, padding ] );
    var that = this;
    this._area = d3.svg.area()
      .x( function( d ) { return that._xScale( d.x ); } )
      .y0( function( d ) { return that._yScale( d.y0 ); } )
      .y1( function( d ) { return that._yScale( d.y0 + d.y ); } );
  },

  render: function() {

    var data = this._items.map( function( item ) {
      return {
        item: item,
        values: item.getValues().map( function( value, index ) {
          return { x: index, y: value };
        } )
      };
    } );
    var selection = this._layer.selectAll( "g.item" )
      .data( this._stack( data ) ); // , function( item ) { return item.id(); }

    this._createElements( selection.enter() );
    this._updateElements( selection );
    this._removeElements( selection.exit() );
  },

  _createElements: function( selection ) {
    var that = this;
    var items = selection.append( "svg:g" )
      .attr( "class", "item" )
      .attr( "opacity", 1.0 );
    items.on( "click", function( datum, index ) { that._selectItem( index ); } );
    this._createStreams( items );
    this._createTexts( items );
  },

  _createStreams: function( selection ) {
    var that = this;
    selection.append( "svg:path" )
      .attr( "d", function( d ) { return that._area( d.values ); } )
      .style( "fill", function( d ) { return d.item.getColor(); } )
      .append( "svg:title" )
        .text( function( d ) { return d.item.getText(); } );
  },

  _createTexts: function( selection ) {
    selection.append( "svg:text" )
      .attr( "text", function( d ) { return d.item.getText(); } );
  },

  _updateElements: function( selection ) {
    this._updateStreams( selection.select( "path" ) );
  },

  _updateStreams: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 1000 )
      .attr( "d", function( d ) { return that._area( d.values ); } );
  },

  _removeElements: function( selection ) {
    selection
      .transition()
      .duration( 400 )
      .attr( "opacity", 0.0 )
      .remove();
  },

  _selectItem: function( index ) {
    var remoteObject = rap.getRemoteObject( this );
    remoteObject.notify( "Selection", { "index": index } );
  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.StreamChart", {

  factory: function( properties ) {
    var parent = rap.getObject( properties.parent );
    return new d3chart.StreamChart( parent );
  },

  destructor: "destroy",

  properties: [],

  events: [ "Selection" ]

} );
