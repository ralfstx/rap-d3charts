
describe( "Chart", function() {

  var parent;
  var chart;
  var captor;

  beforeEach( function() {
    rap.setup();
    parent = {
      append: function() {},
      addListener: function() {},
      getClientArea: function() { return [ 0, 0, 0, 0 ] }
    };
    chart = new d3chart.Chart( parent );
    captor = [];
  } );

  it( "has a DOM element", function() {
    expect( chart.getElement() ).toBeDefined();
  } );

  describe( "setType()", function() {

    it( "changes type", function() {
      chart.setType( "foo" );

      expect( chart.getType() ).toEqual( "foo" );
    } );

    it( "schedules update", function() {
      var called = false;
      d3chart.Chart.renderers[ "foo" ] = function() { called = true; };

      chart.setType( "foo" );

      rap.notify( "render" );
      expect( called ).toBe( true );
    } );

  } );

} );
