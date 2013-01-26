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

d3chart.BarChartRenderer = function() {
  this._barWidth = 25;
  this._spacing = 2;
};

d3chart.BarChartRenderer.prototype = {

  initialize: function( chart ) {
    this._chart = chart;
    this._layer = chart.getLayer( "layer" );
  },

  render : function( chart ) {
    this._xScale = d3.scale.linear().domain( [ 0, 1 ] ).range( [ 0, chart._width - chart._padding * 2 ] );
    var selection = this._layer.selectAll( "g.item" )
      .data( chart._items, function( item ) { return item.id(); } );
    this._createElements( selection.enter() );
    this._updateElements( selection );
    this._removeElements( selection.exit() );
  },

  _createElements: function( selection ) {
    var items = selection.append( "svg:g" )
      .attr( "class", "item" )
      .attr( "opacity", 1.0 );
    this._createBars( items );
    this._createTexts( items );
  },

  _createBars: function( selection ) {
    var that = this;
    selection.append( "svg:rect" )
      .attr( "x", that._chart._padding )
      .attr( "y", function( item, index ) { return that._chart._padding + index * ( that._barWidth + that._spacing ); } )
      .attr( "width", 0 )
      .attr( "height", that._barWidth )
      .attr( "fill", function( item ) { return item.getColor(); } );
  },

  _createTexts: function( selection ) {
    var that = this;
    selection.append( "svg:text" )
      .attr( "text-anchor", "left" )
      .attr( "x", that._chart._padding + 6 )
      .attr( "y", function( item, index ) { return that._chart._padding + index * ( that._barWidth + that._spacing ) + 18; } )
      .style( "font-family", "sans-serif" )
      .style( "font-size", "12px" );
  },

  _updateElements: function( selection ) {
    this._updateBars( selection.select( "rect" ) );
    this._updateTexts( selection.select( "text" ) );
  },

  _updateBars: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 1000 )
      .attr( "y", function( item, index ) { return that._chart._padding + index * ( that._barWidth + that._spacing ); } )
      .attr( "width", function( item ) { return that._xScale( item.getValue() ); } )
      .attr( "fill", function( item ) { return item.getColor(); } );
  },

  _updateTexts: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 1000 )
      .attr( "x", function( item ) { return that._chart._padding + 6 + that._xScale( item.getValue() ); } )
      .attr( "y", function( item, index ) { return that._chart._padding + index * ( that._barWidth + that._spacing ) + 18; } )
      .text( function( item ) { return item.getText(); } );
  },

  _removeElements: function( selection ) {
    selection
      .transition()
      .duration( 400 )
      .attr( "opacity", 0.0 )
      .remove();
  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.BarChart", {

  factory : function( properties ) {
    var parent = rap.getObject( properties.parent );
    var renderer = new d3chart.BarChartRenderer();
    return new d3chart.Chart( parent, renderer );
  },

  destructor : "destroy",

  properties: [ "type" ]

} );
