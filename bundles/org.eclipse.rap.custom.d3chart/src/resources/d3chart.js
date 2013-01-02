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

d3chart = {};

d3chart.Chart = function() {
  var element = document.createElement( "div" );
  element.style.position = "absolute";
  element.style.left = "0px";
  element.style.top = "0px";
  element.style.width = "100%";
  element.style.height = "100%";
// TODO flexible width and height
var width = 250;
var height = 200;
  this._element = element;
  this._xScale = d3.scale.linear()
    .domain( [ 0, 1 ] )
    .range( [ 0, width ] );
  this._data = [ { value: 0.1, color: "#3182bd" },
                 { value: 0.5, color: "#6baed6" },
                 { value: 0.9, color: "#9ecae1" } ];
  this._chart = d3.select( this._element ).append( "svg" )
    .attr( "class", "chart" )
    .attr( "width", width )
    .attr( "height", height );
  this._redraw();
};

d3chart.Chart.prototype = {

  getElement: function() {
    return this._element;
  },

  setData: function( data ) {
    this._data = data;
    this._redraw();
  },

  _redraw: function() {
    var that = this;
    var selection = this._chart.selectAll( "rect" )
      .data( this._data, function( datum ) { return datum.value; } );
    selection.enter().append( "rect" )
      .attr( "x", 10 )
      .attr( "y", function( datum, index ) { return 10 + index * 12; } )
      .attr( "opacity", 1.0 )
      .attr( "width", function( datum ) { return that._xScale( datum.value ); } )
      .attr( "height", 11 )
      .attr( "fill", function( datum ) { return ( datum.color ); } );
    selection
      .attr( "width", function( datum ) { return that._xScale( datum.value ); } );
    selection.exit()
      .remove();
  }
};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.Chart", {

  factory : function( properties ) {
    var chart = new d3chart.Chart();
    rap.getObject( properties.parent ).append( chart.getElement() );
    return chart;
  },

  destructor : function( widget ) {
    var el = widget.getElement();
    if( el.parentNode ) {
      el.parentNode.removeChild( el );
    }
  },

  properties : [
    "data"
  ]

} );
