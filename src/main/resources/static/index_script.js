function submitForm(){
    let name = document.querySelector('#medicine_name');
    if(name.value.length > 2) {
        cleanTable();
        var table = document.getElementById('table');
        let district = document.querySelector('#district');
        let town = document.querySelector('#town');
        let chain = document.querySelector('#chain');
        let request = new XMLHttpRequest();
        let url = 'http://localhost:8080/medicine/get/all';
        url += '?name=' + name.value;
        url += '&district=' + district.value;
        url += '&town=' + town.value;
        url += '&chain=' + chain.value;
        url += '&pharmacy=' + pharmacy.value;

        request.open("GET", url);
        request.responseType = 'json';
        request.onload = function () {
            var response = request.response;
            if (response.length > 0) {
                drawTable(response);
            }
        }
         request.send();
    }

}

function submitPharmacy(){

    cleanTable();
    let request = new XMLHttpRequest();
    let url = 'http://localhost:8080/medicine/get/all/from/pharmacy';
    url += '&pharmacy=' + pharmacy.value;
    request.open("GET", url);
    request.responseType = 'json';
    request.onload = function () {
        var response = request.response;
        if (response.length > 0) {
            drawTable(response);
        }
    }
     request.send();


}

function drawTable(data) {

//    var col = [];
//    for (var i = 0; i < data.length; i++) {
//        for (var key in data[i]) {
//            if (col.indexOf(key) === -1) {
//                col.push(key);
//            }
//        }
//    }

    var name = 'name';
    var price = 'price';
    var quantity = 'quantity';
    var pharmacy = 'pharmacy';
    var address = 'address';
    var telephone_numbers = 'telephoneNumbers';
    var manufacturer = 'manufacturer';
    var id = 'id';
    var district = 'district';
    var date = 'date';

    var table = document.getElementById("table");


//    var tr = table.insertRow(-1);

//    for (var i = 0; i < col.length; i++) {
//        var th = document.createElement("th");
//        th.innerHTML = col[i];
//        tr.appendChild(th);
//    }

    for (var i = 0; i < data.length; i++) {

        tr = table.insertRow(-1);
//        tr.setAttribute('class', "selected");

//        for (var j = 0; j < col.length; j++) {
//            var tabCell = tr.insertCell(-1);
//            tabCell.innerHTML = data[i][col[j]];
//        }
        var tabCell = tr.insertCell(-1);
        tabCell.innerHTML = data[i][name];
        var tabCell = tr.insertCell(-1);
        tabCell.innerHTML = data[i][pharmacy][name] + ' (' + data[i][pharmacy][address] + ')';
        var tabCell = tr.insertCell(-1);
        tabCell.innerHTML = data[i][pharmacy][telephone_numbers];
        var tabCell = tr.insertCell(-1);
        tabCell.innerHTML = data[i][pharmacy][district];
        var tabCell = tr.insertCell(-1);
        tabCell.align = 'center';
        tabCell.innerHTML = data[i][price];
        var tabCell = tr.insertCell(-1);
        tabCell.align = 'center';
        tabCell.innerHTML = data[i][quantity];
        var tabCell = tr.insertCell(-1);
        tabCell.innerHTML = data[i][manufacturer];
        var tabCell = tr.insertCell(-1);
        tabCell.innerHTML = data[i][date];
        var tabCell = tr.insertCell(-1);
        tabCell.innerHTML = data[i][id];

    }

//    var divContainer = document.getElementById("showData");
//    divContainer.innerHTML = "";
//    divContainer.appendChild(table);

//    var tbody = document.getElementById('table').getElementsByTagName("TBODY")[0];
//    var row = document.createElement("TR")
//    var td1 = document.createElement("TD")
//    td1.appendChild(document.createTextNode("column 1"))
//    var td2 = document.createElement("TD")
//    td2.appendChild (document.createTextNode("column 2"))
//    row.appendChild(td1);
//    row.appendChild(td2);
//    tbody.appendChild(row);

}

function clearInput() {
    var medicine_name = document.getElementById('medicine_name');
    medicine_name.value = "";
    var district = document.getElementById('district');
    district.value = "";
    var town = document.getElementById('town');
    town.value = "";
    var chain = document.getElementById('chain');
    chain.value = "";
    cleanTable();

}

function cleanTable() {
    for (var i = document.getElementById('table').getElementsByTagName('tr').length -1; i; i--) {
        document.getElementById('table').deleteRow(i);
    }
}

