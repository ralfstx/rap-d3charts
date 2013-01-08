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

// CHART

d3chart.Chart = function( parent ) {
  this._type = "bar";
  var element = document.createElement( "div" );
  element.style.position = "absolute";
  element.style.left = "0px";
  element.style.top = "0px";
  element.style.width = "100%";
  element.style.height = "100%";
  this._element = element;
  parent.append( element );
  this._padding = 20;
  this._items = [];
  this._svg = d3.select( this._element ).append( "svg" ).attr( "class", "chart" );
  var that = this;
  this._redrawRunner = function() {
    that._redraw();
  };
  parent.addListener( "Resize", function() {
    that._resize( parent.getClientArea() );
  } );
  this._resize( parent.getClientArea() );
};

d3chart.Chart.prototype = {

  getElement: function() {
    return this._element;
  },

  setType: function( type ) {
    this._type = type;
    // TODO replace with render callback
    window.setTimeout( this._redrawRunner, 30 );
  },

  getType: function() {
    return this._type;
  },

  _addItem: function( item ) {
    this._items.push( item );
    // TODO replace with render callback
    window.setTimeout( this._redrawRunner, 30 );
  },

  _removeItem: function( item ) {
    this._items = this._items.filter( function( element ) {
      return element !== item;
    } );
    // TODO replace with render callback
    window.setTimeout( this._redrawRunner, 30 );
  },

  _resize: function( clientArea ) {
    this._width = clientArea[ 2 ];
    this._height = clientArea[ 3 ];
    this._svg.attr( "width", this._width ).attr( "height", this._height );
    // TODO replace with render callback
    window.setTimeout( this._redrawRunner, 30 );
  },

  _redraw: function() {
    this._renderers[ this._type ]( this );
  },

  _renderers: {

    "bar": function( chart ) {
      var barWidth = 30;
      var xScale = d3.scale.linear().domain( [ 0, 1 ] ).range( [ 0, chart._width - chart._padding * 2 ] );
      var selection = chart._svg.selectAll( "rect" )
        .data( chart._items, function( item ) { return item._rwtId; } );
      selection.enter().append( "rect" )
        .attr( "x", chart._padding )
        .attr( "y", function( item, index ) { return chart._padding + index * barWidth; } )
        .attr( "opacity", 1.0 )
        .attr( "width", 0 )
        .attr( "height", barWidth )
        .attr( "fill", function( item ) { return item.getColor(); } );
      selection
        .transition()
        .duration( 1000 )
        .attr( "y", function( item, index ) { return chart._padding + index * barWidth; } )
        .attr( "width", function( item ) { return xScale( item.getValue() ); } )
        .attr( "fill", function( item ) { return item.getColor(); } );
      selection.exit()
        .transition()
        .duration( 400 )
        .attr( "opacity", 0.0 )
        .remove();
    },

    "pie": function( chart ) {
      var barWidth = 40;
      var startAngle = -90;
      var endAngle = 90;
      var centerX = chart._width / 2;
      var centerY = chart._height - chart._padding;
      var outerRadius = Math.min( chart._width / 2, chart._height - chart._padding ) - chart._padding;
      var innerRadius = Math.max( outerRadius - barWidth, 0 );
      var layout = d3.layout.pie().sort( null )
        .value( function( item ) { return item.getValue(); } )
        .startAngle( startAngle * Math.PI / 180  )
        .endAngle( endAngle * Math.PI / 180  );
      var layer = chart._svg.select( "g.layer" );
      if( layer.empty() ) {
        chart._svg.append( "svg:g" ).attr( "class", "layer" );
        layer = chart._svg.select( "g.layer" );
      }
      layer.attr( "transform", "translate(" + centerX + "," + centerY + ")" );
      var arc = d3.svg.arc()
        .outerRadius( outerRadius )
        .innerRadius( innerRadius );
      var selection = layer.selectAll( "path.piechart_slice" )
        .data( layout( chart._items ), function( item ) { return item.data._rwtId; } );
      selection
        .attr( "fill", function( item ) { return item.data.getColor(); } )
        .transition()
        .duration( 1000 )
        .attr( "opacity", 1.0 )
        .attrTween( "d", function( datum ) {
          var previous = this._buffer;
          var interpolate = d3.interpolate( previous, datum );
          this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
          return function( t ) {
            return arc( interpolate( t ) );
          };
        })
      ;
      selection.enter().append( "svg:path" )
        .attr( "class", "piechart_slice" )
        .attr( "opacity", 0.0 )
        .attr( "fill", function( item ) { return item.data.getColor(); } )
        .attr( "d", arc )
        .each( function( datum ) {
          this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
        } )
        .transition()
        .duration( 1500 )
        .attr( "opacity", 1.0 )
      ;
      selection.exit()
        .transition()
        .duration( 500 )
        .attr( "opacity", 0.0 )
        .remove();
    }

  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.Chart", {

  factory : function( properties ) {
    var parent = rap.getObject( properties.parent );
    var chart = new d3chart.Chart( parent );
    return chart;
  },

  destructor : function( widget ) {
    var el = widget.getElement();
    if( el.parentNode ) {
      el.parentNode.removeChild( el );
    }
  },

  properties: [ "type" ]

} );

// CHART ITEM

d3chart.ChartItem = function( chart ) {
  this._chart = chart;
  this._value = 0;
};

d3chart.ChartItem.prototype = {

  getChart: function() {
    return this._chart;
  },

  getValue: function() {
    return this._value;
  },

  setValue: function( value ) {
    this._value = value;
  },

  getColor: function() {
    return this._color;
  },

  setColor: function( color ) {
    this._color = color;
  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.ChartItem", {

  factory : function( properties ) {
    var chart = rap.getObject( properties.parent );
    var item = new d3chart.ChartItem( chart );
    chart._addItem( item );
    return item;
  },

  destructor : function( item ) {
    item.getChart()._removeItem( item );
  },

  properties : [
    "value", "color"
  ],

  propertyHandler: {
    "color": function( chartItem, value ) {
      chartItem.setColor( "#" + rwt.util.Colors.rgbToHexString( value ) );
    }
  }

} );
