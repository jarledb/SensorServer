google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(function () {
    getTempSensors();
});
setInterval(function () {
    updateTempSensors() // this will run after every 5 seconds
}, 30000);
function updateTempSensors() {
    $("#temprow").children().each(function (index, element) {
        var id = element.id;

        displayTempratureForSensor(id.split("_")[1]);
    });
}
function getTempSensors() {
    $.ajax({
        url: '/data/templog?type=TEMP_TEST',
        dataType: 'json',
    }).done(function (msg) {
        //console.log(msg);
        msg.forEach(function (entry) {
            displayTempratureForSensor(entry.id);
        });
    }).fail(function () {
        console.log("Could't get temp sensors");
    });
};

function displayTempratureForSensor(id) {
    $.ajax({
        url: '/data/templog/' + id,
        dataType: 'json',
    }).done(function (msg) {
        console.log(msg);
        var location = drawTempratureBox(msg);

        if (msg.events && msg.events[0]) {
            plotTempChart(msg.events, location);
        }
    }).fail(function () {
        console.log("Could't get temp sensor data for sensor: " + id);
    });
}

function extractLatestTemprature(msg) {
    var degree = "-";
    if (msg.events && msg.events[0]) {
        $.each(msg.events, function (index, event) {
            if (event && event.values && event.values[0]) {
                $.each(event.values, function (index, value) {
                    if (value.key == "TEMP") {
                        degree = Math.round(value.value * 10) / 10;
                        return false; //break each loop
                    }
                });
            }
            if (degree != "-") { //temp found, break each loop
                return false;
            }
        });
    }
    return degree;
}
function drawTempratureBox(msg) {
    var temprow = document.getElementById("temprow");
    var id = "tempBox_" + msg.id;
    var degree = extractLatestTemprature(msg);

    if (document.getElementById(id)) {
        $("#" + id).find(".degrees").html(degree + "°C")
    } else {
        var col = document.createElement("div");
        col.className = col.className + "col-xs-6 col-md-3";
        col.id = id;

        temprow.appendChild(col);
        var box = document.createElement("div");
        box.className = box.className + "box clearfix";

        col.appendChild(box);
        var name = document.createElement("h2");
        name.appendChild(document.createTextNode(msg.name));

        box.appendChild(name);
        var temp = document.createElement("h2");
        temp.className = temp.className + "degrees " + (degree >= 0 ? "warm" : "cold");
        temp.appendChild(document.createTextNode(degree + "°C"));
        box.appendChild(temp);

        var chart = document.createElement("div");
        chart.className = chart.className + "no_mobile chart";
        box.appendChild(chart);
    }
    ;
    return id;
}
function plotTempChart(events, documentLocation) {
    var data = new google.visualization.DataTable();
    data.addColumn('datetime', '');
    data.addColumn('number', '');
    data.addColumn('boolean', 'axis-crossing point');

    $.each(events, function (index, event) {
        if (event && event.values && event.values[0]) {
            $.each(event.values, function (index, value) {
                if (value.key == "TEMP") {
                    var date = new Date(event.regTime);

                    var d = new Date();
                    d.setDate(d.getDate() - 1);

                    if (date>d) {
                        console.log(date.getHours() + " : " + new Number(value.value).valueOf());
                        data.addRow([date, new Number(value.value).valueOf(), false]);
                    }
                    return false; //break each loop
                }
            });
        }

    });

    var p1, p2, m, b, intersect;
    for (var i = data.getNumberOfRows() - 1; i > 0; i--) {
        p1 = {x: data.getValue(i - 1, 0), y: data.getValue(i - 1, 1)};
        p2 = {x: data.getValue(i, 0), y: data.getValue(i, 1)};

        if ((p1.y >= 0 && p2.y < 0) || (p1.y < 0 && p2.y >= 0)) {
            m = (p2.y - p1.y) / (p2.x - p1.x);
            b = p1.y - m * p1.x;
            intersect = -1 * b / m;
            data.insertRows(i, [
                [new Date(intersect), p1.y, true],
                [new Date(intersect), p2.y, true]
            ]);
        }
    }

    var view = new google.visualization.DataView(data);
    view.setColumns([0, {
        type: 'number',
        label: 'Positive',
        calc: function (dt, row) {
            var y = dt.getValue(row, 1);
            return data.getValue(row, 2) ? 0 : ((y >= 0) ? y : null);
        }
    }, {
        type: 'number',
        label: 'Negative',
        calc: function (dt, row) {
            var y = dt.getValue(row, 1);
            return data.getValue(row, 2) ? 0 : ((y < 0) ? y : null);
        }
    }]);
    var gridColor = '#444';
    var options = {
        curveType: 'function',
        legend: 'none',
        backgroundColor: 'none',
        //width: 900,
        hAxis: {
            //format: 'HH:mm',
            gridlines: {
                color: gridColor, //'none',

                count: -1,
                units: {
                    days: {format: ['MMM dd']},
                    hours: {format: ['HH:mm', 'ha']},
                },

            },
            minorGridlines: {
                color: "#777", //'none',
                units: {
                    hours: {format: ['hh:mm:ss a', 'ha']},
                    minutes: {format: ['HH:mm a Z', ':mm']}
                }
            },

            baselineColor: 'none',
            //direction: -1,
            textStyle: {
                color: gridColor
            },
            //title: 'Timer',
        },
        vAxis: {
            format: '#',
            gridlines: {
                color: gridColor,
            },
            baselineColor: gridColor,
            textStyle: {
                color: gridColor
            }
        },
        series: {
            0: {color: '#CC0000', visibleInLegend: false},
            1: {color: '#0000CC', visibleInLegend: false}
        },
        //timeline: {
        //    groupByRowLabel: true
        //}
    };

    var chart = new google.visualization.LineChart($("#" + documentLocation).find(".chart")[0]);
    chart.draw(view, options);
}
