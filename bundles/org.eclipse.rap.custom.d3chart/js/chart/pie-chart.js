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

d3chart.PieChartRenderer = function() {
  this._startAngle = 0;
  this._endAngle = 2 * Math.PI;
  this._outerRadius = 1;
  this._innerRadius = 0;
  this._arc = d3.svg.arc();
  this._layout = d3.layout.pie().sort( null )
    .value( function( item ) { return item.getValue(); } )
    .startAngle( this._startAngle )
    .endAngle( this._endAngle );
};

d3chart.PieChartRenderer.prototype = {

  initialize: function( chart ) {
    this._chart = chart;
    this._updateLayout();
  },

  setStartAngle: function( angle ) {
    this._startAngle = angle;
    this._layout.startAngle( this._startAngle );
    this._chart._scheduleUpdate();
  },

  setEndAngle: function( angle ) {
    this._endAngle = angle;
    this._layout.endAngle( this._endAngle );
    this._chart._scheduleUpdate();
  },

  setInnerRadius: function( radius ) {
    this._innerRadius = radius;
    this._chart._scheduleUpdate( true );
  },

  _updateLayout: function() {
    var centerX = this._chart._width / 2;
    var centerY = this._chart._height / 2;
    var maxRadius = Math.min( centerX, centerY ) - this._chart._padding;
    this._arc
      .outerRadius( this._outerRadius * ( maxRadius ) )
      .innerRadius( this._innerRadius * ( maxRadius ) );
    this._layer = this._chart.getLayer( "layer" );
    this._layer.attr( "transform", "translate(" + centerX + "," + centerY + ")" );
  },

  render : function( chart ) {
    var selection = this._layer.selectAll( "g.piece" )
      .data( this._layout( chart._items ), function( datum ) { return datum.data.id(); } );
    this._show( selection );
    this._updatePieces( selection );
    this._createPieces( selection.enter() );
    this._removePieces( selection.exit() );
  },

  _createPieces: function( selection ) {
    var newGroups = selection.append( "svg:g" )
      .attr( "class", "piece" )
      .attr( "opacity", 0.0 );
    this._createPaths( newGroups );
    this._createTexts( newGroups );
    this._show( newGroups );
  },

  _createPaths: function( selection ) {
    var that = this;
    selection.append( "svg:path" )
      .attr( "fill", function( item ) { return item.data.getColor(); } )
      .attr( "d", that._arc )
      .each( function( datum ) {
        this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
      } );
  },

  _createTexts: function( selection ) {
    var that = this;
    selection.append( "svg:text" )
      .attr( "opacity", 1.0 )
      .style( "font-family", "sans-serif" )
      .style( "font-size", "11px" )
      .style( "fill", "white" )
      .attr( "transform", function( datum ) { return "translate(" + that._arc.centroid( datum ) + ")"; } )
      .attr( "dy", ".35em" )
      .attr( "text-anchor", "middle" )
      .text( function( item ) { return item.data.getText(); } );
  },

  _updatePieces: function( selection ) {
    this._updatePaths( selection.select( "path" ) );
    this._updateTexts( selection.select( "text" ) );
  },

  _updatePaths: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 1000 )
      .attr( "fill", function( item ) { return item.data.getColor(); } )
      .attrTween( "d", function( datum ) {
        var previous = this._buffer;
        var interpolate = d3.interpolate( previous, datum );
        this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
        return function( t ) {
          return that._arc( interpolate( t ) );
        };
      } );
  },

  _updateTexts: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 500 )
      .attr( "opacity", 0.0 )
      .transition()
      .duration( 0 )
      .attr( "transform", function( datum ) { return "translate(" + that._arc.centroid( datum ) + ")"; } )
      .attr( "dy", ".35em" )
      .attr( "text-anchor", "middle" )
      .text( function( item ) { return item.data.getText(); } )
      .transition()
      .duration( 500 )
      .attr( "opacity", 1.0 );
  },

  _removePieces: function( selection ) {
    selection
      .transition()
      .duration( 500 )
      .attr( "opacity", 0.0 )
      .remove();
  },

  _show: function( selection ) {
    selection.transition().duration( 1000 ).attr( "opacity", 1.0 );
  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.PieChart", {

  factory : function( properties ) {
    var parent = rap.getObject( properties.parent );
    var renderer = new d3chart.PieChartRenderer();
    var chart = new d3chart.Chart( parent, renderer );
    chart.setStartAngle = function( angle ) { renderer.setStartAngle( angle ); };
    chart.setEndAngle = function( angle ) { renderer.setEndAngle( angle ); };
    chart.setInnerRadius = function( radius ) { renderer.setInnerRadius( radius ); };
    return chart;
  },

  destructor : "destroy",

  properties: [ "startAngle", "endAngle", "innerRadius" ]

} );
