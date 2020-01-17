<%-- 
    Document   : John
    Created on : 26-04-2011, 02:30:51 PM
    Author     : John
--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <title>Drag Feature Example</title>
        <!-- OpenLayers -->
        <link rel="stylesheet" href="/openlayers-2.12/theme/default/style.css" type="text/css">
        <link rel="stylesheet" href="/openlayers-2.12/examples/style.css" type="text/css">
        <style type="text/css">
            #controls {
                width: 512px;
            }
            #controlToggle {
                padding-left: 1em;
            }
            #controlToggle li {
                list-style: none;
            }
        </style>

        <script src="/openlayers-2.12/lib/OpenLayers.js"></script>

        <script type="text/javascript">


            var data = [{
                    id: 0,
                    geom: "POLYGON((-68.765624463558 -12.761719822884,-67.842772901058 -11.926758885384,-66.832030713558 -12.146485447884,-67.535155713558 -13.860352635384,-67.710936963558 -14.958985447884,-67.139647901058 -15.925782322884,-67.095702588558 -16.760743260384,-67.754882276058 -17.507813572884,-68.370116651058 -17.683594822884,-69.249022901058 -17.507813572884,-69.556640088558 -17.463868260384,-69.776366651058 -17.024415135384,-68.897460401058 -16.497071385384,-68.458007276058 -16.189454197884,-68.721679151058 -15.793946385384,-69.380858838558 -15.398438572884,-68.897460401058 -15.002930760384,-69.292968213558 -14.739258885384,-68.765624463558 -14.124024510384,-69.205077588558 -13.816407322884,-68.941405713558 -13.025391697884,-68.765624463558 -12.761719822884))",
                    value: 10
                }, {
                    id: 1,
                    geom: "POLYGON((-69.556640088558 -17.419922947884,-68.853515088558 -18.738282322884,-68.897460401058 -19.529297947884,-67.754882276058 -19.749024510384,-66.612304151058 -19.617188572884,-66.041015088558 -19.221680760384,-66.128905713558 -18.298829197884,-66.392577588558 -17.595704197884,-66.919921338558 -17.463868260384,-67.183593213558 -16.936524510384,-67.666991651058 -17.683594822884,-68.326171338558 -17.771485447884,-69.556640088558 -17.419922947884))",
                    value: 5
                }, {
                    id: 2,
                    geom: "POLYGON((-68.589843213558 -19.661133885384,-68.897460401058 -20.276368260384,-68.194335401058 -21.155274510384,-67.886718213558 -22.473633885384,-67.666991651058 -22.649415135384,-67.183593213558 -22.561524510384,-66.392577588558 -21.990235447884,-65.865233838558 -21.726563572884,-65.337890088558 -21.902344822884,-65.337890088558 -20.979493260384,-65.293944776058 -20.056641697884,-64.810546338558 -19.968751072884,-64.766601026058 -19.265626072884,-65.557616651058 -19.133790135384,-65.469726026058 -18.166993260384,-66.216796338558 -17.727540135384,-65.953124463558 -19.265626072884,-66.612304151058 -19.749024510384,-67.491210401058 -19.836915135384,-68.589843213558 -19.661133885384))",
                    value: 1
                }, {
                    id: 3,
                    geom: "POLYGON((-65.162108838558 -20.935547947884,-65.249999463558 -21.990235447884,-64.546874463558 -22.034180760384,-64.195311963558 -22.737305760384,-63.975585401058 -21.902344822884,-62.481444776058 -21.946290135384,-62.217772901058 -20.803712010384,-65.162108838558 -20.935547947884))",
                    value: 7
                }, {
                    id: 4,
                    geom: "POLYGON((-65.337890088558 -18.298829197884,-64.634765088558 -18.562501072884,-64.458983838558 -18.342774510384,-63.711913526058 -19.001954197884,-63.624022901058 -19.968751072884,-63.492186963558 -20.276368260384,-61.954101026058 -20.188477635384,-62.261718213558 -20.759766697884,-64.766601026058 -20.891602635384,-65.337890088558 -20.847657322884,-65.206054151058 -20.012696385384,-64.502929151058 -19.968751072884,-64.810546338558 -19.221680760384,-65.469726026058 -19.177735447884,-65.337890088558 -18.298829197884))",
                    value: 7
                }, {
                    id: 5,
                    geom: "POLYGON((-66.788085401058 -15.662110447884,-65.909179151058 -16.475098729134,-65.315917432308 -16.299317479134,-65.140136182308 -15.837891697884,-64.502929151058 -15.859864354134,-64.700683057308 -16.826661229134,-64.151366651058 -17.419922947884,-64.634765088558 -17.947266697884,-64.415038526058 -18.298829197884,-64.744628369808 -18.562501072884,-65.403808057308 -18.210938572884,-66.238768994808 -17.815430760384,-66.854003369808 -17.485840916634,-67.161620557308 -16.892579197884,-66.700194776058 -16.804688572884,-66.788085401058 -15.662110447884))",
                    value: 10
                }, {
                    id: 6,
                    geom: "POLYGON((-69.644530713558 -10.894044041634,-68.743651807308 -12.585938572884,-67.776854932308 -11.860840916634,-66.810058057308 -12.036622166634,-66.502440869808 -11.399415135384,-65.359862744808 -10.366700291634,-65.425780713558 -9.5976573228844,-65.623534619808 -9.7734385728844,-66.326659619808 -9.7734385728844,-67.007811963558 -10.103028416634,-67.227538526058 -10.388672947884,-67.688964307308 -10.608399510384,-67.688964307308 -10.762208104134,-68.062499463558 -10.630372166634,-68.501952588558 -10.959962010384,-68.677733838558 -11.069825291634,-69.644530713558 -10.894044041634))",
                    value: 5
                }, {
                    id: 7,
                    geom: "POLYGON((-65.447753369808 -10.542481541634,-66.854003369808 -12.212403416634,-67.732909619808 -14.849122166634,-65.843261182308 -16.343262791634,-65.271972119808 -16.211426854134,-64.964354932308 -15.508301854134,-63.558104932308 -15.903809666634,-63.821776807308 -14.585450291634,-61.932128369808 -13.706544041634,-61.580565869808 -13.574708104134,-63.382323682308 -12.476075291634,-64.437011182308 -12.476075291634,-65.271972119808 -11.685059666634,-65.447753369808 -10.542481541634))", value: 4}, {id: 8, geom: "POLYGON((-61.272948682308 -13.442872166634,-60.306151807308 -14.014161229134,-60.174315869808 -16.211426854134,-58.284667432308 -16.387208104134,-58.196776807308 -17.222169041634,-57.405761182308 -18.057129979134,-58.284667432308 -19.946778416634,-59.251464307308 -19.067872166634,-61.888183057308 -19.683106541634,-62.063964307308 -20.078614354134,-63.514159619808 -20.166504979134,-63.558104932308 -18.892090916634,-64.568847119808 -18.232911229134,-63.865722119808 -17.397950291634,-64.568847119808 -16.694825291634,-64.437011182308 -15.947754979134,-63.294433057308 -15.991700291634,-63.689940869808 -14.673340916634,-61.272948682308 -13.442872166634))",
                    value: 10
                }];

            function featureFromText(the_geom) {
                var feature = new OpenLayers.Feature.Vector(
                        OpenLayers.Geometry.fromWKT(the_geom)
                        );
                return feature;
            }
            ;

            var map, vectors, controls;
            function init() {
                var styles = new OpenLayers.StyleMap({
                    "default": new OpenLayers.Style(null, {
                        rules: [
                            new OpenLayers.Rule({
                                    symbolizer: {
                                        "Point": {
                                            pointRadius: 5,
                                            graphicName: "square",
                                            fillColor: "white",
                                            fillOpacity: 0.25,
                                            strokeWidth: 1,
                                            strokeOpacity: 1,
                                            strokeColor: "#3333aa"
                                        },
                                        "Line": {
                                            strokeWidth: 3,
                                            strokeOpacity: 1,
                                            strokeColor: "#0000ff"
                                        },
                                        "Polygon": {
                                            strokeWidth: 1,
                                            strokeOpacity: 1,
                                            fillColor: "#9999aa",
                                            strokeColor: "#6666aa"
                                        }
                                    }
                                }),
                            new OpenLayers.Rule({
                                filter: new OpenLayers.Filter.Comparison({
                                    type: OpenLayers.Filter.Comparison.BETWEEN,
                                    property: "value",
                                    lowerBoundary: 0,
                                    upperBoundary: 3
                                }),
                                symbolizer: {
                                    strokeWidth: 1,
                                    strokeOpacity: 1,
                                    fillColor: "yellow",
                                    strokeColor: "yellow"
                                }
                            }),
                            new OpenLayers.Rule({
                                filter: new OpenLayers.Filter.Comparison({
                                    type: OpenLayers.Filter.Comparison.BETWEEN,
                                    property: "value",
                                    lowerBoundary: 4,
                                    upperBoundary: 7
                                }),
                                symbolizer: {
                                    strokeWidth: 1,
                                    strokeOpacity: 1,
                                    fillColor: "orange",
                                    strokeColor: "orange"
                                }
                            }),
                            new OpenLayers.Rule({
                                filter: new OpenLayers.Filter.Comparison({
                                    type: OpenLayers.Filter.Comparison.BETWEEN,
                                    property: "value",
                                    lowerBoundary: 8,
                                    upperBoundary: 11
                                }),
                                symbolizer: {
                                    strokeWidth: 1,
                                    strokeOpacity: 1,
                                    fillColor: "red",
                                    strokeColor: "red"
                                }
                            })
                        ]
                    }),
                    "select": new OpenLayers.Style(null, {
                        rules: [
                            new OpenLayers.Rule({
                                symbolizer: {
                                    "Point": {
                                        pointRadius: 5,
                                        graphicName: "square",
                                        fillColor: "red",
                                        fillOpacity: 0.25,
                                        strokeWidth: 2,
                                        strokeOpacity: 1,
                                        strokeColor: "#0000ff"
                                    },
                                    "Line": {
                                        strokeWidth: 3,
                                        strokeOpacity: 1,
                                        strokeColor: "#0000ff"
                                    },
                                    "Polygon": {
                                        strokeWidth: 2,
                                        strokeOpacity: 1,
                                        fillColor: "#0000ff",
                                        strokeColor: "#0000ff"
                                    }
                                }
                            })
                        ]
                    }),
                    "temporary": new OpenLayers.Style(null, {
                        rules: [
                            new OpenLayers.Rule({
                                symbolizer: {
                                    "Point": {
                                        graphicName: "square",
                                        pointRadius: 5,
                                        fillColor: "white",
                                        fillOpacity: 0.25,
                                        strokeWidth: 2,
                                        strokeColor: "#0000ff"
                                    },
                                    "Line": {
                                        strokeWidth: 3,
                                        strokeOpacity: 1,
                                        strokeColor: "#0000ff"
                                    },
                                    "Polygon": {
                                        strokeWidth: 2,
                                        strokeOpacity: 1,
                                        strokeColor: "#0000ff",
                                        fillColor: "#0000ff"
                                    }
                                }
                            })
                        ]
                    })
                });


                map = new OpenLayers.Map('map');
                var wms = new OpenLayers.Layer.WMS("OpenLayers WMS",
                        "http://vmap0.tiles.osgeo.org/wms/vmap0?", {layers: 'basic'});

                // allow testing of specific renderers via "?renderer=Canvas", etc
                var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
                renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;

                vectors = new OpenLayers.Layer.Vector("Vector Layer", {
                    renderers: renderer,
                    styleMap: styles
                });
                var fs = new Array();
                data.forEach(function(p) {
                    var feature = featureFromText(p.geom);
                    feature.attributes.value = p.value;
                    fs.push(feature);
                });
                vectors.addFeatures(fs);

                map.addLayers([wms, vectors]);
                map.addControl(new OpenLayers.Control.LayerSwitcher());
                map.addControl(new OpenLayers.Control.MousePosition());

                controls = {
                    point: new OpenLayers.Control.DrawFeature(vectors,
                            OpenLayers.Handler.Point),
                    line: new OpenLayers.Control.DrawFeature(vectors,
                            OpenLayers.Handler.Path),
                    polygon: new OpenLayers.Control.DrawFeature(vectors,
                            OpenLayers.Handler.Polygon),
                    drag: new OpenLayers.Control.DragFeature(vectors)
                };

                for (var key in controls) {
                    map.addControl(controls[key]);
                }

                map.setCenter(new OpenLayers.LonLat(-65, -17), 5);
                document.getElementById('noneToggle').checked = true;
            }

            function toggleControl(element) {
                for (key in controls) {
                    var control = controls[key];
                    if (element.value == key && element.checked) {
                        control.activate();
                    } else {
                        control.deactivate();
                    }
                }
            }

            function ShowFeatures() {
                if (vectors) {
                    //console.log(vectors.features);
                    vectors.features.forEach(function(f) {
                        console.log(f);
                    });
                } else {
                    console.log('no layer');
                }
            }
        </script>
    </head>
    <body onload="init()">
        <h1 id="title">Drag Feature Example</h1>

        <div id="tags">
            point, line, linestring, polygon, digitizing, geometry, draw, drag
        </div>

        <p id="shortdesc">
            Demonstrates point, line and polygon creation and editing.
        </p>

        <div id="map" class="smallmap"></div>
        <button onclick="ShowFeatures();">do this</button>
        <div id="controls">
            <ul id="controlToggle">
                <li>
                    <input type="radio" name="type" value="none" id="noneToggle"
                           onclick="toggleControl(this);" checked="checked" />
                    <label for="noneToggle">navigate</label>
                </li>
                <li>
                    <input type="radio" name="type" value="point" id="pointToggle" onclick="toggleControl(this);" />
                    <label for="pointToggle">draw point</label>
                </li>
                <li>
                    <input type="radio" name="type" value="line" id="lineToggle" onclick="toggleControl(this);" />
                    <label for="lineToggle">draw line</label>
                </li>
                <li>
                    <input type="radio" name="type" value="polygon" id="polygonToggle" onclick="toggleControl(this);" />
                    <label for="polygonToggle">draw polygon</label>
                </li>
                <li>
                    <input type="radio" name="type" value="drag" id="dragToggle"
                           onclick="toggleControl(this);" />
                    <label for="dragToggle">drag feature</label>
                </li>
            </ul>
        </div>

        <div id="docs"></div>
    </body>
</html>