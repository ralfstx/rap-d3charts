
describe( "Chart", function() {

  var parent;
  var chart;
  var captor;

  beforeEach( function() {
    rap.setup();
    parent = {
      _listeners: {},
      append: function() {},
      addListener: function( event, listener ) {
        if( this._listeners[ event ] === undefined ) {
          this._listeners[ event ] = [];
        }
        this._listeners[ event ].push( listener );
      },
      notify: function( event ) {
        var listeners = this._listeners[ event ];
        if( listeners ) {
          for( var i = 0; i < listeners.length; i++ ) {
            listeners[ i ]();
          }
        }
      },
      getClientArea: function() { return [ 20, 30, 200, 300 ]; }
    };
    captor = [];
  } );

  it( "creates a DOM element and appends it to its parent", function() {
    spyOn( parent, "append" );

    chart = new d3chart.Chart( parent );

    expect( parent.append ).toHaveBeenCalled();
    expect( parent.append.mostRecentCall.args[0] ).toBeDefined();
  } );

  describe( "svg element", function() {

    beforeEach( function() {
      chart = new d3chart.Chart( parent );
    } );

    it( "is created", function() {
      expect( chart._svg.node().tagName ).toBe( "svg" );
      expect( chart._svg.node().parentNode ).toBe( chart._element );
    } );

    it( "has initial size", function() {
      expect( chart._svg.attr( "width" ) ).toBe( "200" );
      expect( chart._svg.attr( "height" ) ).toBe( "300" );
    } );

    it( "is resized on parent resize", function() {
      parent.getClientArea = function() { return [ 20, 30, 400, 600 ]; };

      parent.notify( "Resize" );

      expect( chart._svg.attr( "width" ) ).toBe( "400" );
      expect( chart._svg.attr( "height" ) ).toBe( "600" );
    } );

  } );

  describe( "getLayer", function() {

    beforeEach( function() {
      chart = new d3chart.Chart( parent );
    } );

    it( "returns a d3 selection", function() {
      var layer = chart.getLayer( "foo" );

      expect( typeof layer.data ).toBe( "function" );
      expect( layer.length ).toBe( 1 );
      expect( layer.node().tagName ).toBe( "g" );
      expect( layer.classed( "foo" ) ).toBe( true );
      expect( layer.node().parentNode ).toBe( chart._svg.node() );
    } );

    it( "returns selection with the same nodes for one name", function() {
      var layer1 = chart.getLayer( "foo" );
      var layer2 = chart.getLayer( "foo" );

      expect( layer1.node() ).toBe( layer2.node() );
    } );

  } );

} );
