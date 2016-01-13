google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
    var serverData = $.ajax({
        url: 'http://localhost:8080/data/templog?id=9',
        username: 'admin',
        password: 'admin',
        async: false,
        dataType: 'json',
        // snip repeat
    });
    //var data = google.visualization.arrayToDataTable(serverData['responseJSON'] );
    var data = new google.visualization.DataTable();
    data.addColumn('number', '');
    data.addColumn('number', '');

    data.addRows( serverData['responseJSON']);
    var options = {
        //title: 'Company Performance',
        curveType: 'function',
        legend: 'none', //{position: 'bottom'},
        backgroundColor: 'none',
        hAxis: {
            gridlines: {
                color: 'none',
            },
            baselineColor: 'none',
            direction: -1
        },
        vAxis: {
            gridlines: {
                color: '#000'
            }
        },
        color: 'green',
        //tid tekst på x akse
    };

    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

    chart.draw(data, options);
}