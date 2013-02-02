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

d3chart.BarChart = function( parent ) {
  this._barWidth = 25;
  this._spacing = 2;
  this._chart = new d3chart.Chart( parent, this );
};

d3chart.BarChart.prototype = {

  destroy: function() {
    this._chart.destroy();
  },

  initialize: function( chart ) {
    this._chart = chart;
    this._layer = chart.getLayer( "layer" );
  },

  setBarWidth: function( barWidth ) {
    this._barWidth = barWidth;
    this._chart._scheduleUpdate();
  },

  setSpacing: function( spacing ) {
    this._spacing = spacing;
    this._chart._scheduleUpdate();
  },

  render: function( chart ) {
    this._xScale = d3.scale.linear().domain( [ 0, 1 ] ).range( [ 0, chart._width - chart._padding * 2 ] );
    var selection = this._layer.selectAll( "g.item" )
      .data( chart._items, function( item ) { return item.id(); } );
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
    this._createBars( items );
    this._createTexts( items );
  },

  _createBars: function( selection ) {
    var that = this;
    selection.append( "svg:rect" )
      .attr( "x", that._chart._padding )
      .attr( "y", function( item, index ) { return that._getOffset( index ); } )
      .attr( "width", 0 )
      .attr( "height", that._barWidth )
      .attr( "fill", function( item ) { return item.getColor(); } );
  },

  _createTexts: function( selection ) {
    var that = this;
    selection.append( "svg:text" )
      .attr( "x", that._chart._padding )
      .attr( "y", function( item, index ) { return that._getOffset( index ) + that._barWidth / 2; } )
      .attr( "text-anchor", "left" )
      .attr( "dy", ".35em" )
      .style( "font-family", "sans-serif" )
      .style( "font-size", "11px" );
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
      .attr( "y", function( item, index ) { return that._getOffset( index ); } )
      .attr( "width", function( item ) { return that._xScale( item.getValue() ); } )
      .attr( "height", that._barWidth )
      .attr( "fill", function( item ) { return item.getColor(); } );
  },

  _updateTexts: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 1000 )
      .attr( "x", function( item ) { return that._chart._padding + 6 + that._xScale( item.getValue() ); } )
      .attr( "y", function( item, index ) { return that._getOffset( index ) + that._barWidth / 2; } )
      .text( function( item ) { return item.getText(); } );
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
  },

  _getOffset: function( index ) {
    return this._chart._padding + index * ( this._barWidth + this._spacing );
  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.BarChart", {

  factory: function( properties ) {
    var parent = rap.getObject( properties.parent );
    return new d3chart.BarChart( parent );
  },

  destructor: "destroy",

  properties: [ "barWidth", "spacing" ],

  events: [ "Selection" ]

} );
